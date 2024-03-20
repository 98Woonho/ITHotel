const mainForm = document.querySelector('#mainForm');
const CK_editor = document.querySelector('.ck.ck-content.ck-editor__editable.ck-rounded-corners.ck-editor__editable_inline.ck-blurred');
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
        if (mainForm.editor.getData() === ""){
            alert('내용을 입력하세요');
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
                    console.log(res);
                    alert('공지사항 등록이 완료되었습니다.');
                    location.href = "/board/noticeList";
                }
            })
            .catch(err => {
                console.log(err);
            })
    }
}

