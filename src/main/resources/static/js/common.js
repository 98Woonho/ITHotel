// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
// hotelList js
// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
const hotelList = document.getElementById('hotelList');
const buttons = hotelList.querySelectorAll('.button');

buttons.forEach(
    function (button) {
        button.onclick = function () {
            searchForm['hotelname'].value = button.value;
            hotelList.classList.remove('visible');
        };
    }
)

const searchForm = document.getElementById('searchForm');

$(function () {
    $("#datepicker1")
        .datepicker({
            dateFormat: 'yy-mm-dd' //달력 날짜 형태
            , showMonthAfterYear: true // 월- 년 순서가아닌 년도 - 월 순서
            , monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] //달력의 월 부분 텍스트
            , monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] //달력의 월 부분 Tooltip
            , dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'] //달력의 요일 텍스트
            , dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'] //달력의 요일 Tooltip
            , minDate: "0D" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
            , onSelect: function(selectedDate) {
                const nextDayDate = new Date(selectedDate);
                nextDayDate.setDate(nextDayDate.getDate() + 1);
                $("#datepicker2").datepicker("option", "minDate", nextDayDate);
            }
        });
    if(window.location.pathname === "/") {
        $("#datepicker1")
            .datepicker("setDate", new Date());
    }
})

$(function () {
    $("#datepicker2")
        .datepicker({
            dateFormat: 'yy-mm-dd' //달력 날짜 형태
            , showMonthAfterYear: true // 월- 년 순서가아닌 년도 - 월 순서
            , monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] //달력의 월 부분 텍스트
            , monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] //달력의 월 부분 Tooltip
            , dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'] //달력의 요일 텍스트
            , dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'] //달력의 요일 Tooltip
            , minDate: "+" + 1 + "D" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
        });

    if(window.location.pathname === "/") {
        const currentDate = new Date(searchForm['checkin'].value);
        currentDate.setDate(currentDate.getDate() + 1);
        $("#datepicker2")
            .datepicker("setDate", currentDate);
    }
})


searchForm.querySelector('.hotelSearch').onclick = function () {
    hotelList.classList.add('visible');
}


document.addEventListener('mouseup', function (e) {
    const modal = document.querySelector('[rel="modal"]');
    if (!modal.contains(e.target)) {
        hotelList.classList.remove('visible');
    }
});


searchForm.onsubmit = function (e) {
    e.preventDefault();

    if (searchForm['hotelname'].value === '') {
        alert("호텔을 선택해 주세요.");
        return;
    }

    if (searchForm['checkin'].value === '') {
        alert("체크인 날짜를 선택해 주세요.");
        return;
    }

    if (searchForm['checkout'].value === '') {
        alert("체크아웃 날짜를 선택해 주세요.");
        return;
    }

    const hotelname = searchForm['hotelname'].value;
    const checkin = searchForm['checkin'].value;
    const checkout = searchForm['checkout'].value;
    const adultCount = searchForm['adultCount'].value;
    const childCount = searchForm['childCount'].value;

    location.href = '/reservation/select?hotelname=' + hotelname + '&checkin=' + checkin + '&checkout=' + checkout + '&adultCount=' + adultCount + '&childCount=' + childCount;
}
