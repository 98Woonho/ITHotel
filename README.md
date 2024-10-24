ITHotel PROJECT PLANING
=

<br/>

## ▶️ 개발 동기
##### 호텔 예약 사이트에 챗봇 기능이 있으면 좋겠다고 생각하여 챗봇 기능이 추가된 호텔 예약 사이트를 개발하게 되었습니다.

<br/>

## ▶️ 개발 목표
##### 챗봇 기능이 추가된 호텔 예약 사이트 구현

<br/>

## ▶️ 개발 일정
|PLAN|일정|DESCRIPTION|
|---------------|----------------|------------------------|
|주제 선정 <br/> 요구사항 분석 <br/> 기술스택 결정 <br/> 개발환경 구축|2024-01-15 ~ 2024-01-16(02Days)|완료| 
|기본 프론트엔드 구조 기획 및 개발 <br/> DB 설계|2024-01-17 ~ 2024-01-19(03Days)|완료| 
|프론트엔드 및 백엔드 개발|2024-01-20 ~ 2024-02-10(22Days)|완료| 

<br/>

## ▶️ 구성인원 
|조원|역할|
|---------------|----------------|
|공통|요구사항분석 및 시스템 설계|
|이운호(조장)|Git, UI, 예약 및 결제 CRUD, 관리자 CRUD, 문서 작성 및 정리|
|황보성현(조원1)|게시판 CRUD, UI, 문서 작성 및 정리|
|함성모(조원2)|고객 CRUD|

<br/>

## ▶️ 개발 환경
|-|개발 환경|
|---------------|----------------|
|IDE|IntelliJ Ultimate|
|JDK|JDK 21|
|Java|Java 21|
|SpringBoot Version|3.1.7|
|Build Tool|gradle|
|DBMS|Mysql|
|Connection Pool|HikariCP|
|Version Control|Git|
|Repository Hosting|GitHub|
|Security|Spring Security|

<br/>

