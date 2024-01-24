/* 성인, 어린이 + - 버튼 js */
let adultNum = Number(searchForm['adultCount'].value);
let childNum = Number(searchForm['childCount'].value);

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


document.querySelectorAll('.reservation-form').forEach(function(form) {
    form.onsubmit = function(e) {
        e.preventDefault();

        if(form['remainingRoomCount'].value === '0') {
            alert('다른 이용자가 마지막 객실을 예약 진행중에 있습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }

        const formData = new FormData();
        formData.append("checkin", searchForm['checkin'].value);
        formData.append("checkout", searchForm['checkout'].value);
        formData.append("roomId", form['roomId'].value);
        formData.append("status", "예약 중");
        formData.append("people", form['people'].value);
        formData.append("price", form.querySelector('.price').innerText);
        axios.post("/hotel/reservationStep1", formData)
            .then(res => {  
                console.log(res);
                location.href = "/hotel/reservationStep2";
            })
            .catch(err => {
                console.log(err);
            })
    };
});