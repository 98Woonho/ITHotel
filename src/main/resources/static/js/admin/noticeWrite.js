const mainForm = document.querySelector('#mainForm');
const formData = new FormData();
if (mainForm) {
    ClassicEditor
        .create(mainForm['content'], {
            removePlugins: ['Markdown'],
            simpleUpload: {
                uploadUrl: './image'
            }
        })
        .then(function (editor) {
            mainForm.editor = editor;
        })
        .catch(function (error) {
            console.log(error);
        });
    mainForm.onsubmit = function (e) {
        e.preventDefault();

        if (mainForm['title'].value === "") {
            alert('제목을 입력하세요');
            return;
        }
        formData.append('title', mainForm['title'].value);
        formData.append('content', mainForm.editor.getData());
        if (mainForm['file'].files.length > 0) {
            formData.append('file', mainForm['file'].files[0]);
        }
        axios.post("/admin/noticeWrite", formData, {header: {'Content-Type': 'multipart/form-data'}})
            .then(res => {
                if (res.data === "SUCCESS") {
                    alert('공지사항 등록이 완료되었습니다.');
                    location.href = "/board/noticeList";
                }
            })
            .catch(err => {
                console.log(err);
            })
    }
}

//     });
// const noticeWrite = mainForm.querySelector('.write');
// noticeWrite.addEventListener('click', function (e){
//     e.preventDefault();
//
//     if (mainForm['title'].value === ""){
//         alert('제목을 입력하세요');
//         return;
//     }
//
//     formData.append('title', mainForm['title'].value);
//     formData.append('content', mainForm.editor.getData());
//     formData.append('file', mainForm['file'].value);
//
//     axios.post("/admin/noticeWrite", formData, {header: {'Content-Type': 'multipart/form-data'}})
//         .then(res => {
//             if (res.data === "SUCCESS") {
//                 alert('공지사항 등록이 완료되었습니다.');
//                 location.href = "/board/noticeList";
//             }
//         })
//         .catch(err => {
//             console.log(err);
//         })
// })
// }
