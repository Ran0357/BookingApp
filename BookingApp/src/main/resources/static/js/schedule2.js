'use strict';
dayjs.locale("ja");

document.addEventListener("DOMContentLoaded", function() {
	const facilityTypeId = document.getElementById("facilityTypeId").value;
	const startDate = dayjs().startOf("week").add(1, "day").format("YYYY-MM-DD");
	const endDate = dayjs().add(14, "day").format("YYYY-MM-DD");
	const uri = new URL(window.location.href);
	let closedDates = [];
	const selectedSlots = [];

	// 選択スロットリスト更新
	function updateSelectedSlotsUI() {
		const list = document.getElementById("selectedSlotsList");
		list.innerHTML = "";
		selectedSlots
			.sort((a, b) => dayjs(a.start) - dayjs(b.start))
			.forEach(slot => {
				const li = document.createElement("li");
				li.className = "list-group-item d-flex justify-content-between align-items-center";
				li.textContent = `${dayjs(slot.start).format("MM/DD HH:mm")} - ${dayjs(slot.end).format("HH:mm")}`;
				const removeBtn = document.createElement("button");
				removeBtn.className = "btn btn-sm btn-outline-danger";
				removeBtn.textContent = "削除";
				removeBtn.onclick = () => {
					const idx = selectedSlots.findIndex(
						s => s.start === slot.start && s.end === slot.end
					);
					if (idx !== -1) selectedSlots.splice(idx, 1);
					updateSelectedSlotsUI();
					updateSelectionMarks();
				};
				li.appendChild(removeBtn);
				list.appendChild(li);
			});

		const confirmBtn = document.getElementById("facilityInfo");
		if (confirmBtn) confirmBtn.disabled = selectedSlots.length === 0;
	}

	// カレンダー上の◎⇄✔️更新
	function updateSelectionMarks() {
		const allEvents = calendar.getEvents();

		allEvents.forEach(event => {
			const isSelected = selectedSlots.some(slot =>
				dayjs(slot.start).isSame(event.start) && dayjs(slot.end).isSame(event.end)
			);

			if (isSelected) {
				event.setProp("title", "✔️");
				event.setProp("textColor", "blue");
			} else {
				event.setProp("title", "◎");
				event.setProp("textColor", "green");
			}
		});
	}

	let calendar;

	// 休館日取得
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
				headerToolbar: { left: "prev,next", center: "title", right: "today" },
				validRange: { start: startDate, end: endDate },
				selectable: false,
				selectMirror: false,
				selectOverlap: false,

				events: function(info, successCallback, failureCallback) {
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
										display: 'auto',
										backgroundColor: "transparent",
										borderColor: "transparent",
										textColor: "green",
										extendedProps: { clickable: event.title !== "×" } // ★追加
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

				eventClick: function(info) {
					info.jsEvent.preventDefault();
					if (info.event.extendedProps.clickable === false) {
						return;
					}
					const selectedStart = dayjs(info.event.start);
					const selectedEnd = dayjs(info.event.end);

					// すでに選択済みなら解除
					const idx = selectedSlots.findIndex(
						slot => dayjs(slot.start).isSame(selectedStart) && dayjs(slot.end).isSame(selectedEnd)
					);
					if (idx !== -1) {
						selectedSlots.splice(idx, 1);
						info.event.setProp("title", "◎");
						info.event.setProp("textColor", "green");
						updateSelectedSlotsUI();
						return;
					}

					// 同じ日のみ選択
					const selectedDay = selectedSlots.length > 0 ? dayjs(selectedSlots[0].start).format("YYYY-MM-DD") : null;
					if (selectedDay && selectedDay !== selectedStart.format("YYYY-MM-DD")) {
						alert("同じ日の時間帯しか選択できません。");
						return;
					}

					// 連続チェック
					const isAdjacent = selectedSlots.some(slot =>
						dayjs(slot.end).isSame(selectedStart) || dayjs(slot.start).isSame(selectedEnd)
					);
					if (!isAdjacent && selectedSlots.length > 0) {
						alert("時間は連続して選択してください。");
						return;
					}

					// 追加
					selectedSlots.push({ start: info.event.start.toISOString(), end: info.event.end.toISOString() });
					info.event.setProp("title", "✔️");
					info.event.setProp("textColor", "blue");
					updateSelectedSlotsUI();
				},

				dayCellClassNames: function(arg) {
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

	// 「予約に進む」ボタン（GET パラメータ遷移版）
	// 「予約に進む」ボタン
	const confirmBtn = document.getElementById("facilityInfo");
	confirmBtn.addEventListener("click", function() {
		if (selectedSlots.length === 0) return;

		// selectedSlots を開始時間でソート
		selectedSlots.sort((a, b) => new Date(a.start) - new Date(b.start));

		const firstSlot = selectedSlots[0];
		const lastSlot = selectedSlots[selectedSlots.length - 1];

		const startDateTime = dayjs(firstSlot.start);
		const endDateTime = dayjs(lastSlot.end);

		// GET パラメータ形式で facilityInfo に遷移
		const facilityId = document.getElementById("facilityTypeId").value;
		const url = new URL(`${window.location.origin}/school/facilityInfo`);
		url.searchParams.set("facilityId", facilityId);
		url.searchParams.set("useDate", startDateTime.format("YYYY-MM-DD"));
		url.searchParams.set("startDateTime", startDateTime.toISOString());
		url.searchParams.set("endDateTime", endDateTime.toISOString());

		window.location.href = url.toString(); // 遷移
	});
});

function backward() {
	location.href = "/school/facilityTypes";
}
