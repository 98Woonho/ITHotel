hotelList = document.querySelector('.hotel-list');

function selectedHotel() {
    location.href = "/admin/roomStatus?hotelName=" + hotelList.value;
}