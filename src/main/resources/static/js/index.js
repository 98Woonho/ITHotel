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