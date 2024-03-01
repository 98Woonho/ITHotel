 function cancel(id){
    axios.delete('/user/deleteReservation/' + parseInt(id))
        .then(res => {
            console.log(res);
            if(res.data === 'Success') {
                alert("예약이 취소되었습니다.");
                location.reload();
            }
            else
                alert("예약일이 당일이거나 지난 경우 취소가 불가능합니다.")
        })
        .catch(err => { console.log(err) })
}