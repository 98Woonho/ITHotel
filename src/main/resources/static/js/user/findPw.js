const isValid = function(){
    const id = document.querySelector('#id').value;
    const name = document.querySelector('#name').value;
    const email = document.querySelector('#email').value;
    const code = document.querySelector('#emailCode').value;

    if (id === "") {
        alert('아이디를 입력하세요');
        return ;
    }
    if (name === "") {
        alert('이름을 입력하세요');
        return ;
    }
    if (email === "") {
        alert('이메일을 입력하세요');
        return;
    }

    axios.get('/user/confirmEmail?email='+email+"&code="+code)
        .then( res => {
            if(code === ""){
                alert('이메일 인증이 필요합니다.');
                return ;
            }

            axios.get('/user/sendPw?confirm=' + res.data.success + '&email=' + email)
                .then(res => {
                    alert(res.data.message);
                })
                .catch(err => {
                    console.log(err);
                })
        })
        .catch( err => { console.log(err); })
}
const EmailCheck = () => {
    const name = document.querySelector('#name').value;
    const email = document.querySelector('#email').value;
    let regexEmail = new RegExp("^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$");
    const email_msg = document.querySelector('#email_msg');
    axios.get('/user/existPw?name=' + name + '&email=' + email)
        .then(res => {
            console.log(res);
            if(res.data.success){
                email_msg.style.color = 'green';
                email_msg.innerHTML = res.data.message;
                return true;
            } else {
                email_msg.style.color = 'orange';
                email_msg.innerHTML = res.data.message;
                return false;
            }
        })
        .catch( err => {console.log(err);})
    if(!regexEmail.test(email.trim())){
        email_msg.textContent = "이메일 형식으로 작성해야 합니다.";
        email_msg.style.color = 'orange';
        return false;
    }
}

const SendEmail = function(){
    const email = document.querySelector('#email').value;
    const email_msg = document.querySelector('#email_msg');
    if(EmailCheck){
        email_msg.style.color = 'green';
        axios.get('/user/sendEmail/' + email)
            .then(res => {
                alert('입력하신 이메일로 인증코드를 전송 하였습니다. 확인 후 인증코드를 입력해 주세요.');
                document.querySelector('.code').classList.add('visible');
            })
            .catch( err => {console.log(err);} )
    }
}