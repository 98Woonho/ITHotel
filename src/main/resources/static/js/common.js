const searchForm = document.getElementById('searchForm');
const hotelList = document.getElementById('hotelList');

// search_fragment js
$('[name="checkin"]')
    .datepicker({
        dateFormat: 'yyyy-mm-dd',
        language: 'ko',
    });

$('[name="checkout"]')
    .datepicker({
        language: 'ko'
    });

searchForm.querySelector('.hotelSearch').onclick = function() {
    hotelList.classList.add('visible');
}

let adultNum = 0;
let childNum = 0;

function adultMinus() {
    if(adultNum > 0)
    {
        adultNum -= 1;
        searchForm.querySelector('.adult-count').innerHTML = adultNum;
    }
    searchForm['adultCount'].value = adultNum;
}

function adultPlus() {
    adultNum += 1;
    searchForm.querySelector('.adult-count').innerHTML = adultNum;
    searchForm['adultCount'].value = adultNum;
}

function childMinus() {
    if(childNum > 0)
    {
        childNum -= 1;
        searchForm.querySelector('.child-count').innerHTML = childNum;
    }
    searchForm['childCount'].value = childNum;
}

function childPlus() {
    childNum += 1;
    searchForm.querySelector('.child-count').innerHTML = childNum;
    searchForm['childCount'].value = childNum;
}



// hotelList_fragment js
const buttons = hotelList.querySelectorAll('.hotelName');

buttons.forEach(
    function(button) {
        button.onclick = function() {
            searchForm['hotel'].value = button.value;
            hotelList.classList.remove('visible');
        };
    }
)