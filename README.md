WEB PROJECT PLANING
=

## ▶️ 개발 동기
##### 호텔 예약 사이트에 챗봇 기능이 있으면 좋겠다고 생각

## ▶️ 개발 목표
##### 챗봇 기능이 추가된 호텔 예약 사이트 구현

<br/>

## ▶️ 개발 일정
#### 2024-01-15 ~ 2024-01-16(02Days) : 주제 선정 및 요구사항 분석, 기술스택 결정, 개발환경 구축
#### 2024-01-17 ~ 2024-01-19(03Days) : 기본 프론트엔드 구조 기획 및 개발, DB 기획 및 개발
#### 2024-01-20 ~ 2024-02-10(22Days) : 프론트엔드 및 백엔드 개발

<br/>

## ▶️ 구성인원 
##### 이운호(조장) : Git, UI, 예약 및 결제 CRUD, 관리자 CRUD
##### 황보성현(조원1) : 게시판 CRUD, UI
##### 함성모(조원2) : 고객 CRUD, UI

<br/>

## ▶️ 개발 환경
##### IDE : IntelliJ Ultimate
##### OpenJDK 21
##### Java21
##### SpringBoot 3.1.7
##### gradle
##### Mysql Server
##### Mysql Workbench

<br/>

## ▶️ 사용 API
##### 아임포트 결제시스템 API
##### 카카오 지도 API
##### 다음 주소 API
##### OAuth2 로그인 API

<br/>

## ▶️ 기술스택

![JAVA](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![MySQL](https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=Mysql&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=Axios&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![Github](https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white)

<br/>

## ▶️ END POINT 

|END POINT|METHOD|DESCRIPTION|
|------|---|---|
|/|GET|홈페이지|
|/hotel/find|GET|호텔 찾기 페이지|
|/hotel/findMap|GET|호텔 찾기 지도 페이지|
|/hotel/info|GET|호텔 정보 페이지|
|/reservation/select|GET|호텔 및 객실 선택 페이지|
|/payment/read|GET|결제 페이지|
|/admin/inquiry|GET|관리자 문의 페이지|
|/admin/modifyHotel|GET|관리자 호텔 수정 페이지|
|/admin/modifyRoom|GET|관리자 객실 수정 페이지|
|/admin/registerHotel|GET|관리자 호텔 등록 페이지|
|/admin/registerRoom|GET|관리자 객실 등록 페이지|
|/admin/monthSales|GET|관리자 월 매출 페이지|
|/admin/monthSales|GET|관리자 월 매출 페이지|
|/admin/hotelStatus|GET|관리자 호텔 현황 페이지|
|/admin/reservationStatus|GET|관리자 예약 현황 페이지|
|/admin/roomStatus|GET|관리자 객실 현황 페이지|
|/policy/privacyPolicy|GET|개인정보 처리방침 페이지|
|/policy/terms|GET|이용약관 페이지|
|/user/login|GET|로그인 페이지|
|/user/join|GET|회원가입 페이지|
|/user/join|POST|회원가입|
|/user/confirmId|GET|아이디 중복 확인|
|/user/sendEmail/{email}|GET|이메일 인증 보내기|
|/user/confirmEmail|GET|이메일 인증 확인|
|/board/save|GET|게시글 작성 페이지|
|/board/save|POST|게시글 작성|
|/board/list|GET|게시글 목록 페이지|
|/board/update/{id}|GET|게시글 수정 페이지|
|/board/update|POST|게시글 수정|
|/board/delete/{id}|GET|게시글 삭제|
|/comment/save|POST|댓글 작성|
<br/>
