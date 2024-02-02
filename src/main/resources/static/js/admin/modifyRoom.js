const hotelList = document.querySelector('.hotel-list');
const roomList = document.querySelector('.room-list');
const mainImg = document.querySelector('.main-img');
const roomImg = document.querySelector('.room-img');
const roomForm = document.querySelector('.room-form');
const modifyRoom = document.querySelector('.modify-room-container');
const formData = new FormData();


function selectedHotel() {
    location.href = "/admin/modifyRoom?hotelName=" + hotelList.value;
}

function selectedRoom() {
    const hotelName = document.getElementById('hotelName').value;

    location.href = "/admin/modifyRoom?hotelName=" + hotelName + "&roomKind=" + roomList.value;
}

function confirmDuplication() {
    const hotelName = document.getElementById('hotelName').value;
    const kind = encodeURIComponent(roomForm['kind'].value);

    if(kind === "") {
        alert("객실 종류를 입력해 주세요.");
        return;
    }

    axios.get("/room/confirmKind?kind=" + kind + "&hotelName=" + hotelName)
        .then(res => {
            if (res.data === "FAILURE_DUPLICATED_KIND") {
                alert("이미 존재하는 객실 종류입니다. 다른 객실 종류를 입력해 주세요.");
            } else {
                alert("사용 가능한 객실 종류입니다.");
            }
        })
        .catch(err => {
            console.log(err);
        })
}




// 대표 이미지

let existingFileNameArray = [];
const existingFileNames = modifyRoom.querySelectorAll('.existing-file-name');
existingFileNames.forEach(fileName => {
    existingFileNameArray.push(fileName.value)
})

let fileNameArray = [];
let mainFileName = modifyRoom.querySelector('.existing-main-file-name').value;
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
            alert("파일 하나당 최대 사이즈는 5MB이하여야 합니다.");
            return false;
        }
    })

    const reader = new FileReader(); // FileReader
    const preview = document.querySelector('#mainPreview');

    for (const file of imgFiles) {
        if(preview.querySelectorAll('.item').length === 1) {
            alert("대표 이미지는 한 개만 등록 가능합니다.");
            return;
        }
        for (const fileName of fileNameArray) {
            if (fileName === file.name) {
                alert("동일한 이미지는 등록할 수 없습니다. 다른 이미지를 등록해 주세요.");
                return;
            }
        }

        for (const existingFileName of existingFileNameArray) {
            if (existingFileName === file.name) {
                alert("동일한 이미지는 등록할 수 없습니다. 다른 이미지를 등록해 주세요.");
                return
            }
        }

        fileNameArray.push(file.name);
        reader.readAsDataURL(file); // reader에 file 정보를 넣어줌.
        reader.onload = function (e) { // preview 태그에 이미지가 업로드 되었을 때 동작 함수
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
            }

            deleteBtn.onclick = function () {
                mainFileName = null;
                fileNameArray = fileNameArray.filter(name => name !== file.name);
                item.remove();
            }
        }
        mainFileName = file.name;
        formData.append("mainFiles", file);
    }
});




