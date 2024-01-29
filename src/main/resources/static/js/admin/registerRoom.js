const hotelList = document.querySelector('.hotel-list');
const roomForm = document.querySelector('.room-form');
const mainImg = document.querySelector('.main-img');
const roomImg = document.querySelector('.room-img');

function selectedHotel() {
    location.href = "/admin/registerRoom?hotelName=" + hotelList.value;
}

// 추가 이미지

const formData = new FormData();
const roomUploadBox = roomImg.querySelector('.room-upload-box');

roomUploadBox.addEventListener('dragenter', function (e) {
    e.preventDefault();
});
roomUploadBox.addEventListener('dragover', function (e) {
    e.preventDefault();
    roomUploadBox.style.opacity = '0.5';

});
roomUploadBox.addEventListener('dragleave', function (e) {
    e.preventDefault();
    roomUploadBox.style.opacity = '1';
});

let fileNameArray = [];

roomUploadBox.addEventListener('drop', function (e) {
    e.preventDefault();

    // 유효성 체크
    let imgFiles = Array.from(e.dataTransfer.files).filter(f => f.type.startsWith('image/')); // type이 image/로 시작하는 파일들만 가져와서 배열로 구성
    if (imgFiles.length === 0) {
        alert("이미지 파일만 가능합니다.");
        return false;
    }

    // 이미지 파일 용량 제한
    imgFiles.forEach(file => {
        if (file.size > (1024 * 1024 * 5)) {
            alert("파일 하나당 최대 사이즈는 5MB이하여야 합니다.")
        }
    })

    const reader = new FileReader(); // FileReader

    for (const file of imgFiles) {
        fileNameArray.push(file.name);
        reader.readAsDataURL(file); // reader에 file 정보를 넣어줌.
        reader.onload = function (e) { // preview 태그에 이미지가 업로드 되었을 때 동작 함수
            const preview = document.querySelector('#previewRoom');
            const src = e.target.result;

            const item = new DOMParser().parseFromString(`
                <li class="item">
                    <input hidden type="text" class="file-name" name="fileName" th:value="${file.name}">
                    <img class="img" src="${src}" alt="">
                    <a class="btn btn-secondary delete-btn">삭제</a>
                </li>
            `, 'text/html').querySelector('.item');
            const deleteBtn = item.querySelector('.delete-btn');

            preview.append(item);
            preview.scrollLeft = preview.scrollWidth; // 파일이 추가 되면 스크롤을 오른쪽 끝으로 알아서 당겨줌.

            deleteBtn.onclick = function () {
                fileNameArray = fileNameArray.filter(name => name !== file.name);
                item.remove();
            }
        }
        formData.append("files", file);
    }
});



// 대표 이미지

let mainFileName;
const mainUploadBox = mainImg.querySelector('.main-upload-box');

mainUploadBox.addEventListener('dragenter', function (e) {
    e.preventDefault();
});
mainUploadBox.addEventListener('dragover', function (e) {
    e.preventDefault();
    mainUploadBox.style.opacity = '0.5';

});
mainUploadBox.addEventListener('dragleave', function (e) {
    e.preventDefault();
    mainUploadBox.style.opacity = '1';
});

mainUploadBox.addEventListener('drop', function (e) {
    e.preventDefault();

    // 유효성 체크
    let imgFiles = Array.from(e.dataTransfer.files).filter(f => f.type.startsWith('image/')); // type이 image/로 시작하는 파일들만 가져와서 배열로 구성
    if (imgFiles.length === 0) {
        alert("이미지 파일만 가능합니다.");
        return false;
    }

    // 이미지 파일 용량 제한
    imgFiles.forEach(file => {
        if (file.size > (1024 * 1024 * 5)) {
            alert("파일 하나당 최대 사이즈는 5MB이하여야 합니다.")
        }
    })

    const reader = new FileReader(); // FileReader

    for (const file of imgFiles) {
        reader.readAsDataURL(file); // reader에 file 정보를 넣어줌.
        reader.onload = function (e) { // preview 태그에 이미지가 업로드 되었을 때 동작 함수
            const preview = document.querySelector('#previewMain');
            const src = e.target.result;

            const item = new DOMParser().parseFromString(`
                <li class="item">
                    <input hidden type="text" class="file-name" name="fileName" th:value="${file.name}">
                    <img class="img" src="${src}" alt="">
                    <a class="btn btn-secondary delete-btn">삭제</a>
                </li>
            `, 'text/html').querySelector('.item');
            const deleteBtn = item.querySelector('.delete-btn');

            if(preview.querySelectorAll('.item').length !== 1) {
                preview.append(item);
            } else {
                alert("대표 이미지는 한 개만 등록 가능합니다.");
                return;
            }

            deleteBtn.onclick = function () {
                item.remove();
            }
        }
        mainFileName = file.name;
        formData.append("mainFiles", file);
    }
});




const addRoomBtn = roomForm.querySelector('.add_room_btn');

addRoomBtn.addEventListener('click', function(e) {
    e.preventDefault();
    formData.append("mainFileName", mainFileName);
    formData.append("fileNames", fileNameArray);
    formData.append("hotelName", roomForm['hotelName'].value);
    formData.append("kind", roomForm['kind'].value);
    formData.append("checkinTime", roomForm['checkinHour'].value + ":" + roomForm['checkinMinute'].value);
    formData.append("checkoutTime", roomForm['checkoutHour'].value + ":" + roomForm['checkoutMinute'].value);
    formData.append("fridayPrice", roomForm['fridayPrice'].value);
    formData.append("saturdayPrice", roomForm['saturdayPrice'].value);
    formData.append("weekdayPrice", roomForm['weekdayPrice'].value);
    formData.append("standardPeople", roomForm['standardPeople'].value);
    formData.append("maximumPeople", roomForm['maximumPeople'].value);
    formData.append("count", roomForm['count'].value);

    axios.post("/room/add", formData, {header : {'Content-Type': 'multipart/form-data'}})
        .then(res => {
            alert("객실 등록이 완료 되었습니다.");
        })
        .catch(err => {
            console.log(err);
        })
})