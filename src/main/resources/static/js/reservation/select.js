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

        

        const formData = new FormData();
        formData.append("checkin", searchForm['checkin'].value);
        formData.append("checkout", searchForm['checkout'].value);
        formData.append("roomId", form['roomId'].value);
        formData.append("status", "예약 중");
        formData.append("people", form['people'].value);
        formData.append("price", form.querySelector('.price').innerText);
        axios.post("/reservation/select", formData)
            .then(res => {  
                console.log(res);
                location.href = "/payment/read";
            })
            .catch(err => {
                console.log(err);
            })
    };
});