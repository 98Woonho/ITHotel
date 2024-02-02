const hotelInfoButtons = document.querySelectorAll('.hotel-info-button');
const hotelInfos = document.querySelectorAll('.hotel-info');

hotelInfoButtons.forEach(hotelInfoButton => {
    hotelInfoButton.addEventListener('click', function() {
        hotelInfos.forEach(hotelInfo => {
            if(hotelInfoButton.dataset.value === hotelInfo.dataset.value) {
                if(hotelInfo.classList.contains('visible')) {
                    hotelInfo.classList.remove('visible');
                } else {
                    hotelInfo.classList.add('visible');
                }
            }
        })
    });
})
