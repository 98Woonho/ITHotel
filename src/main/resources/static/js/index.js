let currentIndex = 0;
const totalImages = document.querySelectorAll('.slide ul li').length;
const slide = document.querySelector('.slide ul');

function slideImage(direction) {
    if (direction === 'prev') {
        currentIndex = (currentIndex - 1 + totalImages) % totalImages;
    } else if (direction === 'next') {
        currentIndex = (currentIndex + 1) % totalImages;
    }
    const translateValue = -currentIndex * 33.33333333333 + '%';
    slide.style.transform = 'translateX(' + translateValue + ')';
}

const searchForm = document.getElementById('searchForm');

$(function() {
    $("#datepicker1")
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

/* 체크인, 체크아웃에 default 날짜 설정 js */
// 현재 날짜를 가져오기
const currentDate = new Date();

// YYYY-MM-DD 형식으로 포맷팅
const year = currentDate.getFullYear();
const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
const day = currentDate.getDate().toString().padStart(2, '0');

// YYYY-MM-DD 값을 input 요소의 value 속성에 설정
const checkin = searchForm['checkin'];
checkin.value = `${year}-${month}-${day}`;

// 다음 날 날짜 계산
const nextDay = new Date(currentDate);
nextDay.setDate(currentDate.getDate() + 1);

// YYYY-MM-DD 형식으로 포맷팅
const year1 = nextDay.getFullYear();
const month1 = (nextDay.getMonth() + 1).toString().padStart(2, '0');
const day1 = nextDay.getDate().toString().padStart(2, '0');

// YYYY-MM-DD 값을 input 요소의 value 속성에 설정
const checkout = searchForm['checkout'];
checkout.value = `${year1}-${month1}-${day1}`;






/* 성인, 어린이 + - 버튼 js */
let adultNum = 1;
let childNum = 0;

searchForm['adultCount'].value = adultNum;
searchForm['childCount'].value = childNum;

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



searchForm.onsubmit = function (e) {
    e.preventDefault();

    if (searchForm['hotel'].value === '') {
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

    const hotel = searchForm['hotel'].value;
    const checkin = searchForm['checkin'].value;
    const checkout = searchForm['checkout'].value;
    const adultCount = searchForm['adultCount'].value;
    const childCount = searchForm['childCount'].value;

    location.href = '/hotel/reservationStep1?hotel=' + hotel + '&checkin=' + checkin + '&checkout=' + checkout + '&adultCount=' + adultCount + '&childCount=' + childCount;
}