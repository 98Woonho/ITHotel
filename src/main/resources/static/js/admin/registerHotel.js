const hotelForm = document.querySelector('.hotel-form');
const regionList = hotelForm.querySelector('#regionList');

regionList.addEventListener('change', function(e) {
    if(e.target.value !== 'input') {
        hotelForm['region'].value = e.target.value;
    } else {
        hotelForm['region'].removeAttribute("disabled");
        hotelForm['region'].value = "";
    }
})

const uploadBox = document.querySelector('.upload-box');
const formData = new FormData();

uploadBox.addEventListener('dragenter', function (e) {
    e.preventDefault();
});

uploadBox.addEventListener('dragover', function (e) {
    e.preventDefault();
    uploadBox.style.opacity = '0.5';
});

uploadBox.addEventListener('dragleave', function (e) {
    e.preventDefault();
    uploadBox.style.opacity = '1';
});

let fileNameArray = [];

uploadBox.addEventListener('drop', function (e) {
    e.preventDefault();

    // 유효성 체크
    const imgFiles = Array.from(e.dataTransfer.files).filter(f => f.type.startsWith('image/')); // type이 image/로 시작하는 파일들만 가져와서 배열로 구성
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
            const preview = document.querySelector('#preview');
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
        }
    }).open();
}



const addHotelBtn = hotelForm.querySelector('.add_hotel_btn');

addHotelBtn.addEventListener('click', function(e) {
    e.preventDefault();

    const hotelnameRegex = new RegExp("^[a-zA-Z0-9\uAC00-\uD7A3\\s]+$");
    const regionRegex = new RegExp("^[\uAC00-\uD7A3]+$");
    const contactRegex = new RegExp("^\\d{3}-\\d{3,4}-\\d{4}$");

    if(hotelForm['hotelname'].value === "") {
        alert("호텔 이름을 입력해 주세요.");
        return;
    }

    if(!hotelnameRegex.test(hotelForm['hotelname'].value)) {
        alert("올바른 호텔이름을 입력해 주세요.");
        return;
    }

    if(hotelForm['region'].value === "") {
        alert("지역을 등록해 주세요.");
        return;
    }

    if(!regionRegex.test(hotelForm['region'].value)) {
        alert("올바른 지역을 입력해 주세요.");
        return;
    }

    if(hotelForm['zipcode'].value === "") {
        alert("주소 찾기를 통해 주소를 입력해 주세요.");
        return;
    }

    if(hotelForm['addr1'].value === "") {
        alert("주소 찾기를 통해 주소를 입력해 주세요.");
        return;
    }

    if(hotelForm['contactInfo'].value === "") {
        alert("연락처를 입력해 주세요.");
        return;
    }

    if(!contactRegex.test(hotelForm['contactInfo'].value)) {
        alert("올바른 연락처를 입력해 주세요.");
        return;
    }

    if(hotelForm['hotelDetails'].value === "") {
        alert("호텔 소개를 입력해 주세요.");
        return;
    }
    formData.append("fileNames", fileNameArray);
    formData.append("hotelname", hotelForm['hotelname'].value);
    formData.append("region", hotelForm['region'].value);
    formData.append("addr1", hotelForm['addr1'].value);
    formData.append("addr2", hotelForm['addr2'].value);
    formData.append("zipcode", hotelForm['zipcode'].value);
    formData.append("contactInfo", hotelForm['contactInfo'].value);
    formData.append("hotelDetails", hotelForm['hotelDetails'].value);

    axios.post("/hotel/add", formData, {header : {'Content-Type': 'multipart/form-data'}})
        .then(res => {
            alert("호텔 등록이 완료 되었습니다.");
        })
        .catch(err => {
            console.log(err);
        })
})