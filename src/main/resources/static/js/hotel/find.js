const hotelInfoBtns = document.querySelectorAll('.hotel-info-btn');
const regionUls = document.querySelectorAll('.region-ul');

hotelInfoBtns.forEach(hotelInfoBtn => {
    hotelInfoBtn.addEventListener('click', function() {
        regionUls.forEach(regionUl => {
            if(hotelInfoBtn.dataset.value === regionUl.dataset.value) {
                if(regionUl.classList.contains('visible')) {
                    regionUl.classList.remove('visible');
                } else {
                    regionUl.classList.add('visible');
                }
            }
        })
    });
})
