const createdAt = document.querySelector('.created-at');

const createdAtDate = new Date(createdAt.value);
const expirationDate = new Date(createdAtDate.getTime() + (10 * 60 * 1000));

let startX;
let startY;
let isDeleteExecuted = false;

document.addEventListener('mousemove', function (event) {
    const currentTime = new Date();
    const reservationId = document.querySelector('.reservation-id').value;
    if (currentTime > expirationDate) {
        if (startX === undefined || startY === undefined) {
            // Initial mouse position
            startX = event.clientX;
            startY = event.clientY;
            return;
        }

        const distanceX = Math.abs(event.clientX - startX);
        const distanceY = Math.abs(event.clientY - startY);

        // Check if the mouse has moved more than the threshold
        if (distanceX > 100 || distanceY > 100) {
            // Reset the starting position for the next check
            startX = event.clientX;
            startY = event.clientY;

            if (!isDeleteExecuted) {
                isDeleteExecuted = true;
                axios.delete(`/reservation/delete/${reservationId}`)
                    .then(res => {
                        alert("예약 확인 및 결제 시간이 초과 하였습니다. 이전 페이지로 이동합니다.");
                        window.history.back();
                    })
                    .catch(err => {
                        console.log(err);
                        isDeleteExecuted = false;
                    })
            }
        }
    }
});

const cardButton = document.querySelector('.card-button');
const kakaopayButton = document.querySelector('.kakaopay-button');
const tosspayButton = document.querySelector('.tosspay-button');
const paycoButton = document.querySelector('.payco-button');
const phonepayButton = document.querySelector('.phonepay-button');

cardButton.addEventListener('click', function () {
    cardButton.classList.add("clicked");
    kakaopayButton.classList.remove("clicked");
    tosspayButton.classList.remove("clicked");
    paycoButton.classList.remove("clicked");
    phonepayButton.classList.remove("clicked");
})

kakaopayButton.addEventListener('click', function () {
    kakaopayButton.classList.add("clicked");
    cardButton.classList.remove("clicked");
    tosspayButton.classList.remove("clicked");
    paycoButton.classList.remove("clicked");
    phonepayButton.classList.remove("clicked");
})

tosspayButton.addEventListener('click', function () {
    tosspayButton.classList.add("clicked");
    cardButton.classList.remove("clicked");
    kakaopayButton.classList.remove("clicked");
    paycoButton.classList.remove("clicked");
    phonepayButton.classList.remove("clicked");
})

paycoButton.addEventListener('click', function () {
    paycoButton.classList.add("clicked");
    cardButton.classList.remove("clicked");
    kakaopayButton.classList.remove("clicked");
    tosspayButton.classList.remove("clicked");
    phonepayButton.classList.remove("clicked");
})

phonepayButton.addEventListener('click', function () {
    phonepayButton.classList.add("clicked");
    cardButton.classList.remove("clicked");
    kakaopayButton.classList.remove("clicked");
    tosspayButton.classList.remove("clicked");
    paycoButton.classList.remove("clicked");
})

const payButton = document.querySelector('.pay-button');

payButton.addEventListener('click', function () {
    const name = document.querySelector('.name').innerHTML;
    const contact = document.querySelector('.contact').innerHTML;
    const price = document.querySelector('.price').innerHTML;
    const address = document.querySelector('.address').value;
    const reservationId = document.querySelector('.reservation-id').value;
    let pg;
    let pay_method;

    if (cardButton.classList.contains("clicked")) {
        pg = "html5_inicis";
        pay_method = "card";
    } else if (kakaopayButton.classList.contains("clicked")) {
        pg = "kakaopay";
        pay_method = "card";
    } else if (tosspayButton.classList.contains("clicked")) {
        pg = "tosspay";
        pay_method = "card";
    } else if (paycoButton.classList.contains("clicked")) {
        pg = "payco"
        pay_method = "card";
    } else if (phonepayButton.classList.contains("clicked")) {
        pg = "danal";
        pay_method = "phone";
    }

    IMP.init("imp40654467");

    IMP.request_pay({
            pg: pg,
            pay_method: pay_method,
            merchant_uid: "merchant_" + new Date().getTime(),
            name: name, // 제품 이름
            amount: 100, // 총 가격
            buyer_tel: contact, // 구매자 폰 번호
        },
        function (resp) {
            const formData = new FormData();
            formData.append("impUid", resp.imp_uid);
            formData.append("merchantUid", resp.merchant_uid);
            formData.append("payMethod", resp.pay_method);
            formData.append("name", encodeURIComponent(name));
            formData.append("paidAmount", resp.paid_amount);
            formData.append("status", resp.status);
            formData.append("address", encodeURIComponent(address));
            formData.append("reservationId", encodeURIComponent(reservationId));
            axios.post("/payment/read", formData)
                .then(resp => {
                    console.log(resp.data);
                    if (resp.data === "SUCCESS") {
                        alert('예약이 완료 되었습니다. 예약 확인 페이지로 이동합니다.');
                    }
                })
                .catch(err => {
                    if (err.response.data === "FAILURE_NOVACANCY") {
                        alert('해당 객실은 예약이 모두 완료 되었습니다. 잠시 후 다시 시도해 주세요.');
                    } else {
                        alert('알 수 없는 이유로 결제에 실패 하였습니다. 잠시 후 다시 시도해 주세요.')
                    }
                })
        });
})