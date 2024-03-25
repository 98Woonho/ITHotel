const registerRoomForm = document.getElementById('registerRoomForm');
const formData = new FormData();

registerRoomForm['kind'].addEventListener('input', function() {
    if(registerRoomForm['kind'].classList.contains("confirmed")) {
        registerRoomForm['kind'].classList.remove("confirmed");
    }
})

registerRoomForm['confirmRoomKindDuplicationBtn'].addEventListener('click', function() {
    if(registerRoomForm['kind'].value === "") {
        alert("객실 종류를 입력해 주세요.");
        return;
    }

    axios.get("/room/confirmKind?kind=" + registerRoomForm['kind'].value + "&hotelName=" + registerRoomForm['hotelName'].value)
        .then(res => {
            if (res.data === "FAILURE_DUPLICATED_KIND") {
                alert("이미 존재하는 객실 종류입니다. 다른 객실 종류를 입력해 주세요.");
            } else {
                alert("사용 가능한 객실 종류입니다.");
                registerRoomForm['kind'].classList.add("confirmed");
            }
        })
        .catch(err => {
            console.log(err);
        })
})


// 대표 이미지

let fileNameArray = [];
let mainFileName;
const mainUploadBox = document.getElementById('mainUploadBox');



mainUploadBox.addEventListener('dragover', function (e) {
    e.preventDefault();
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
    const mainPreview = document.getElementById('mainPreview');

    for (const file of imgFiles) {
        if(mainPreview.querySelectorAll('.item').length === 1) {
            alert("대표 이미지는 한 개만 등록 가능합니다.");
            return;
        }
        for (const fileName of fileNameArray) {
            if (fileName === file.name) {
                alert("동일한 이미지는 등록할 수 없습니다. 다른 이미지를 등록해 주세요.");
                return;
            }
        }
        fileNameArray.push(file.name);
        reader.readAsDataURL(file); // reader에 file 정보를 넣어줌.
        reader.onload = function (e) { // preview 태그에 이미지가 업로드 되었을 때 동작 함수
            const src = e.target.result;

            const item = new DOMParser().parseFromString(`
                <li class="item">
                    <input hidden type="text" class="file-name" name="mainFileName" th:value="${file.name}">
                    <img src="${src}" alt="">
                    <button type="button" class="btn btn-secondary ms-2 text-nowrap delete-btn">삭제</button>
                </li>
            `, 'text/html').querySelector('.item');
            const deleteBtn = item.querySelector('.delete-btn');

            if(mainPreview.querySelectorAll('.item').length !== 1) {
                mainPreview.append(item);
            }

            deleteBtn.addEventListener('click', function() {
                mainFileName = null;
                fileNameArray = fileNameArray.filter(name => name !== file.name);
                item.remove();
            })
        }
        mainFileName = file.name;
        formData.append("mainFiles", file);
    }
});




// 추가 이미지

const additionalUploadBox = document.getElementById('additionalUploadBox');

additionalUploadBox.addEventListener('dragover', function (e) {
    e.preventDefault();
    additionalUploadBox.style.opacity = '0.5';
});

additionalUploadBox.addEventListener('drop', function (e) {
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
        fileNameArray.push(file.name);
        reader.readAsDataURL(file); // reader에 file 정보를 넣어줌.
        reader.onload = function (e) { // preview 태그에 이미지가 업로드 되었을 때 동작 함수
            const additionalPreview = document.getElementById('additionalPreview');
            const src = e.target.result;

            const item = new DOMParser().parseFromString(`
                <li class="item">
                    <input hidden type="text" class="file-name" name="fileName" th:value="${file.name}">
                    <img src="${src}" alt="">
                    <button type="button" class="btn btn-secondary ms-2 text-nowrap delete-btn">삭제</button>
                </li>
            `, 'text/html').querySelector('.item');
            const deleteBtn = item.querySelector('.delete-btn');

            additionalPreview.append(item);
            additionalPreview.scrollLeft = additionalPreview.scrollWidth; // 파일이 추가 되면 스크롤을 오른쪽 끝으로 알아서 당겨줌.

            deleteBtn.onclick = function () {
                fileNameArray = fileNameArray.filter(name => name !== file.name);
                item.remove();
            }
        }
        formData.append("files", file);
    }
});