// 추가 이미지
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
            alert("파일 하나당 최대 사이즈는 5MB이하여야 합니다.");
            return false;
        }
    })

    const reader = new FileReader(); // FileReader

    for (const file of imgFiles) {
        if(file.name === mainFileName) {
            alert("이미 대표 이미지에 등록된 이미지입니다. 다른 이미지를 등록해 주세요.");
            return;
        }

        for (const fileName of fileNameArray) {
            if (fileName === file.name) {
                alert("동일한 이미지는 등록할 수 없습니다. 다른 이미지를 등록해 주세요.");
                return;
            }
        }

        for (const existingFileName of existingFileNameArray) {
            if (existingFileName === file.name) {
                alert("동일한 이미지는 등록할 수 없습니다. 다른 이미지를 등록해 주세요.");
                return
            }
        }

        fileNameArray.push(file.name);
        reader.readAsDataURL(file); // reader에 file 정보를 넣어줌.
        reader.onload = function (e) { // preview 태그에 이미지가 업로드 되었을 때 동작 함수
            const preview = document.querySelector('#roomPreview');
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





const items = modifyRoom.querySelectorAll('.item');

items.forEach(item => {
    const deleteBtn = item.querySelector('.delete-btn');
    deleteBtn.onclick = function () {
        existingFileNameArray = existingFileNameArray.filter(name => name !== item.querySelector('.existing-file-name').value);
        item.remove();
    }
})



const modifyRoomBtn = roomForm.querySelector('.modify_room_btn');

modifyRoomBtn.addEventListener('click', function(e) {
    e.preventDefault();

    const mainPreview = document.getElementById('mainPreview');
    const roomPreview = document.getElementById('roomPreview');

    let newExistingFileNameArray = [];
    const existingFileNames = modifyRoom.querySelectorAll('.existing-file-name');
    existingFileNames.forEach(fileName => {
        newExistingFileNameArray.push(fileName.value)
    })

    const kindRegex = new RegExp("^[^/]*$");
    const checkinHourRegex = new RegExp("^(0?[0-9]|1[0-9]|2[0-3])$");
    const checkoutHourRegex = new RegExp("^(0?[0-9]|1[0-9]|2[0-3])$");
    const checkinMinuteRegex = new RegExp("^[0-5]?[0-9]$");
    const checkoutMinuteRegex = new RegExp("^[0-5]?[0-9]$");
    const fridayPriceRegex = new RegExp("^\\d+$");
    const saturdayPriceRegex = new RegExp("^\\d+$");
    const weekdayPriceRegex = new RegExp("^\\d+$");
    const standardPeopleRegex = new RegExp("^\\d+$");
    const maximumPeopleRegex = new RegExp("^\\d+$");
    const countRegex = new RegExp("^\\d+$");


    if(roomForm['kind'].value === "") {
        alert("객실 종류를 입력해 주세요.");
        return;
    }

    if(!kindRegex.test(roomForm['kind'].value)) {
        alert("/ 은 사용할 수 없습니다. 객실 종류를 다시 입력해 주세요.");
        return;
    }

    if(roomForm['checkinHour'].value === "" || roomForm['checkinMinute'].value === "") {
        alert("체크인 시간을 입력해 주세요.");
        return;
    }

    if(!checkinHourRegex.test(roomForm['checkinHour'].value) || !checkinMinuteRegex.test(roomForm['checkinMinute'].value)) {
        alert("올바른 체크인 시간을 입력해 주세요.");
        return;
    }

    if(roomForm['checkoutHour'].value === "" || roomForm['checkoutMinute'].value === "") {
        alert("체크아웃 시간을 입력해 주세요.");
        return;
    }

    if(!checkoutHourRegex.test(roomForm['checkoutHour'].value) || !checkoutMinuteRegex.test(roomForm['checkoutMinute'].value)) {
        alert("올바른 체크아웃 시간을 입력해 주세요.");
        return;
    }

    if(roomForm['fridayPrice'].value === "") {
        alert("금요일 가격을 입력해 주세요.");
        return;
    }

    if(!fridayPriceRegex.test(roomForm['fridayPrice'].value)) {
        alert("올바른 금요일 가격을 입력해 주세요.");
        return;
    }

    if(roomForm['saturdayPrice'].value === "") {
        alert("토요일 가격을 입력해 주세요.");
        return;
    }

    if(!saturdayPriceRegex.test(roomForm['saturdayPrice'].value)) {
        alert("올바른 토요일 가격을 입력해 주세요.");
        return;
    }

    if(roomForm['weekdayPrice'].value === "") {
        alert("주중 가격을 입력해 주세요.");
        return;
    }

    if(!weekdayPriceRegex.test(roomForm['weekdayPrice'].value)) {
        alert("올바른 주중 가격을 입력해 주세요.");
        return;
    }

    if(roomForm['standardPeople'].value === "") {
        alert("기준 인원을 입력해 주세요.");
        return;
    }

    if(!standardPeopleRegex.test(roomForm['standardPeople'].value)) {
        alert("올바른 기준 인원을 입력해 주세요.");
        return;
    }

    if(roomForm['maximumPeople'].value === "") {
        alert("최대 인원을 입력해 주세요.");
        return;
    }

    if(!maximumPeopleRegex.test(roomForm['maximumPeople'].value)) {
        alert("올바른 최대 인원을 입력해 주세요.");
        return;
    }

    if(roomForm['count'].value === "") {
        alert("객실 개수를 입력해 주세요.");
        return;
    }

    if(!countRegex.test(roomForm['count'].value)) {
        alert("올바른 객실 개수를 입력해 주세요.");
        return;
    }

    if (mainPreview.querySelector('.item') == null) {
        alert("대표 이미지를 등록해 주세요.");
        return;
    }

    if (roomPreview.querySelector('.item') == null) {
        alert("한 개 이상의 객실 이미지를 등록해 주세요.");
        return;
    }

    let checkinHour = roomForm['checkinHour'].value;
    let checkinMinute = roomForm['checkinMinute'].value;
    let checkoutHour = roomForm['checkoutHour'].value;
    let checkoutMinute = roomForm['checkoutMinute'].value;

    if (roomForm['checkinHour'].value.length < 2) {
        checkinHour = '0' + roomForm['checkinHour'].value;
    }
    if (roomForm['checkinMinute'].value.length < 2) {
        checkinMinute = '0' + roomForm['checkinMinute'].value;

    }if (roomForm['checkoutHour'].value.length < 2) {
        checkoutHour = '0' + roomForm['checkoutHour'].value;

    }if (roomForm['checkoutMinute'].value.length < 2) {
        checkoutMinute = '0' + roomForm['checkoutMinute'].value;
    }

    formData.append("existingFileNames", newExistingFileNameArray);
    formData.append("existingKind", roomForm['existingKind'].value);
    formData.append("mainFileName", mainFileName);
    formData.append("fileNames", fileNameArray);
    formData.append("id", document.querySelector('.room-id').value);
    formData.append("hotelName", document.getElementById('hotelName').value);
    formData.append("kind", roomForm['kind'].value);
    formData.append("checkinTime", checkinHour + ":" + checkinMinute);
    formData.append("checkoutTime", checkoutHour + ":" + checkoutMinute);
    formData.append("fridayPrice", roomForm['fridayPrice'].value);
    formData.append("saturdayPrice", roomForm['saturdayPrice'].value);
    formData.append("weekdayPrice", roomForm['weekdayPrice'].value);
    formData.append("standardPeople", roomForm['standardPeople'].value);
    formData.append("maximumPeople", roomForm['maximumPeople'].value);
    formData.append("count", roomForm['count'].value);

    axios.put("/room/modify", formData, {header : {'Content-Type': 'multipart/form-data'}})
        .then(res => {
            alert("객실 수정이 완료 되었습니다.");
            location.href = "/admin/roomStatus";
        })
        .catch(err => {
            console.log(err);
        })
})


const deleteRoomBtn = document.querySelector('.delete_room_btn');

deleteRoomBtn.addEventListener('click', function(e) {
    e.preventDefault();

    if(confirm("정말로 객실을 삭제 하시겠습니까?")) {
        axios.delete("/room/delete?hotelName=" + document.getElementById('hotelName').value + "&kind=" + roomForm['kind'].value)
            .then(res => {
                console.log(res.data);
                if(res.data === "SUCCESS") {
                    alert("객실이 성공적으로 삭제 되었습니다.");
                    location.href = "/admin/roomStatus";
                }
            })
            .catch(err => {
                alert("알 수 없는 이유로 호텔을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.");
            })
    }

})