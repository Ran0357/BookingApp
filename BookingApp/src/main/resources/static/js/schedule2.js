'use strict';
dayjs.locale("ja");

document.addEventListener("DOMContentLoaded", function () {
  const facilityTypeId = document.getElementById("facilityTypeId").value;
  const startDate = dayjs().startOf("week").add(1, "day").format("YYYY-MM-DD");
  const endDate = dayjs().add(14, "day").format("YYYY-MM-DD");
  const uri = new URL(window.location.href);
  let closedDates = [];
  const selectedSlots = [];

  function updateSelectedSlotsUI() {
    const list = document.getElementById("selectedSlotsList");
    list.innerHTML = "";
    selectedSlots.sort((a, b) => dayjs(a.start) - dayjs(b.start)).forEach(slot => {
      const li = document.createElement("li");
      li.className = "list-group-item d-flex justify-content-between align-items-center";
      li.textContent = `${dayjs(slot.start).format("MM/DD HH:mm")} - ${dayjs(slot.end).format("HH:mm")}`;
      const removeBtn = document.createElement("button");
      removeBtn.className = "btn btn-sm btn-outline-danger";
      removeBtn.textContent = "削除";
      removeBtn.onclick = () => {
        const idx = selectedSlots.findIndex(s => s.start === slot.start && s.end === slot.end);
        if (idx !== -1) selectedSlots.splice(idx, 1);
        updateSelectedSlotsUI();
        updateSelectionMarks();
      };
      li.appendChild(removeBtn);
      list.appendChild(li);
    });

    document.getElementById("confirmBooking").disabled = selectedSlots.length === 0;
  }

  function updateSelectionMarks() {
    const allEvents = calendar.getEvents();
    allEvents.forEach(event => {
      if (event.extendedProps.type === 'selection') event.remove();
    });

    selectedSlots.forEach(slot => {
      calendar.addEvent({
        start: slot.start,
        end: slot.end,
        title: '✔️',
        display: 'background',
        backgroundColor: '#b3e5fc',
        borderColor: '#0288d1',
        textColor: '#0288d1',
        extendedProps: { type: 'selection' }
      });
    });
  }

  document.getElementById("confirmBooking").addEventListener("click", () => {
    if (selectedSlots.length === 0) return;
    const summary = selectedSlots.map(s => `${dayjs(s.start).format("MM/DD HH:mm")} - ${dayjs(s.end).format("HH:mm")}`).join("\n");
    if (confirm(`以下の時間帯を予約しますか？\n\n${summary}`)) {
      console.log("予約確定:", selectedSlots);
      // API送信処理
    }
  });

  let calendar;

  fetch(`${uri.origin}/api/facility/${facilityTypeId}/closed-dates?start=${startDate}&end=${endDate}`)
    .then(res => res.json())
    .then(data => {
      closedDates = data;
      const calendarEl = document.getElementById("calendar");

      calendar = new FullCalendar.Calendar(calendarEl, {
        locale: "ja",
        initialView: "timeGridWeek",
        slotMinTime: "09:00:00",
        slotMaxTime: "17:30:00",
        slotDuration: "00:15:00",
        slotLabelInterval: "00:15:00",
        displayEventTime: false,
        allDaySlot: false,
        slotLabelFormat: { hour: 'numeric', minute: '2-digit', hour12: false },
        firstDay: 1,
        nowIndicator: false,
        height: "auto",
        headerToolbar: {
          left: "prev,next",
          center: "title",
          right: "today"
        },
        validRange: { start: startDate, end: endDate },
        selectable: true,
        selectMirror: true,
        selectOverlap: false,

        selectAllow: function (selectInfo) {
          const now = dayjs();
          const selectedStart = dayjs(selectInfo.start);
          const dayStr = selectedStart.format("YYYY-MM-DD");
          if (selectedStart.isBefore(now)) return false;
          if (closedDates.includes(dayStr)) return false;
          return true;
        },

        select: function (info) {
          const selectedStart = dayjs(info.start);
          const selectedEnd = dayjs(info.end);
          const dayStr = selectedStart.format("YYYY-MM-DD");

          if (selectedStart.isBefore(dayjs()) || closedDates.includes(dayStr)) return;

          const overlap = selectedSlots.some(slot =>
            dayjs(slot.start).isBefore(selectedEnd) && selectedStart.isBefore(dayjs(slot.end))
          );

          if (overlap) {
            alert("既に選択済みの時間と重複しています。");
            return;
          }

          selectedSlots.push({ start: info.startStr, end: info.endStr });
          updateSelectedSlotsUI();
          updateSelectionMarks();
        },

        events: function (info, successCallback, failureCallback) {
          const start = dayjs(info.start).format("YYYY-MM-DD");
          const end = dayjs(info.end).format("YYYY-MM-DD");

          fetch(`${uri.origin}/api/schedule/facilityTypes/${facilityTypeId}?start=${start}&end=${end}`)
            .then(response => {
              if (!response.ok) {
                console.error("施設空き情報取得エラー", response);
                failureCallback();
              } else {
                response.json().then(result => {
                  const now = dayjs();
                  const filtered = result.filter(event => {
                    const eventStart = dayjs(event.start);
                    const eventDate = eventStart.format("YYYY-MM-DD");
                    return !closedDates.includes(eventDate) && eventStart.isAfter(now);
                  });

                  const events = filtered.map(event => ({
                    title: event.title,
                    start: event.start,
                    end: event.end,
                    allDay: false,
                    url: event.url,
                    backgroundColor: "transparent",
                    borderColor: "transparent",
                    textColor: "#000"
                  }));

                  successCallback(events);
                });
              }
            })
            .catch(error => {
              console.error(error);
              failureCallback();
            });
        },

        dateClick: function (info) {
          const clickedDateTime = dayjs(info.date);
          const dayStr = clickedDateTime.format("YYYY-MM-DD");
          if (clickedDateTime.isBefore(dayjs()) || closedDates.includes(dayStr)) {
            return;
          }
        },

        dayCellClassNames: function (arg) {
          const dateStr = dayjs(arg.date).format("YYYY-MM-DD");
          const nowStr = dayjs().startOf("day").format("YYYY-MM-DD");

          if (closedDates.includes(dateStr) && dayjs(dateStr).isBefore(nowStr)) {
            return ['fc-closed-date', 'fc-past-date'];
          } else if (closedDates.includes(dateStr)) {
            return 'fc-closed-date';
          } else if (dayjs(dateStr).isBefore(nowStr)) {
            return 'fc-past-date';
          }
          return '';
        },

        eventColor: "transparent",
        eventTextColor: "navy",
      });

      calendar.render();
    })
    .catch(err => console.error("休館日取得失敗:", err));
});

function backward() {
  location.href = "/school/facilityTypes";
}
