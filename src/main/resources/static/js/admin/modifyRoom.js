const modifyRoomForm = document.getElementById('modifyRoomForm');
const formData = new FormData();

modifyRoomForm['kind'].addEventListener('input', function() {
    if(modifyRoomForm['kind'].classList.contains("confirmed")) {
        modifyRoomForm['kind'].classList.remove("confirmed");
    }
})

modifyRoomForm['confirmRoomKindDuplicationBtn'].addEventListener('click', function() {
    if(modifyRoomForm['kind'].value === "") {
        alert("객실 종류를 입력해 주세요.");
        return;
    }

    if(modifyRoomForm['kind'].value === modifyRoomForm['currentKind'].value) {
        alert("기존 객실 종류와 동일합니다.");
        modifyRoomForm['kind'].classList.add("confirmed");
        return;
    }

    axios.get("/room/confirmKind?kind=" + modifyRoomForm['kind'].value + "&hotelName=" + modifyRoomForm['hotelName'].value)
        .then(res => {
            if (res.data === "FAILURE_DUPLICATED_KIND") {
                alert("이미 존재하는 객실 종류입니다. 다른 객실 종류를 입력해 주세요.");
            } else {
                alert("사용 가능한 객실 종류입니다.");
                modifyRoomForm['kind'].classList.add("confirmed");
            }
        })
        .catch(err => {
            console.log(err);
        })
})






// 대표 이미지

let existingFileNameArray = [];
const existingFileNames = document.querySelectorAll('.existing-file-name');
existingFileNames.forEach(fileName => {
    existingFileNameArray.push(fileName.value)
})

let fileNameArray = [];
let mainFileName = document.getElementById('existingMainFileName').value;
const mainUploadBox = document.getElementById('mainUploadBox');

