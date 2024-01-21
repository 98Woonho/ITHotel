const searchForm = document.getElementById('searchForm');
const reservationForm = document.getElementById('reservationForm');

$(function() {
    $("#datepicker1")
        .datepicker({
            dateFormat: 'yy-mm-dd' //달력 날짜 형태
            ,showMonthAfterYear:true // 월- 년 순서가아닌 년도 - 월 순서
            ,yearSuffix: "년" //달력의 년도 부분 뒤 텍스트
            ,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 텍스트
            ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip
            ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 텍스트
            ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 Tooltip
            ,minDate: "0D" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
            ,maxDate: "+30D" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)
        });
})

$(function() {
    $("#datepicker2")
        .datepicker({
            dateFormat: 'yy-mm-dd' //달력 날짜 형태
            ,showMonthAfterYear:true // 월- 년 순서가아닌 년도 - 월 순서
            ,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 텍스트
            ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip
            ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 텍스트
            ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 Tooltip
            ,minDate: "+1D" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
            ,maxDate: "+31D" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)
        });
})


searchForm.querySelector('.hotelSearch').onclick = function () {
    hotelList.classList.add('visible');
}


/* 성인, 어린이 + - 버튼 js */

let adultNum = Number(searchForm['adultCount'].value);
let childNum = Number(searchForm['childCount'].value);

function adultMinus() {
    if (adultNum > 1) {
        adultNum -= 1;
    }
    searchForm['adultCount'].value = adultNum;
}

function adultPlus() {
    adultNum += 1;
    searchForm['adultCount'].value = adultNum;
}

function childMinus() {
    if (childNum > 0) {
        childNum -= 1;
    }
    searchForm['childCount'].value = childNum;
}

function childPlus() {
    childNum += 1;
    searchForm['childCount'].value = childNum;
}


document.addEventListener('mouseup', function (e) {
    const modal = document.querySelector('[rel="modal"]');
    if (!modal.contains(e.target)) {
        hotelList.classList.remove('visible');
    }
});



