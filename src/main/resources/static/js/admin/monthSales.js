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






const categoryBtn = document.getElementById('categoryBtn');
const category = document.getElementById('category');

categoryBtn.addEventListener('mouseover', function () {
    category.removeAttribute('hidden');
})

categoryBtn.addEventListener('mouseout', function () {
    category.setAttribute('hidden', '');
})

category.addEventListener('mouseover', function () {
    category.removeAttribute('hidden');
})

category.addEventListener('mouseout', function () {
    category.setAttribute('hidden', '');
})

const regions = document.querySelectorAll('.region');
const hotelLists = document.querySelectorAll('.hotel-list');
regions.forEach(region => {
    region.addEventListener('mouseover', function () {
        region.style.color = "blue";
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
            region.style.color = "black";
            if(region.innerText === hotelList.dataset.value) {
                hotelList.setAttribute('hidden', '');
            }
            hotelList.addEventListener('mouseout', function() {
                hotelList.setAttribute('hidden','');
            })
        })
    })
})

const totalLink = document.getElementById('totalLink');

totalLink.addEventListener('click', function () {
    location.href = "/admin/monthSales?region=total";
})

totalLink.addEventListener('mouseover', function () {
    totalLink.style.color = "blue";
})

totalLink.addEventListener('mouseout', function () {
    totalLink.style.color = "black";
})

hotelLists.forEach(hotelList => {
    const hotelToTalLink = hotelList.querySelector('.link.hotel-total');

    hotelToTalLink.addEventListener('click', function() {
        location.href = "/admin/monthSales?region=" + hotelList.dataset.value + "&hotelName=total";
    })

    hotelToTalLink.addEventListener('mouseover', function() {
        hotelToTalLink.style.color = "blue";
    })

    hotelToTalLink.addEventListener('mouseout', function() {
        hotelToTalLink.style.color = "black";
    })

    const hotelLinks = hotelList.querySelectorAll('.hotel.link');

    hotelLinks.forEach(hotelLink => {
        hotelLink.addEventListener('click', function() {
            location.href = "/admin/monthSales?region=" + hotelList.dataset.value + "&hotelName=" + hotelLink.innerText;
        })

        hotelLink.addEventListener('mouseover', function() {
            hotelLink.style.color = "blue";
        })

        hotelLink.addEventListener('mouseout', function() {
            hotelLink.style.color = "black";
        })
    })
})