'use strict';

/**
 * スケジュールに戻る
 */
function backward() {
	const facilityTypeId = document.getElementById("facilityTypeId").value;
	location.href = `/school/schedule?facilityTypeId=${facilityTypeId}`;
}

/**
 * 利用人数を施設ごとに変更する処理
 */
document.addEventListener("DOMContentLoaded", function() {
	const facilityIdElem = document.getElementById("facilityId");
	const select = document.getElementById("numberOfPeople");

	if (!facilityIdElem || !select) return;
	console.log("facilityIdElem.value:", facilityIdElem.value);

	const facilityId = parseInt(facilityIdElem.value);

	const optionsMap = {
		1: Array.from({ length: 16 }, (_, i) => i + 5), // カラオケ（5〜20人）
		3: [1],                                         // ブース
		// 他の施設IDがあれば追加
	};

	const options = optionsMap[facilityId] || [1]; // それ以外は1人

	// 初期化
	select.innerHTML = "";

	// オプションを追加
	options.forEach(num => {
		const option = document.createElement("option");
		option.value = num;
		option.textContent = num;
		select.appendChild(option);
	});
	const startSelect = document.getElementById("startTimeOnly");
	const endSelect = document.getElementById("endTimeOnly");

	if (!startSelect || !endSelect) return;

	// 元の終了時間の全選択肢を保存しておく
	const originalEndOptions = Array.from(endSelect.options).map(option => ({
		value: option.value,
		text: option.text,
	}));

	function updateEndTimeOptions() {
		const startValue = startSelect.value;

		// 終了時間の選択肢をいったんクリア
		endSelect.innerHTML = "";

		// 開始時間より後の時間だけ追加
		originalEndOptions.forEach(opt => {
			if (!startValue || opt.value > startValue) {
				const option = document.createElement("option");
				option.value = opt.value;
				option.textContent = opt.text;
				endSelect.appendChild(option);
			}
		});

		// 終了時間の選択値が無くなっていたら空にする
		if (!Array.from(endSelect.options).some(opt => opt.value === endSelect.value)) {
			endSelect.value = "";
		}
	}

	updateEndTimeOptions();

	startSelect.addEventListener("change", updateEndTimeOptions);
});