## ▶️ 사용 API
|용도|제공|API 문서|
|---------------|----------------|------------------------|
|결제|아임포트|[링크](https://developers.portone.io/docs/ko/readme?v=v1)|
|인증|아임포트|[링크](https://developers.portone.io/docs/ko/readme?v=v1)|
|주소 찾기|다음|[링크](https://postcode.map.daum.net/guide)|
|지도|카카오|[링크](https://apis.map.kakao.com)|
|OAuth2 로그인|카카오|[링크](https://developers.kakao.com/docs/latest/ko/index)|
|OAuth2 로그인|구글|[링크](https://console.cloud.google.com/)|
|OAuth2 로그인|네이버|[링크](https://developers.naver.com/products/login/api/api.md)|
|JAVA PERSISTENCE API|Spring|[링크](https://docs.spring.io/spring-data/jpa/reference/index.html)|

<br/>

## ▶️ 기술스택

![JAVA](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![MySQL](https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=Mysql&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=Axios&logoColor=white)
![BootStrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![Github](https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white)

<br/>

## ▶️ 주요 기능
|서비스|주요 기능|
|---------------|----------------|
|회원 서비스|<ul><li>JWT 기반 로그인</li><li>소셜 로그인 및 회원가입</li></ul>|
|게시판 서비스|<ul><li>공지사항, 고객 문의, FAQ 게시판</li></ul>|
|호텔 서비스|<ul><li>호텔 객실 예약/결제</li></ul>|
|사용자 마이페이지|<ul><li>회원 정보 수정/탈퇴</li><li>예약 취소</li><li>문의 사항 작성</li></ul>|
|관리자 페이지|<ul><li>예약 현황/취소</li><li>호텔 등록/수정/삭제</li><li>객실 등록/수정/삭제</li><li>공지사항 작성/자주묻는 질문 작성/문의사항 답변</li><li>매출 그래프</li></ul>|

<br/>

## ▶️ 시연 영상
[https://www.youtube.com/watch?v=4Y_hGYJT9Pc](https://www.youtube.com/watch?v=DTQzaR3oxDE)

<br/>

## ▶️ ERD
![ITHOTEL ERD](https://github.com/98Woonho/ITHotel/assets/145889732/e2fcbf21-d3f8-477e-a2b9-2ead065e483f)

<br/>

## ▶️ 클래스 다이어그램
![ITHotel_ClassDiagram](https://github.com/user-attachments/assets/5b655918-5b52-463c-addb-2bdc766bddef)


<br/>

📃: File Tree
---
```
ITHotel

C:.
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ example
   │  │        └─ ITHOTEL
   │  │           ├─ config
   │  │           │  ├─ auth
   │  │           │  │  ├─ exceptionHandler
   │  │           │  │  │  ├─ CustomAccessDeniedHandler.java
   │  │           │  │  │  └─ CustomAuthenticationEntryPoint.java
   │  │           │  │  ├─ jwt
   │  │           │  │  │  ├─ JwtAuthorizationFilter.java
   │  │           │  │  │  ├─ JwtProperties.java
   │  │           │  │  │  ├─ JwtTokenProvider.java
   │  │           │  │  │  ├─ KeyGenerator.java
   │  │           │  │  │  └─ TokenInfo.java
   │  │           │  │  ├─ loginHandler
   │  │           │  │  │  ├─ CustomAuthenticationFailureHandler.java
   │  │           │  │  │  ├─ CustomLoginSuccessHandler.java
   │  │           │  │  │  └─ OAuth2LoginSuccessHandler.java
   │  │           │  │  ├─ logoutHandler
   │  │           │  │  │  ├─ CustomLogoutHandler.java
   │  │           │  │  │  └─ CustomLogoutSuccessHandler.java
   │  │           │  │  ├─ PrincipalDetails.java
   │  │           │  │  ├─ PrincipalDetailsOAuth2Service.java
   │  │           │  │  ├─ PrincipalDetailsService.java
   │  │           │  │  └─ provider
   │  │           │  │     ├─ GoogleUserInfo.java
   │  │           │  │     ├─ KakaoUserInfo.java
   │  │           │  │     └─ OAuth2UserInfo.java
   │  │           │  ├─ DataSourceConfig.java
   │  │           │  ├─ SecurityConfig.java
   │  │           │  └─ WebMvcConfig.java
   │  │           ├─ controller
   │  │           │  ├─ AdminController.java
   │  │           │  ├─ BoardController.java
   │  │           │  ├─ HomeController.java
   │  │           │  ├─ HotelController.java
   │  │           │  ├─ HotelInfoController.java
   │  │           │  ├─ MyinfoController.java
   │  │           │  ├─ PaymentController.java
   │  │           │  ├─ PolicyContainer.java
   │  │           │  ├─ ReservationController.java
   │  │           │  ├─ RoomController.java
   │  │           │  └─ UserController.java
   │  │           ├─ domain
   │  │           │  ├─ common
   │  │           │  │  ├─ dto
   │  │           │  │  │  ├─ BoardDto.java
   │  │           │  │  │  ├─ BoardFileDto.java
   │  │           │  │  │  ├─ CommentDto.java
   │  │           │  │  │  ├─ HotelDto.java
   │  │           │  │  │  ├─ NoticeboardImageDto.java
   │  │           │  │  │  ├─ PaymentDto.java
   │  │           │  │  │  ├─ ReservationDto.java
   │  │           │  │  │  └─ RoomDto.java
   │  │           │  │  ├─ entity
   │  │           │  │  │  ├─ BaseEntity.java
   │  │           │  │  │  ├─ Comment.java
   │  │           │  │  │  ├─ Hotel.java
   │  │           │  │  │  ├─ HotelFileInfo.java
   │  │           │  │  │  ├─ InquiryBoard.java
   │  │           │  │  │  ├─ NoticeBoard.java
   │  │           │  │  │  ├─ NoticeBoardFileInfo.java
   │  │           │  │  │  ├─ NoticeBoardImage.java
   │  │           │  │  │  ├─ Payment.java
   │  │           │  │  │  ├─ Persistent_logins.java
   │  │           │  │  │  ├─ QuestionBoard.java
   │  │           │  │  │  ├─ Reservation.java
   │  │           │  │  │  ├─ ReservedRoomCount.java
   │  │           │  │  │  ├─ Room.java
   │  │           │  │  │  ├─ RoomFileInfo.java
   │  │           │  │  │  └─ Signature.java
   │  │           │  │  └─ repository
   │  │           │  │     ├─ CommentRepository.java
   │  │           │  │     ├─ HotelFileInfoRepository.java
   │  │           │  │     ├─ HotelRepository.java
   │  │           │  │     ├─ InquiryBoardRepository.java
   │  │           │  │     ├─ NoticeBoardFileInfoRepository.java
   │  │           │  │     ├─ NoticeBoardRepsoitory.java
   │  │           │  │     ├─ NoticeImageRepository.java
   │  │           │  │     ├─ PaymentRepository.java
   │  │           │  │     ├─ QuestionBoardRepository.java
   │  │           │  │     ├─ ReservationRepository.java
   │  │           │  │     ├─ ReservedRoomCountRepository.java
   │  │           │  │     ├─ RoomFileInfoRepository.java
   │  │           │  │     └─ RoomRepository.java
   │  │           │  └─ user
   │  │           │     ├─ dto
   │  │           │     │  └─ UserDto.java
   │  │           │     ├─ entity
   │  │           │     │  └─ User.java
   │  │           │     └─ repository
   │  │           │        └─ UserRepository.java
   │  │           ├─ HotelServiceApplication.java
   │  │           └─ service
   │  │              ├─ AdminService.java
   │  │              ├─ BoardService.java
   │  │              ├─ HomeService.java
   │  │              ├─ HotelService.java
   │  │              ├─ MyinfoService.java
   │  │              ├─ PaymentService.java
   │  │              ├─ ReservationService.java
   │  │              ├─ RoomService.java
   │  │              └─ UserService.java
   │  └─ resources
   │     ├─ application.properties
   │     ├─ static
   │     │  ├─ css
   │     │  │  ├─ admin
   │     │  │  │  ├─ deleteHotel.css
   │     │  │  │  ├─ hotelStatus.css
   │     │  │  │  ├─ inquiryList.css
   │     │  │  │  ├─ inquiryListDetail.css
   │     │  │  │  ├─ modifyHotel.css
   │     │  │  │  ├─ modifyRoom.css
   │     │  │  │  ├─ monthSales.css
   │     │  │  │  ├─ noticeWrite.css
   │     │  │  │  ├─ questionWrite.css
   │     │  │  │  ├─ registerHotel.css
   │     │  │  │  ├─ registerRoom.css
   │     │  │  │  ├─ reservationStatus.css
   │     │  │  │  └─ roomStatus.css
   │     │  │  ├─ board
   │     │  │  │  ├─ basic.css
   │     │  │  │  ├─ hotelContact.css
   │     │  │  │  ├─ inquiryForm.css
   │     │  │  │  ├─ notice.css
   │     │  │  │  ├─ question.css
   │     │  │  │  ├─ questionDetail.css
   │     │  │  │  └─ questionUpdate.css
   │     │  │  ├─ common.css
   │     │  │  ├─ hotel
   │     │  │  │  ├─ find.css
   │     │  │  │  └─ findMap.css
   │     │  │  ├─ hotelInfo
   │     │  │  │  ├─ award.css
   │     │  │  │  ├─ history.css
   │     │  │  │  └─ info.css
   │     │  │  ├─ index.css
   │     │  │  ├─ payment
   │     │  │  │  └─ read.css
   │     │  │  ├─ policy
   │     │  │  │  ├─ privacyPolicy.css
   │     │  │  │  └─ terms.css
   │     │  │  ├─ reservation
   │     │  │  │  └─ select.css
   │     │  │  └─ user
   │     │  │     ├─ findId.css
   │     │  │     ├─ findPw.css
   │     │  │     ├─ inquiryInfoDetail.css
   │     │  │     ├─ join.css
   │     │  │     ├─ login.css
   │     │  │     ├─ myinfo.css
   │     │  │     └─ reservationinfo.css
   │     │  ├─ images
   │     │  │  ├─ btns.png
   │     │  │  ├─ cancel_btn.png
   │     │  │  ├─ diningRoom.jpg
   │     │  │  ├─ google-icon.png
   │     │  │  ├─ gym.jpg
   │     │  │  ├─ hotel1.jpg
   │     │  │  ├─ hotel2.jpg
   │     │  │  ├─ hotel3.jpg
   │     │  │  ├─ ITHOTEL.header.png
   │     │  │  ├─ kakao-icon.png
   │     │  │  ├─ lounge.jpg
   │     │  │  ├─ payco.png
   │     │  │  ├─ room1.webp
   │     │  │  ├─ room2.webp
   │     │  │  ├─ room3.webp
   │     │  │  └─ tosspay.png
   │     │  └─ js
   │     │     ├─ admin
   │     │     │  ├─ inquiryList.js
   │     │     │  ├─ inquiryListDetail.js
   │     │     │  ├─ modifyHotel.js
   │     │     │  ├─ modifyNotice.js
   │     │     │  ├─ modifyRoom.js
   │     │     │  ├─ monthSales.js
   │     │     │  ├─ noticeWrite.js
   │     │     │  ├─ registerHotel.js
   │     │     │  ├─ registerRoom.js
   │     │     │  ├─ reservationStatus.js
   │     │     │  └─ roomStatus.js
   │     │     ├─ board
   │     │     │  ├─ libraries
   │     │     │  │  ├─ ckeditor.js
   │     │     │  │  ├─ ckeditor.js.map
   │     │     │  │  └─ translations
   │     │     │  │     ├─ af.js
   │     │     │  │     ├─ ar.js
   │     │     │  │     ├─ ast.js
   │     │     │  │     ├─ az.js
   │     │     │  │     ├─ bg.js
   │     │     │  │     ├─ bn.js
   │     │     │  │     ├─ bs.js
   │     │     │  │     ├─ ca.js
   │     │     │  │     ├─ cs.js
   │     │     │  │     ├─ da.js
   │     │     │  │     ├─ de-ch.js
   │     │     │  │     ├─ de.js
   │     │     │  │     ├─ el.js
   │     │     │  │     ├─ en-au.js
   │     │     │  │     ├─ en-gb.js
   │     │     │  │     ├─ en.js
   │     │     │  │     ├─ eo.js
   │     │     │  │     ├─ es-co.js
   │     │     │  │     ├─ es.js
   │     │     │  │     ├─ et.js
   │     │     │  │     ├─ eu.js
   │     │     │  │     ├─ fa.js
   │     │     │  │     ├─ fi.js
   │     │     │  │     ├─ fr.js
   │     │     │  │     ├─ gl.js
   │     │     │  │     ├─ gu.js
   │     │     │  │     ├─ he.js
   │     │     │  │     ├─ hi.js
   │     │     │  │     ├─ hr.js
   │     │     │  │     ├─ hu.js
   │     │     │  │     ├─ hy.js
   │     │     │  │     ├─ id.js
   │     │     │  │     ├─ it.js
   │     │     │  │     ├─ ja.js
   │     │     │  │     ├─ jv.js
   │     │     │  │     ├─ km.js
   │     │     │  │     ├─ kn.js
   │     │     │  │     ├─ ku.js
   │     │     │  │     ├─ lt.js
   │     │     │  │     ├─ lv.js
   │     │     │  │     ├─ ms.js
   │     │     │  │     ├─ nb.js
   │     │     │  │     ├─ ne.js
   │     │     │  │     ├─ nl.js
   │     │     │  │     ├─ no.js
   │     │     │  │     ├─ oc.js
   │     │     │  │     ├─ pl.js
   │     │     │  │     ├─ pt-br.js
   │     │     │  │     ├─ pt.js
   │     │     │  │     ├─ ro.js
   │     │     │  │     ├─ ru.js
   │     │     │  │     ├─ si.js
   │     │     │  │     ├─ sk.js
   │     │     │  │     ├─ sl.js
   │     │     │  │     ├─ sq.js
   │     │     │  │     ├─ sr-latn.js
   │     │     │  │     ├─ sr.js
   │     │     │  │     ├─ sv.js
   │     │     │  │     ├─ th.js
   │     │     │  │     ├─ tk.js
   │     │     │  │     ├─ tr.js
   │     │     │  │     ├─ tt.js
   │     │     │  │     ├─ ug.js
   │     │     │  │     ├─ uk.js
   │     │     │  │     ├─ ur.js
   │     │     │  │     ├─ uz.js
   │     │     │  │     ├─ vi.js
   │     │     │  │     ├─ zh-cn.js
   │     │     │  │     └─ zh.js
   │     │     │  ├─ noticeBoardDetail.js
   │     │     │  ├─ question.js
   │     │     │  ├─ questionDetail.js
   │     │     │  └─ questionUpdate.js
   │     │     ├─ common.js
   │     │     ├─ hotel
   │     │     │  ├─ find.js
   │     │     │  └─ findMap.js
   │     │     ├─ index.js
   │     │     ├─ payment
   │     │     │  └─ read.js
   │     │     ├─ policy
   │     │     │  ├─ privacyPolicy.js
   │     │     │  └─ terms.js
   │     │     ├─ reservation
   │     │     │  └─ select.js
   │     │     └─ user
   │     │        ├─ findId.js
   │     │        ├─ findPw.js
   │     │        ├─ informationInfo.js
   │     │        ├─ inquiryInfo.js
   │     │        ├─ inquiryInfoDetail.js
   │     │        ├─ join.js
   │     │        ├─ OauthJoin.js
   │     │        └─ reservationInfo.js
   │     └─ templates
   │        ├─ admin
   │        │  ├─ hotelStatus.html
   │        │  ├─ inquiryList.html
   │        │  ├─ inquiryListDetail.html
   │        │  ├─ modifyHotel.html
   │        │  ├─ modifyNotice.html
   │        │  ├─ modifyRoom.html
   │        │  ├─ monthSales.html
   │        │  ├─ noticeWrite.html
   │        │  ├─ questionWrite.html
   │        │  ├─ registerHotel.html
   │        │  ├─ registerRoom.html
   │        │  ├─ reservationStatus.html
   │        │  └─ roomStatus.html
   │        ├─ board
   │        │  ├─ basic.html
   │        │  ├─ hotelContact.html
   │        │  ├─ inquiryForm.html
   │        │  ├─ noticeBoardDetail.html
   │        │  ├─ noticeList.html
   │        │  ├─ question.html
   │        │  ├─ questionDetail.html
   │        │  └─ questionUpdate.html
   │        ├─ fragments
   │        │  ├─ adminLeft.html
   │        │  ├─ boardLeft.html
   │        │  ├─ findTop.html
   │        │  ├─ footer.html
   │        │  ├─ header.html
   │        │  ├─ hotelList.html
   │        │  ├─ link.html
   │        │  └─ userLeft.html
   │        ├─ hotel
   │        │  ├─ find.html
   │        │  └─ findMap.html
   │        ├─ hotelInfo
   │        │  ├─ award.html
   │        │  ├─ history.html
   │        │  └─ info.html
   │        ├─ index.html
   │        ├─ payment
   │        │  └─ read.html
   │        ├─ policy
   │        │  ├─ privacyPolicy.html
   │        │  └─ terms.html
   │        ├─ reservation
   │        │  └─ select.html
   │        └─ user
   │           ├─ findId.html
   │           ├─ findPw.html
   │           ├─ informationInfo.html
   │           ├─ inquiryInfo.html
   │           ├─ inquiryInfoDetail.html
   │           ├─ join.html
   │           ├─ login.html
   │           ├─ OauthJoin.html
   │           └─ reservationInfo.html
   └─ test
      └─ java
         └─ com
            └─ example
               └─ ITHOTEL
                  └─ HotelServiceApplicationTests.java

```
