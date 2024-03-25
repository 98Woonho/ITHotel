 function cancel(id){
    axios.delete('/user/deleteReservation/' + parseInt(id))
        .then(res => {
            console.log(res);
            if(res.data === 'Success') {
                alert("예약이 취소되었습니다.");
                location.reload();
            }
            else if(res.data === 'null'){
                alert("예약이 이미 취소되었거나 존재하지 않습니다.");
            }
            else if(res.data === 'fail'){
                alert("예약 당일 혹은 그 이후의 경우 예약취소가 불가능합니다.")
            }
        })
        .catch(err => { console.log(err); })
}