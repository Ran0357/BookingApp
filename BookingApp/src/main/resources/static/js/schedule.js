'use strict';

dayjs.locale("ja");

document.addEventListener("DOMContentLoaded", function() {
	// 施設タイプID取得
	const facilityTypeId = document.getElementById("facilityTypeId").value;

	// 休館日格納用
	let closedDates = [];

	// カレンダー表示対象期間
	const startDate = dayjs().add(1, 'day').format("YYYY-MM-DD");
	const endDate = dayjs().add(91, 'day').format("YYYY-MM-DD");

	// APIのベースURL
	const uri = new URL(window.location.href);

	// 休館日データ取得 → カレンダー初期化
	fetch(`${uri.origin}/api/facility/${facilityTypeId}/closed-dates?start=${startDate}&end=${endDate}`)
		.then(response => {
			if (!response.ok) throw new Error("休館日取得エラー");
			return response.json();
		})
		.then(data => {
			closedDates = data; // 例: ["2025-06-01", "2025-06-02"]

			// カレンダー初期化
			let calendarEl = document.getElementById("calendar");
			let calendar = new FullCalendar.Calendar(calendarEl, {
				locale: "ja",
				height: "auto",
				initialView: "dayGridMonth",
				firstDay: 1,
				headerToolbar: {
					left: "today",
					center: "title",
					right: "prev,next"
				},
				dayCellContent: function(e) {
					e.dayNumberText = e.dayNumberText.replace("日", "");
				},
				validRange: {
					start: startDate,
					end: endDate
				},
				events: function(info, successCallback, failureCallback) {
					const start = dayjs(info.start).format("YYYY-MM-DD");
					const end = dayjs(info.end).subtract(1, "day").format("YYYY-MM-DD");

					fetch(`${uri.origin}/api/schedule/facilityTypes/${facilityTypeId}?start=${start}&end=${end}`)
						.then(response => {
							if (!response.ok) {
								console.error("施設空き情報取得エラー", response);
								failureCallback();
							} else {
								response.json().then(result => {
									// 休館日のイベントは除外
									const filtered = result.filter(event => {
										// イベントの日付フィールドが start と仮定
										const eventDate = dayjs(event.start).format("YYYY-MM-DD");
										return !closedDates.includes(eventDate);
									});
									// ここから追加コード
									const groupedEvents = {};
									filtered.forEach(event => {
										const day = dayjs(event.start).format("YYYY-MM-DD");
										if (!groupedEvents[day]) {
											groupedEvents[day] = {
												title: event.title,
												start: day,
												allDay: true,
												url: event.url
											};
										}
									});
									successCallback(Object.values(groupedEvents));
								});
							}
						})
						.catch(error => {
							console.error(error);
							failureCallback();
						});
				},

				// 休館日を選択不可にする
				selectAllow: function(info) {
					const dateStr = dayjs(info.startStr).format("YYYY-MM-DD");
					return !closedDates.includes(dateStr);
				},
				// 見た目をグレーアウト
				dayCellClassNames: function(arg) {
					const dateStr = dayjs(arg.date).format("YYYY-MM-DD");
					return closedDates.includes(dateStr) ? 'fc-closed-date' : '';
				},
				eventColor: "transparent",
				eventTextColor: "navy",
			});

			calendar.render();
		})
		.catch(error => {
			console.error("休館日取得に失敗しました:", error);
		});
});

/**
 * 施設タイプ一覧に戻る
 */
function backward() {
	location.href = "/school/facilityTypes";
}
