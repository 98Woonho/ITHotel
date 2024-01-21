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

// /* 체크인, 체크아웃에 default 날짜 설정 js */
// // 현재 날짜를 가져오기
// const currentDate = new Date();
//
// // YYYY-MM-DD 형식으로 포맷팅
// const year = currentDate.getFullYear();
// const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
// const day = currentDate.getDate().toString().padStart(2, '0');
//
// // YYYY-MM-DD 값을 input 요소의 value 속성에 설정
// const checkin = searchForm['checkin'];
// checkin.value = `${year}-${month}-${day}`;
//
// // 다음 날 날짜 계산
// const nextDay = new Date(checkin.value);
// nextDay.setDate(currentDate.getDate() + 1);
//
// // YYYY-MM-DD 형식으로 포맷팅
// const year1 = nextDay.getFullYear();
// const month1 = (nextDay.getMonth() + 1).toString().padStart(2, '0');
// const day1 = nextDay.getDate().toString().padStart(2, '0');
//
// // YYYY-MM-DD 값을 input 요소의 value 속성에 설정
// const checkout = searchForm['checkout'];
// checkout.value = `${year1}-${month1}-${day1}`;






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