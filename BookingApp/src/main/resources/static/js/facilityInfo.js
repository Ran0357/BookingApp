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
document.addEventListener("DOMContentLoaded", function () {
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
});
