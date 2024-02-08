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


document.addEventListener('scroll', function() {
    const scrollPosition = window.scrollY;
    const mainSection1 = document.querySelector('.main-section-1');
    const mainSection2 = document.querySelector('.main-section-2');
    const swiper = document.querySelector('.swiper');

    if (scrollPosition > 200) {
        mainSection1.classList.add('visible');
    } else {
        mainSection1.classList.remove('visible');
    }

    if (scrollPosition > 650) {
        mainSection2.classList.add('visible');
    } else {
        mainSection2.classList.remove('visible');
    }

    if (scrollPosition > 1150) {
        swiper.classList.add('visible');
    } else {
        swiper.classList.remove('visible');
    }
});



new Swiper('.swiper', {
    cssMode: true,
    navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
    }
})