mainUploadBox.addEventListener('dragover', function (e) {
    e.preventDefault();
    mainUploadBox.style.opacity = '0.5';
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
                    <input hidden type="text" class="file-name" id="mainFileName" th:value="${file.name}">
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

        for (const existingFileName of existingFileNameArray) {
            if (existingFileName === file.name) {
                alert("동일한 이미지는 등록할 수 없습니다. 다른 이미지를 등록해 주세요.");
                return
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
                    <img class="img" src="${src}" alt="">
                    <button type="button" class="btn btn-secondary ms-2 text-nowrap delete-btn">삭제</button>
                </li>
            `, 'text/html').querySelector('.item');
            const deleteBtn = item.querySelector('.delete-btn');

            additionalPreview.append(item);
            additionalPreview.scrollLeft = additionalPreview.scrollWidth; // 파일이 추가 되면 스크롤을 오른쪽 끝으로 알아서 당겨줌.

            deleteBtn.addEventListener('click', function() {
                fileNameArray = fileNameArray.filter(name => name !== file.name);
                item.remove();
            })
        }
        formData.append("files", file);
    }
});

const items = document.querySelectorAll('.item');

items.forEach(item => {
    const existingFileName = item.querySelector('.existing-file-name');
    const deleteBtn = item.querySelector('.delete-btn');
    deleteBtn.addEventListener('click', function() {
        existingFileNameArray = existingFileNameArray.filter(name => name !== existingFileName.value);
        item.remove();
    })
})


modifyRoomForm.onsubmit = function(e) {
    e.preventDefault();

    const action = e.submitter.value;

    if (action === "modify") {
        const mainPreview = document.getElementById('mainPreview');
        const additionalPreview = document.getElementById('additionalPreview');

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


        if(modifyRoomForm['kind'].value === "") {
            alert("객실 종류를 입력해 주세요.");
            return;
        }

        if(!kindRegex.test(modifyRoomForm['kind'].value)) {
            alert("/ 은 사용할 수 없습니다. 객실 종류를 다시 입력해 주세요.");
            return;
        }

        if(modifyRoomForm['checkinHour'].value === "" || modifyRoomForm['checkinMinute'].value === "") {
            alert("체크인 시간을 입력해 주세요.");
            return;
        }

        if(!checkinHourRegex.test(modifyRoomForm['checkinHour'].value) || !checkinMinuteRegex.test(modifyRoomForm['checkinMinute'].value)) {
            alert("올바른 체크인 시간을 입력해 주세요.");
            return;
        }

        if(modifyRoomForm['checkoutHour'].value === "" || modifyRoomForm['checkoutMinute'].value === "") {
            alert("체크아웃 시간을 입력해 주세요.");
            return;
        }

        if(!checkoutHourRegex.test(modifyRoomForm['checkoutHour'].value) || !checkoutMinuteRegex.test(modifyRoomForm['checkoutMinute'].value)) {
            alert("올바른 체크아웃 시간을 입력해 주세요.");
            return;
        }

        if(modifyRoomForm['fridayPrice'].value === "") {
            alert("금요일 가격을 입력해 주세요.");
            return;
        }

        if(!fridayPriceRegex.test(modifyRoomForm['fridayPrice'].value)) {
            alert("올바른 금요일 가격을 입력해 주세요.");
            return;
        }

        if(modifyRoomForm['saturdayPrice'].value === "") {
            alert("토요일 가격을 입력해 주세요.");
            return;
        }

        if(!saturdayPriceRegex.test(modifyRoomForm['saturdayPrice'].value)) {
            alert("올바른 토요일 가격을 입력해 주세요.");
            return;
        }

        if(modifyRoomForm['weekdayPrice'].value === "") {
            alert("주중 가격을 입력해 주세요.");
            return;
        }

        if(!weekdayPriceRegex.test(modifyRoomForm['weekdayPrice'].value)) {
            alert("올바른 주중 가격을 입력해 주세요.");
            return;
        }

        if(modifyRoomForm['standardPeople'].value === "") {
            alert("기준 인원을 입력해 주세요.");
            return;
        }

        if(!standardPeopleRegex.test(modifyRoomForm['standardPeople'].value)) {
            alert("올바른 기준 인원을 입력해 주세요.");
            return;
        }

        if(modifyRoomForm['maximumPeople'].value === "") {
            alert("최대 인원을 입력해 주세요.");
            return;
        }

        if(!maximumPeopleRegex.test(modifyRoomForm['maximumPeople'].value)) {
            alert("올바른 최대 인원을 입력해 주세요.");
            return;
        }

        if(modifyRoomForm['count'].value === "") {
            alert("객실 개수를 입력해 주세요.");
            return;
        }

        if(!countRegex.test(modifyRoomForm['count'].value)) {
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

        let checkinHour = modifyRoomForm['checkinHour'].value;
        let checkinMinute = modifyRoomForm['checkinMinute'].value;
        let checkoutHour = modifyRoomForm['checkoutHour'].value;
        let checkoutMinute = modifyRoomForm['checkoutMinute'].value;

        if (modifyRoomForm['checkinHour'].value.length < 2) {
            checkinHour = '0' + modifyRoomForm['checkinHour'].value;
        }
        if (modifyRoomForm['checkinMinute'].value.length < 2) {
            checkinMinute = '0' + modifyRoomForm['checkinMinute'].value;

        }if (modifyRoomForm['checkoutHour'].value.length < 2) {
            checkoutHour = '0' + modifyRoomForm['checkoutHour'].value;

        }if (modifyRoomForm['checkoutMinute'].value.length < 2) {
            checkoutMinute = '0' + modifyRoomForm['checkoutMinute'].value;
        }

        if (!modifyRoomForm['kind'].classList.contains("confirmed")) {
            alert("객실 종류 중복 확인을 진행해주세요.");
            return;
        }

        formData.append("existingFileNames", existingFileNameArray);
        formData.append("currentKind", modifyRoomForm['currentKind'].value);
        formData.append("mainFileName", mainFileName);
        formData.append("fileNames", fileNameArray);
        formData.append("id", modifyRoomForm['id'].value);
        formData.append("hotelName", modifyRoomForm['hotelName'].value);
        formData.append("kind", modifyRoomForm['kind'].value);
        formData.append("checkinTime", checkinHour + ":" + checkinMinute);
        formData.append("checkoutTime", checkoutHour + ":" + checkoutMinute);
        formData.append("fridayPrice", modifyRoomForm['fridayPrice'].value);
        formData.append("saturdayPrice", modifyRoomForm['saturdayPrice'].value);
        formData.append("weekdayPrice", modifyRoomForm['weekdayPrice'].value);
        formData.append("standardPeople", modifyRoomForm['standardPeople'].value);
        formData.append("maximumPeople", modifyRoomForm['maximumPeople'].value);
        formData.append("count", modifyRoomForm['count'].value);

        axios.put("/room/modify", formData, {header : {'Content-Type': 'multipart/form-data'}})
            .then(res => {
                alert("객실 수정이 완료 되었습니다.");
                location.href = "/admin/roomStatus?hotelName=" + modifyRoomForm['hotelName'].value;
            })
            .catch(err => {
                console.log(err);
            })
    }

    else {
        if(confirm("정말로 객실을 삭제 하시겠습니까?")) {
            axios.delete("/room/delete?hotelName=" + modifyRoomForm['hotelName'].value + "&kind=" + modifyRoomForm['kind'].value)
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
    }
}