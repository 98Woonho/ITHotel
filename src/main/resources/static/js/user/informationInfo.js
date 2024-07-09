const userAuth = function (){
    const pw = document.querySelector(".password").value;
    const pw_msg = document.querySelector(".auth_msg");
    if(pw === ""){
        pw_msg.innerHTML = "비밀번호를 입력하세요";
        pw_msg.style.color = 'orange';
        return ;
    }
    axios.post("/user/infoAuth/" + pw)
        .then( res => {;
            if(!res.data.success){
                pw_msg.innerHTML = res.data.massage;
                pw_msg.style.color = 'orange';
            }
            else{
                window.location.reload();
            }
        })
        .catch( err => { console.log(err); });
}

const IdCheck = function(){
    const user_id = document.updateinfo.userid.value;
    const id_msg = document.querySelector('#id_msg');
    let regexpId = new RegExp("^[A-Za-z0-9]{6,16}$");
    if(regexpId.test(user_id.trim())) {
        axios.get('/user/ConfirmId?id=' + user_id)
            .then(res => {
                console.log(res);
                if (res.data.success) {
                    id_msg.style.color = 'green';
                    id_msg.innerHTML = res.data.message;
                } else {
                    id_msg.style.color = 'orange';
                    id_msg.innerHTML = res.data.message;
                }
            })
            .catch(err => {
                console.log(err);
            });
    } else
        id_msg.textContent = "아이디는 소문자와 숫자로만 이루어진 6~16자여야 합니다.";
}

const EmailCheck = function (){
    const email = document.querySelector('#email').value;
    let regexEmail = new RegExp("^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$");
    const email_msg = document.querySelector('#email_msg');
    let success;
    axios.get('/user/ConfirmEmail/' + email)
        .then(res => {
            console.log(res);
            if(!res.data.success) {
                email_msg.innerHTML = res.data.message;
                email_msg.style.color = 'orange';
                success = false;
            }
        })
        .catch(err =>{ console.log(err); });
    if(!regexEmail.test(email.trim())) {
        email_msg.textContent = "이메일 형식으로 작성해야 합니다.";
        email_msg.style.color = 'orange';
        success = false;
    }else {
        success = true;
    }
    return success;
}

const SendEmail = function(){
    const email = document.querySelector('#email').value;
    const email_msg = document.querySelector('#email_msg');
    if(EmailCheck()){
        email_msg.textContent = "이메일로 인증코드가 전송되었습니다.";
        email_msg.style.color = 'green';
        axios.get('/user/sendEmail/' + email)
            .then(res => {
                console.log(res);
                document.querySelector('#auth_code').style.display = 'block';
            })
            .catch( err => {console.log(err);} )
    }
}

const ConfirmEmail = function(){
    const email = document.querySelector('#email').value;
    const code = document.querySelector('#code').value;
    axios.get('/user/confirmCode?email='+email+"&code="+code)
        .then(res => {
            console.log(res);
            if(res.data.success){
                document.querySelector('#code_msg').style.color = 'green';
                document.querySelector('#code_msg').innerHTML = res.data.message;
            } else {
                document.querySelector('#code_msg').style.color = 'orange';
                document.querySelector('#code_msg').innerHTML = res.data.message;
            }
        })
        .catch( err => {console.log(err);} )
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