registerRoomForm.onsubmit = function(e) {
    e.preventDefault();

    const mainPreview = document.getElementById('mainPreview');
    const additionalPreview = document.getElementById('additionalPreview');
    
    const kindRegex = new RegExp("^[^\/]+$");
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


    if(registerRoomForm['kind'].value === "") {
        alert("객실 종류를 입력해 주세요.");
        return;
    }

    if(!kindRegex.test(registerRoomForm['kind'].value)) {
        alert("특수문자 / 은 사용할 수 없습니다. 객실 종류를 다시 한 번 확인해 주세요.");
        return;
    }

    if(registerRoomForm['checkinHour'].value === "" || registerRoomForm['checkinMinute'].value === "") {
        alert("체크인 시간을 입력해 주세요.");
        return;
    }

    if(!checkinHourRegex.test(registerRoomForm['checkinHour'].value) || !checkinMinuteRegex.test(registerRoomForm['checkinMinute'].value)) {
        alert("올바른 체크인 시간을 입력해 주세요.");
        return;
    }

    if(registerRoomForm['checkoutHour'].value === "" || registerRoomForm['checkoutMinute'].value === "") {
        alert("체크아웃 시간을 입력해 주세요.");
        return;
    }

    if(!checkoutHourRegex.test(registerRoomForm['checkoutHour'].value) || !checkoutMinuteRegex.test(registerRoomForm['checkoutMinute'].value)) {
        alert("올바른 체크아웃 시간을 입력해 주세요.");
        return;
    }

    if(registerRoomForm['fridayPrice'].value === "") {
        alert("금요일 가격을 입력해 주세요.");
        return;
    }

    if(!fridayPriceRegex.test(registerRoomForm['fridayPrice'].value)) {
        alert("올바른 금요일 가격을 입력해 주세요.");
        return;
    }

    if(registerRoomForm['saturdayPrice'].value === "") {
        alert("토요일 가격을 입력해 주세요.");
        return;
    }

    if(!saturdayPriceRegex.test(registerRoomForm['saturdayPrice'].value)) {
        alert("올바른 토요일 가격을 입력해 주세요.");
        return;
    }

    if(registerRoomForm['weekdayPrice'].value === "") {
        alert("주중 가격을 입력해 주세요.");
        return;
    }

    if(!weekdayPriceRegex.test(registerRoomForm['weekdayPrice'].value)) {
        alert("올바른 주중 가격을 입력해 주세요.");
        return;
    }

    if(registerRoomForm['standardPeople'].value === "") {
        alert("기준 인원을 입력해 주세요.");
        return;
    }

    if(!standardPeopleRegex.test(registerRoomForm['standardPeople'].value)) {
        alert("올바른 기준 인원을 입력해 주세요.");
        return;
    }

    if(registerRoomForm['maximumPeople'].value === "") {
        alert("최대 인원을 입력해 주세요.");
        return;
    }

    if(!maximumPeopleRegex.test(registerRoomForm['maximumPeople'].value)) {
        alert("올바른 최대 인원을 입력해 주세요.");
        return;
    }

    if(registerRoomForm['count'].value === "") {
        alert("객실 개수를 입력해 주세요.");
        return;
    }

    if(!countRegex.test(registerRoomForm['count'].value)) {
        alert("올바른 객실 개수를 입력해 주세요.");
        return;
    }

    if (mainPreview.querySelector('.item') == null) {
        alert("대표 이미지를 등록해 주세요.");
        return;
    }

    if (additionalPreview.querySelector('.item') == null) {
        alert("한 개 이상의 객실 이미지를 등록해 주세요.");
        return;
    }

    let checkinHour = registerRoomForm['checkinHour'].value;
    let checkinMinute = registerRoomForm['checkinMinute'].value;
    let checkoutHour = registerRoomForm['checkoutHour'].value;
    let checkoutMinute = registerRoomForm['checkoutMinute'].value;

    if (registerRoomForm['checkinHour'].value.length < 2) {
        checkinHour = '0' + registerRoomForm['checkinHour'].value;
    }
    if (registerRoomForm['checkinMinute'].value.length < 2) {
        checkinMinute = '0' + registerRoomForm['checkinMinute'].value;

    }if (registerRoomForm['checkoutHour'].value.length < 2) {
        checkoutHour = '0' + registerRoomForm['checkoutHour'].value;

    }if (registerRoomForm['checkoutMinute'].value.length < 2) {
        checkoutMinute = '0' + registerRoomForm['checkoutMinute'].value;
    }

    if (!registerRoomForm['kind'].classList.contains("confirmed")) {
        alert("객실 종류 중복 확인을 진행해주세요.");
        return;
    }

    formData.append("mainFileName", mainFileName);
    formData.append("fileNames", fileNameArray);
    formData.append("hotelName", registerRoomForm['hotelName'].value);
    formData.append("kind", registerRoomForm['kind'].value);
    formData.append("checkinTime", checkinHour + ":" + checkinMinute);
    formData.append("checkoutTime", checkoutHour + ":" + checkoutMinute);
    formData.append("fridayPrice", registerRoomForm['fridayPrice'].value);
    formData.append("saturdayPrice", registerRoomForm['saturdayPrice'].value);
    formData.append("weekdayPrice", registerRoomForm['weekdayPrice'].value);
    formData.append("standardPeople", registerRoomForm['standardPeople'].value);
    formData.append("maximumPeople", registerRoomForm['maximumPeople'].value);
    formData.append("count", registerRoomForm['count'].value);

    axios.post("/room/add", formData, {header : {'Content-Type': 'multipart/form-data'}})
        .then(res => {
            alert("객실 등록이 완료 되었습니다.");
            location.href = "/admin/roomStatus";
        })
        .catch(err => {
            console.log(err);
        })
}