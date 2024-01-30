const ctx = document.getElementById('salesGraph');

const januarySales = document.querySelector('.january-sales').innerText;
const februarySales = document.querySelector('.february-sales').innerText;
const marchSales = document.querySelector('.march-sales').innerText;
const aprilSales = document.querySelector('.april-sales').innerText;
const maySales = document.querySelector('.may-sales').innerText;
const junSales = document.querySelector('.jun-sales').innerText;
const julySales = document.querySelector('.july-sales').innerText;
const augustSales = document.querySelector('.august-sales').innerText;
const septemberSales = document.querySelector('.september-sales').innerText;
const octoberSales = document.querySelector('.october-sales').innerText;
const novemberSales = document.querySelector('.november-sales').innerText;
const decemberSales = document.querySelector('.december-sales').innerText;

const option = document.querySelector('.option');

option.addEventListener('change', function() {
    const seletedvalue = option.value;
    location.href="/admin/monthSales?value=" + seletedvalue;
})

if (document.querySelector('.value').innerText === "") {
    option.value = document.querySelector('.total').innerText;
} else {
    option.value = document.querySelector('.value').innerText;
}
new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['01월', '02월', '03월', '04월', '05월', '06월', '07월', '08월', '09월', '10월', '11월', '12월'],
        datasets: [{
            label: '매출',
            data: [januarySales, februarySales, marchSales, aprilSales, maySales, junSales, julySales, augustSales, septemberSales, octoberSales, novemberSales, decemberSales],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});