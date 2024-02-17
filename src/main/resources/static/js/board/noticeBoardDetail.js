const deleteNoticeBtn = document.querySelector('.delete_notice_btn');
const NoticeForm = document.querySelector('#notice_form');
console.log(deleteNoticeBtn);
deleteNoticeBtn.addEventListener('click', function (e){
    e.preventDefault();

    if (confirm("공지글을 삭제하겠습니까?")){
        axios.delete("/board/delete?id=" + NoticeForm['idx'].value)
            .then(res => {
                if (res.data === "SUCCESS"){
                    alert("공지가 정상적으로 삭제되었습니다.");
                    location.href = "/board/noticeList";
                }
            })
            .catch(err => {
                alert("알 수 없는 이유로 공지글을 삭제하지 못하였습니다. 잠시 후 다시 시도해주세요.")
            })
    }
})