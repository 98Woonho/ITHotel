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


$('.checkin')
    .datepicker({
        dateFormat: 'yyyy-mm-dd',
        language: 'ko',
    });

$('.checkout')
    .datepicker({
        language: 'ko'
    });