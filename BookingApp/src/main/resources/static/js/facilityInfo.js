'use strict';

document.addEventListener("DOMContentLoaded", function() {
    const facilityIdElem = document.getElementById("facilityId");
    const numberSelect = document.getElementById("numberOfPeople");

    if (!facilityIdElem || !numberSelect) return;

    const facilityId = parseInt(facilityIdElem.value);

    // 利用人数オプション
    const optionsMap = {
        1: Array.from({ length: 36 }, (_, i) => i + 5), // カラオケ 5~40人
        3: [1] // ブース
    };
    const options = optionsMap[facilityId] || [1];

    numberSelect.innerHTML = "";
    options.forEach(num => {
        const opt = document.createElement("option");
        opt.value = num;
        opt.textContent = num;
        numberSelect.appendChild(opt);
    });

    // 開始・終了時間 input
    const startInput = document.getElementById("startTimeOnly");
    const endInput = document.getElementById("endTimeOnly");
    const startHidden = document.getElementById("startTimeOnlyHidden");
    const endHidden = document.getElementById("endTimeOnlyHidden");

    // GETパラメータから初期値を取得
    const urlParams = new URLSearchParams(window.location.search);
    const startParam = urlParams.get("startDateTime");
    const endParam = urlParams.get("endDateTime");

    if (startParam && endParam) {
        const start = new Date(startParam);
        const end = new Date(endParam);
        startInput.value = start.toTimeString().slice(0,5);
        endInput.value = end.toTimeString().slice(0,5);
        startHidden.value = startInput.value;
        endHidden.value = endInput.value;
    } else if (facilityId === 1) {
        // デフォルト時間
        startInput.value = "15:30";
        endInput.value = "17:30";
        startHidden.value = "15:30";
        endHidden.value = "17:30";
    }

    // 「予約に進む」ボタンで hidden に値をセットして送信
    const confirmBtn = document.getElementById("confirmBtn");
    if (confirmBtn) {
        confirmBtn.addEventListener("click", function() {
            startHidden.value = startInput.value;
            endHidden.value = endInput.value;
            startHidden.form.submit();
        });
    }
});
