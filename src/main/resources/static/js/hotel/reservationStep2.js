const createdAt = document.querySelector('.created-at');

const createdAtDate = new Date(createdAt.value);
const expirationDate = new Date(createdAtDate.getTime() + (6 * 1000));

document.addEventListener('mousemove', function() {
    const currentTime = new Date();
    if(currentTime > expirationDate) {
        alert("예약 확인 및 결제 시간이 초과 하였습니다. 이전 페이지로 이동합니다.");
        window.history.back();
    }
});

document.addEventListener('click', function() {
    const currentTime = new Date();
    if(currentTime > expirationDate) {
        alert("예약 확인 및 결제 시간이 초과 하였습니다. 이전 페이지로 이동합니다.");
        window.history.back();
    }
});

const cardButton = document.querySelector('.card-button');
const kakaopayButton = document.querySelector('.kakaopay-button');
const tosspayButton = document.querySelector('.tosspay-button');
const paycoButton = document.querySelector('.payco-button');
const phonepayButton = document.querySelector('.phonepay-button');

cardButton.addEventListener('click', function() {
    cardButton.classList.add("clicked");
    kakaopayButton.classList.remove("clicked");
    tosspayButton.classList.remove("clicked");
    paycoButton.classList.remove("clicked");
    phonepayButton.classList.remove("clicked");
})

kakaopayButton.addEventListener('click', function() {
    kakaopayButton.classList.add("clicked");
    cardButton.classList.remove("clicked");
    tosspayButton.classList.remove("clicked");
    paycoButton.classList.remove("clicked");
    phonepayButton.classList.remove("clicked");
})

tosspayButton.addEventListener('click', function() {
    tosspayButton.classList.add("clicked");
    cardButton.classList.remove("clicked");
    kakaopayButton.classList.remove("clicked");
    paycoButton.classList.remove("clicked");
    phonepayButton.classList.remove("clicked");
})

paycoButton.addEventListener('click', function() {
    paycoButton.classList.add("clicked");
    cardButton.classList.remove("clicked");
    kakaopayButton.classList.remove("clicked");
    tosspayButton.classList.remove("clicked");
    phonepayButton.classList.remove("clicked");
})

phonepayButton.addEventListener('click', function() {
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

    if(cardButton.classList.contains("clicked")) {
        pg = "html5_inicis";
        pay_method = "card";
    } else if(kakaopayButton.classList.contains("clicked")) {
        pg = "kakaopay";
        pay_method = "card";
    } else if(tosspayButton.classList.contains("clicked")) {
        pg = "tosspay";
        pay_method = "card";
    } else if(paycoButton.classList.contains("clicked")) {
        pg = "payco"
        pay_method = "card";
    } else if(phonepayButton.classList.contains("clicked")) {
        pg = "danal";
        pay_method = "phone";
    }

    IMP.init("imp82217082");

    IMP.request_pay({
            pg: pg,
            pay_method: pay_method,
            merchant_uid: "merchant_" + new Date().getTime(),
            name: name, // 제품 이름
            amount: 100, // 총 가격
            buyer_tel: contact, // 구매자 폰 번호
        },
        function (resp) {
            const params = {
                params: {
                    impUid: resp.imp_uid,
                    merchantUid: resp.merchant_uid,
                    payMethod: resp.pay_method,
                    name: encodeURIComponent(name),
                    paidAmount: resp.paid_amount,
                    status: resp.status,
                    address: encodeURIComponent(address),
                    reservationId: encodeURIComponent(reservationId)
                }
            }
            axios.get("/hotel/payment", params)
                .then(resp => {
                    alert('예약이 완료 되었습니다. 예약 확인 페이지로 이동합니다.');
                    location.href = "/payment/list"
                })
                .catch(err => {
                    console.log(err);
                })
        });
})