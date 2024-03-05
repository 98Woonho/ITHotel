const cancelReservation = (button) => {
    const reservationId = button.getAttribute('reservationId');

    axios.get("/reservation/cancel?reservationId=" + reservationId)
        .then(
            res => {
                alert('예약 취소가 완료 되었습니다.');
                location.reload();
            }
        )
        .catch(err => {
            alert('알 수 없는 이유로 예약 취소에 실패 하였습니다. 잠시 후 다시 시도해 주세요.');
        })
}