let currentIndex = 0;
const totalImages = document.querySelectorAll('.slide ul li').length;
const slide = document.querySelector('.slide ul');

function slideImage(direction) {
    if (direction === 'prev') {
        currentIndex = (currentIndex - 1 + totalImages) % totalImages;
    } else if (direction === 'next') {
        currentIndex = (currentIndex + 1) % totalImages;
    }
    const translateValue = -currentIndex * 33.33333333333 + '%';
    slide.style.transform = 'translateX(' + translateValue + ')';
}


/* 성인, 어린이 + - 버튼 js */
let adultNum = 1;
let childNum = 0;

searchForm['adultCount'].value = adultNum;
searchForm['childCount'].value = childNum;

function adultMinus() {
    if (adultNum > 1) {
        adultNum -= 1;
    }
    searchForm['adultCount'].value = adultNum;
}

function adultPlus() {
    adultNum += 1;
    searchForm['adultCount'].value = adultNum;
}

function childMinus() {
    if (childNum > 0) {
        childNum -= 1;
    }
    searchForm['childCount'].value = childNum;
}

function childPlus() {
    childNum += 1;
    searchForm['childCount'].value = childNum;
}


document.addEventListener('scroll', function() {
    const scrollPosition = window.scrollY;
    const mainSection1 = document.querySelector('.main-section-1');
    const mainSection2 = document.querySelector('.main-section-2');
    const swiper = document.querySelector('.swiper');

    if (scrollPosition > 200) {
        mainSection1.classList.add('visible');
    } else {
        mainSection1.classList.remove('visible');
    }

    if (scrollPosition > 650) {
        mainSection2.classList.add('visible');
    } else {
        mainSection2.classList.remove('visible');
    }

    if (scrollPosition > 1150) {
        swiper.classList.add('visible');
    } else {
        swiper.classList.remove('visible');
    }
});



new Swiper('.swiper', {
    cssMode: true,
    navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
    },
    autoplay: {
        delay: 5000, // 시간 설정
        disableOnInteraction: false, // false-스와이프 후 자동 재생
        loop: true,
    },
})







// chatbot
// 채팅 메시지를 표시할 DOM
const chatMessages = document.querySelector('.chat-messages');
// 사용자 입력 필드
const userInput = document.querySelector('.user-input input');
// 전송 버튼
const sendButton = document.querySelector('.user-input button');
// 발급받은 OpenAI API 키를 변수로 저장
const apiKey = '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';
// OpenAI API 엔드포인트 주소를 변수로 저장
const apiEndpoint = 'https://api.openai.com/v1/chat/completions'
function addMessage(sender, message) {
    // 새로운 div 생성
    const messageElement = document.createElement('div');
    if(sender === "나") {
        messageElement.className = 'message-me';
        messageElement.textContent = `${message}`;
        chatMessages.prepend(messageElement);
    } else {
        messageElement.className = 'message';
        messageElement.textContent = `${message}`;
        chatMessages.prepend(messageElement);
    }
}
// ChatGPT API 요청
async function fetchAIResponse(prompt) {
    // API 요청에 사용할 옵션을 정의
    const requestOptions = {
        method: 'POST',
        // API 요청의 헤더를 설정
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${apiKey}`
        },
        body: JSON.stringify({
            model: "gpt-3.5-turbo",  // 사용할 AI 모델
            messages: [{
                role: "user", // 메시지 역할을 user로 설정
                content: prompt // 사용자가 입력한 메시지
            }, ],
            temperature: 0.8, // 모델의 출력 다양성
            max_tokens: 1024, // 응답받을 메시지 최대 토큰(단어) 수 설정
            top_p: 1, // 토큰 샘플링 확률을 설정
            frequency_penalty: 0.5, // 일반적으로 나오지 않는 단어를 억제하는 정도
            presence_penalty: 0.5, // 동일한 단어나 구문이 반복되는 것을 억제하는 정도
            stop: ["Human"], // 생성된 텍스트에서 종료 구문을 설정
        }),
    };
    // API 요청후 응답 처리
    try {
        const response = await fetch(apiEndpoint, requestOptions);
        const data = await response.json();
        const aiResponse = data.choices[0].message.content;
        return aiResponse;
    } catch (error) {
        console.error('OpenAI API 호출 중 오류 발생:', error);
        return 'OpenAI API 호출 중 오류 발생';
    }
}
// 전송 버튼 클릭 이벤트 처리
sendButton.addEventListener('click', async () => {
    // 사용자가 입력한 메시지
    const message = userInput.value.trim();
    // 메시지가 비어있으면 리턴
    if (message.length === 0) return;
    // 사용자 메시지 화면에 추가
    addMessage('나', message);
    userInput.value = '';
    //ChatGPT API 요청후 답변을 화면에 추가
    const aiResponse = await fetchAIResponse(message);
    addMessage('챗봇', aiResponse);
});
// 사용자 입력 필드에서 Enter 키 이벤트를 처리
userInput.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        sendButton.click();
    }
});


const chatIcon = document.querySelector('.chat-icon');
const chat = document.querySelector('.chat');

chatIcon.addEventListener('click', function(e) {
    e.preventDefault();

    if(chat.classList.contains('visible')) {
        chat.classList.remove('visible');
    } else {
        chat.classList.add('visible');
    }
})