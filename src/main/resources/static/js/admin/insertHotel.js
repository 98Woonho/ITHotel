const imageForm = document.querySelector('.image-form');
const regionList = imageForm.querySelector('#regionList');

regionList.addEventListener('change', function(e) {
    if(e.target.value !== 'input') {
        imageForm['region'].value = e.target.value;
    } else {
        imageForm['region'].removeAttribute("disabled");
        imageForm['region'].value = "";
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
    for (var file of imgFiles) {
        reader.readAsDataURL(file); // reader에 file 정보를 넣어줌.
        reader.onload = function (e) { // preview 태그에 이미지가 업로드 되었을 때 동작 함수
            const preview = document.querySelector('#preview');
            const imgEl = document.createElement('img');
            imgEl.setAttribute('src', e.target.result);
            preview.appendChild(imgEl); // preview의 자식으로 imgEl 태그 생성
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



addHotelBtn = imageForm.querySelector('.add_hotel_btn');

addHotelBtn.addEventListener('click', function() {
    formData.append("hotelname", imageForm['hotelname'].value);
    formData.append("region", imageForm['region'].value);
    formData.append("addr1", imageForm['addr1'].value);
    formData.append("addr2", imageForm['addr2'].value);
    formData.append("zipcode", imageForm['zipcode'].value);
    formData.append("contactInfo", imageForm['contactInfo'].value);
    formData.append("hotelDetails", imageForm['hotelDetails'].value);

    axios.post("/hotel/add", formData, {header : {'Content-Type': 'multipart/form-data'}})
        .then(res => {
            alert("호텔 등록이 완료 되었습니다.");
        })
        .catch(err => {
            console.log(err);
        })
})