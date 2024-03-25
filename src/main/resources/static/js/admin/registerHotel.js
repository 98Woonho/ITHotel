const registerHotelForm = document.getElementById('registerHotelForm');
const formData = new FormData();

registerHotelForm['searchAddressBtn'].addEventListener('click', function() {
    new daum.Postcode({
        oncomplete: function (data) {
            let addr = '';
            if (data.userSelectedType === 'R') {
                // 도로명 주소
                addr = data.roadAddress;
            } else {
                // 지번 주소
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            registerHotelForm['zipcode'].value = data.zonecode;
            registerHotelForm['addr1'].value = addr;
            // 커서를 상세주소 필드로 이동한다.
            registerHotelForm['addr2'].focus();
            registerHotelForm['region'].value = addr.split(" ")[0];
        }
    }).open();
})
   
registerHotelForm['hotelName'].addEventListener('input', function() {
    if(registerHotelForm['hotelName'].classList.contains("confirmed")) {
        registerHotelForm['hotelName'].classList.remove("confirmed");
    }
})

registerHotelForm['confirmHotelNameDuplicationBtn'].addEventListener('click', function() {
    if(registerHotelForm['hotelName'].value === "") {
        alert("호텔 이름을 입력해 주세요.");
        return;
    }

    axios.get("/hotel/confirmHotelName?hotelName=" + registerHotelForm['hotelName'].value)
        .then(res => {
            if (res.data === "FAILURE_DUPLICATED_HOTEL_NAME") {
                alert("이미 존재하는 호텔 이름입니다. 다른 호텔 이름을 입력해 주세요.");

            } else {
                alert("사용 가능한 호텔 이름입니다.");
                registerHotelForm['hotelName'].classList.add("confirmed");
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
    mainUploadBox.style.opacity = '0.5';
});

mainUploadBox.addEventListener('drop', function (e) {
    e.preventDefault();

    // 유효성 체크
    const imgFiles = Array.from(e.dataTransfer.files).filter(f => f.type.startsWith('image/')); // type이 image/로 시작하는 파일들만 가져와서 배열로 구성
    if (imgFiles.length === 0) {
        alert("이미지 파일만 가능합니다.");
        return;
    }

    // 이미지 파일 용량 제한
    imgFiles.forEach(file => {
        if (file.size > (1024 * 1024 * 5)) {
            alert("파일 하나당 최대 사이즈는 5MB이하여야 합니다.");
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
        return;
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
                    <input hidden type="text" class="file-name" id="additionalFileName" th:value="${file.name}">
                    <img class="img" src="${src}" alt="">
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

registerHotelForm.onsubmit = function (e) {
    e.preventDefault();

    const mainPreview = document.getElementById('mainPreview');
    const additionalPreview = document.getElementById('additionalPreview');


    const hotelNameRegex = new RegExp("^[a-zA-Z0-9\uAC00-\uD7A3\\s]+$");
    const contactRegex = new RegExp("^\\d{2,3}-\\d{3,4}-\\d{4}$");


    if (registerHotelForm['hotelName'].value === "") {
        alert("호텔 이름을 입력해 주세요.");
        return;
    }

    if (!hotelNameRegex.test(registerHotelForm['hotelName'].value)) {
        alert("올바른 호텔이름을 입력해 주세요.");
        return;
    }

    if (registerHotelForm['zipcode'].value === "") {
        alert("주소 찾기를 통해 주소를 입력해 주세요.");
        return;
    }

    if (registerHotelForm['addr1'].value === "") {
        alert("주소 찾기를 통해 주소를 입력해 주세요.");
        return;
    }

    if (registerHotelForm['contactInfo'].value === "") {
        alert("연락처를 입력해 주세요.");
        return;
    }

    if (!contactRegex.test(registerHotelForm['contactInfo'].value)) {
        alert("올바른 연락처를 입력해 주세요.");
        return;
    }

    if (registerHotelForm['hotelDetails'].value === "") {
        alert("호텔 소개를 입력해 주세요.");
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

    if (!registerHotelForm['hotelName'].classList.contains("confirmed")) {
        alert("호텔 이름 중복 확인을 진행해주세요.");
        return;
    }

    formData.append("mainFileName", mainFileName);
    formData.append("fileNames", fileNameArray);
    formData.append("hotelName", registerHotelForm['hotelName'].value);
    formData.append("region", registerHotelForm['region'].value);
    formData.append("addr1", registerHotelForm['addr1'].value);
    formData.append("addr2", registerHotelForm['addr2'].value);
    formData.append("zipcode", registerHotelForm['zipcode'].value);
    formData.append("contactInfo", registerHotelForm['contactInfo'].value);
    formData.append("hotelDetails", registerHotelForm['hotelDetails'].value);

    axios.post("/hotel/add", formData, {header: {'Content-Type': 'multipart/form-data'}})
        .then(res => {
            alert("호텔 등록이 완료 되었습니다.");
            location.href = "/admin/hotelStatus";
        })
        .catch(err => {
            console.log(err);
        })
}