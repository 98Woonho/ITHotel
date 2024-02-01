
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

new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['01월', '02월', '03월', '04월', '05월', '06월', '07월', '08월', '09월', '10월', '11월', '12월'],
        datasets: [{
            label: '매출(원)',
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






const categoryBtn = document.querySelector('.category-btn');

categoryBtn.addEventListener('mouseover', function () {
    document.querySelector('.category-container').removeAttribute('hidden');
})

categoryBtn.addEventListener('mouseout', function () {
    document.querySelector('.category-container').setAttribute('hidden', '');
})


const categoryContainer = document.querySelector('.category-container');

categoryContainer.addEventListener('mouseover', function () {
    document.querySelector('.category-container').removeAttribute('hidden');
})

categoryContainer.addEventListener('mouseout', function () {
    document.querySelector('.category-container').setAttribute('hidden', '');
})

const regions = document.querySelectorAll('.region');
const hotelLists = document.querySelectorAll('.hotel-list');
regions.forEach(region => {
    region.addEventListener('mouseover', function () {
        hotelLists.forEach(hotelList => {
            if(region.innerText === hotelList.dataset.value) {
                hotelList.removeAttribute('hidden');
            }
            hotelList.addEventListener('mouseover', function() {
                hotelList.removeAttribute('hidden');
            })
        })
    })

    region.addEventListener('mouseout', function () {
        hotelLists.forEach(hotelList => {
            if(region.innerText === hotelList.dataset.value) {
                hotelList.setAttribute('hidden', '');
            }
            hotelList.addEventListener('mouseout', function() {
                hotelList.setAttribute('hidden','');
            })
        })
    })
})






document.querySelector('.total').addEventListener('click', function () {
    location.href = "/admin/monthSales?region=total";
})


hotelLists.forEach(hotelList => {
    hotelList.querySelector('.link.hotel-total').addEventListener('click', function() {
        location.href = "/admin/monthSales?region=" + hotelList.dataset.value + "&hotelName=" + "total";
    })

    hotelList.querySelector('.link.hotel').addEventListener('click', function() {
        location.href = "/admin/monthSales?region=" + hotelList.dataset.value + "&hotelName=" + hotelList.querySelector('.link.hotel').innerText;
    })
})