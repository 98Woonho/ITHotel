const hotelList = document.querySelector('.hotel-list');
const hotelForm = document.querySelector('.hotel-form');
const mainImg = document.querySelector('.main-img');
const hotelImg = document.querySelector('.hotel-img');
const modifyHotel = document.querySelector('.modify-hotel-container');
const formData = new FormData();


function selectedHotel() {
    location.href = "/admin/modifyHotel?hotelName=" + hotelList.value;
}


const AddressSearch = () => {
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
            document.querySelector('#zipcode').value = data.zonecode;
            document.querySelector('#addr1').value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.querySelector('#addr2').focus();
            document.getElementById('region').value = addr.split(" ")[0];
        }
    }).open();
}


// 대표 이미지

let fileNameArray = [];
let mainFileName = modifyHotel.querySelector('.existing-main-file-name').value;
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
        return;
    }

    // 이미지 파일 용량 제한
    imgFiles.forEach(file => {
        if (file.size > (1024 * 1024 * 5)) {
            alert("파일 하나당 최대 사이즈는 5MB이하여야 합니다.");
        }
    })

    const reader = new FileReader(); // FileReader

    for (const file of imgFiles) {
        const preview = document.querySelector('#mainPreview');

        for (const fileName of fileNameArray) {
            if(preview.querySelectorAll('.item').length == 1) {
                alert("대표 이미지는 한 개만 등록 가능합니다.");
                return;
            } else if (fileName === file.name) {
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
                    <input hidden type="text" class="file-name" name="fileName" th:value="${file.name}">
                    <img class="img" src="${src}" alt="">
                    <a class="btn btn-secondary delete-btn">삭제</a>
                </li>
            `, 'text/html').querySelector('.item');
            const deleteBtn = item.querySelector('.delete-btn');

            if (preview.querySelectorAll('.item').length !== 1) {
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
const hotelUploadBox = hotelImg.querySelector('.hotel-upload-box');

hotelUploadBox.addEventListener('dragenter', function (e) {
    e.preventDefault();
});
hotelUploadBox.addEventListener('dragover', function (e) {
    e.preventDefault();
    hotelUploadBox.style.opacity = '0.5';

});
hotelUploadBox.addEventListener('dragleave', function (e) {
    e.preventDefault();
    hotelUploadBox.style.opacity = '1';
});

hotelUploadBox.addEventListener('drop', function (e) {
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
            return;
        }
    })

    let existingFileNameArray = [];
    const existingFileNames = modifyHotel.querySelectorAll('.existing-file-name');
    existingFileNames.forEach(fileName => {
        existingFileNameArray.push(fileName.value)
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
                return;
            }
        }

        fileNameArray.push(file.name);
        reader.readAsDataURL(file); // reader에 file 정보를 넣어줌.
        reader.onload = function (e) { // preview 태그에 이미지가 업로드 되었을 때 동작 함수
            const preview = document.querySelector('#hotelPreview');
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





const items = modifyHotel.querySelectorAll('.item');

items.forEach(item => {
    const deleteBtn = item.querySelector('.delete-btn');
    deleteBtn.onclick = function () {
        item.remove();
    }
})


const modifyHotelBtn = hotelForm.querySelector('.modify_hotel_btn');

modifyHotelBtn.addEventListener('click', function (e) {
    e.preventDefault();

    const mainPreview = document.getElementById('mainPreview');
    const hotelPreview = document.getElementById('hotelPreview');

    let newExistingFileNameArray = [];
    const existingFileNames = modifyHotel.querySelectorAll('.existing-file-name');
    existingFileNames.forEach(fileName => {
        newExistingFileNameArray.push(fileName.value)
    })

    const contactRegex = new RegExp("^\\d{3}-\\d{3,4}-\\d{4}$");

    if (hotelForm['zipcode'].value === "") {
        alert("주소 찾기를 통해 주소를 입력해 주세요.");
        return;
    }

    if (hotelForm['addr1'].value === "") {
        alert("주소 찾기를 통해 주소를 입력해 주세요.");
        return;
    }

    if (hotelForm['contactInfo'].value === "") {
        alert("연락처를 입력해 주세요.");
        return;
    }

    if (!contactRegex.test(hotelForm['contactInfo'].value)) {
        alert("올바른 연락처를 입력해 주세요.");
        return;
    }

    if (hotelForm['hotelDetails'].value === "") {
        alert("호텔 소개를 입력해 주세요.");
        return;
    }

    if (mainPreview.querySelector('.item') == null) {
        alert("대표 이미지를 등록해 주세요.");
        return;
    }

    if (hotelPreview.querySelector('.item') == null) {
        alert("한 개 이상의 객실 이미지를 등록해 주세요.");
        return;
    }

    formData.append("existingFileNames", newExistingFileNameArray);
    formData.append("mainFileName", mainFileName);
    formData.append("fileNames", fileNameArray);
    formData.append("hotelName", hotelForm['hotelName'].value);
    formData.append("region", hotelForm['region'].value);
    formData.append("addr1", hotelForm['addr1'].value);
    formData.append("addr2", hotelForm['addr2'].value);
    formData.append("zipcode", hotelForm['zipcode'].value);
    formData.append("contactInfo", hotelForm['contactInfo'].value);
    formData.append("hotelDetails", hotelForm['hotelDetails'].value);

    axios.put("/hotel/modify", formData, {header: {'Content-Type': 'multipart/form-data'}})
        .then(res => {
            alert("호텔 수정이 완료 되었습니다.");
            location.href = "/admin/hotelStatus";
        })
        .catch(err => {
            console.log(err);
        })
})

const deleteHotelBtn = document.querySelector('.delete_hotel_btn');

deleteHotelBtn.addEventListener('click', function (e) {
    e.preventDefault();

    if (confirm("호텔을 삭제 하면 객실 정보도 함께 삭제 됩니다. 정말로 삭제 하시겠습니까?")) {
        axios.delete("/hotel/delete?hotelName=" + hotelForm['hotelName'].value)
            .then(res => {
                console.log(res.data);
                if (res.data === "SUCCESS") {
                    alert("호텔이 성공적으로 삭제 되었습니다.");
                    location.href = "/admin/hotelStatus";
                }
            })
            .catch(err => {
                alert("알 수 없는 이유로 호텔을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.");
            })
    }

})