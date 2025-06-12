'use strict';
dayjs.locale("ja");

document.addEventListener("DOMContentLoaded", function () {
  const facilityTypeId = document.getElementById("facilityTypeId").value;
  const startDate = dayjs().startOf("week").add(1, "day").format("YYYY-MM-DD");
  const endDate = dayjs().add(14, "day").format("YYYY-MM-DD");
  const uri = new URL(window.location.href);
  let closedDates = [];

  fetch(`${uri.origin}/api/facility/${facilityTypeId}/closed-dates?start=${startDate}&end=${endDate}`)
    .then(res => res.json())
    .then(data => {
      closedDates = data;
      const calendarEl = document.getElementById("calendar");

      const calendar = new FullCalendar.Calendar(calendarEl, {
        locale: "ja",
        initialView: "timeGridWeek",
        slotMinTime: "09:00:00",
        slotMaxTime: "17:30:00",
        slotDuration: "00:15:00",
        slotLabelInterval: "00:15:00",
        displayEventTime: false,
        allDaySlot: false,
        slotLabelFormat: {
          hour: 'numeric',
          minute: '2-digit',
          hour12: false
        },
        firstDay: 1,
        nowIndicator: false,
        height: "auto",
        headerToolbar: {
          left: "prev,next",
          center: "title",
          right: "today"
        },
        validRange: {
          start: startDate,
          end: endDate
        },
        selectable: true,
        selectMirror: true,
        selectOverlap: false,

        selectAllow: function (selectInfo) {
          const now = dayjs();
          const selectedStart = dayjs(selectInfo.start);
          const dayStr = selectedStart.format("YYYY-MM-DD");

          // 過去日時禁止
          if (selectedStart.isBefore(now)) return false;

          // 休館日は選択不可
          if (closedDates.includes(dayStr)) return false;

          return true;
        },

        select: function (info) {
          const now = dayjs();
          const selectedStart = dayjs(info.start);
          const dayStr = selectedStart.format("YYYY-MM-DD");

          // 過去日時、休館日なら無視（保険）
          if (selectedStart.isBefore(now)) return;
          if (closedDates.includes(dayStr)) return;

          const start = selectedStart.format("YYYY-MM-DD HH:mm");
          const end = dayjs(info.end).format("YYYY-MM-DD HH:mm");
          const minutes = dayjs(info.end).diff(selectedStart, 'minute');

          if (confirm(`${start} から ${end}（${minutes}分）を予約しますか？`)) {
            console.log("予約確定", {
              start: info.start,
              end: info.end
            });
          }
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

                    // 休館日除外 + 過去日時除外
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

          // 過去日時 or 休館日は何もしない（警告も出さない）
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
