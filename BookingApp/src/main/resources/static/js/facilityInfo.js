'use strict';

/**
 * スケジュールに戻る
 */
function backward() {
  const facilityTypeId = document.getElementById("facilityTypeId").value;
  location.href=`/school/schedule?facilityTypeId=${facilityTypeId}`
}
