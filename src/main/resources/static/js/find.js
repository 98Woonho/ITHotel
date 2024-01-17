const main = document.getElementById('main');
const hotelInfoButtons = document.getElementsByClassName('hotel-info-button');
const hotelInfo = document.getElementsByClassName('hotel-info');

for (let i = 0; i < hotelInfoButtons.length; i++) {
    hotelInfoButtons[i].addEventListener('click', function() {
        if(hotelInfo[i].classList.contains('visible')) {
            hotelInfo[i].classList.remove('visible');
        } else {
            hotelInfo[i].classList.add('visible');
        }
    });
}