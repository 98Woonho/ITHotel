const hotelInfoBtns = document.querySelectorAll('.hotel-info-btn');
const hotelInfos = document.querySelectorAll('.hotel-info');

hotelInfoBtns.forEach(hotelInfoBtn => {
    hotelInfoBtn.addEventListener('click', function() {
        hotelInfos.forEach(hotelInfo => {
            if(hotelInfoBtn.dataset.value === hotelInfo.dataset.value) {
                if(hotelInfo.classList.contains('visible')) {
                    hotelInfo.classList.remove('visible');
                } else {
                    hotelInfo.classList.add('visible');
                }
            }
        })
    });
})
