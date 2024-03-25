const isValid = function (){
    const form = document.joinform;
    const pwCheck = document.querySelector("#pwCheck").value;
    if(pwCheck === "true")
        form.submit();
}
const PwCheck = function (){
    const pw = document.joinform.password.value;
    const pw_msg = document.querySelector('#pw_msg');
    const pwCheck = document.querySelector('#pwCheck');
    let regexpPw = new RegExp("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$");

    if(!regexpPw.test(pw.trim())) {
        pw_msg.textContent = "비밀번호는 영문,숫자,특수문자가 1개 이상 포함된 8~15자여야 합니다.";
        pw_msg.style.color = 'orange';
        pwCheck.value = "false";
    }else{
        pw_msg.textContent = "사용가능한 비밀번호입니다.";
        pw_msg.style.color = 'green';
        pwCheck.value = "true";
    }
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
        }
    }).open();
}