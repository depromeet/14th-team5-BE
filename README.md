# Bibbi: 하루 한번, 가족에게 보내는 생존 신고

` 연락에 대한 부담감과 거부감이 들지 않게
간편하고 사용하기 쉬운 기능으로
일상을 공유하게 유도한다 `


<img src = "https://github.com/depromeet/14th-team5-iOS/assets/62610032/ef7d1e84-93b8-4def-b12a-85dd95c22bce" width = "800" />

<br />


#### "하루 한 번, 가족과의 소중한 연락!"

가족은 삶의 중요한 부분이죠. 하지만 빠른 일상에 묻혀 자주 소통하지 못하는 경우가 많습니다. 이제, 삐삐와 함께 '일일 생존 신고' 프로젝트를 시작해보세요!

매일, 간단한 메세지와 사진을 통해 가족에게 생존을 알리면서 소중한 순간들을 함께 나눌 수 있습니다. 까먹지 않고, 더욱 멋지고 따뜻한 가족 소통의 시작을 만들어보세요. 나중에는 이 작은 노력이 행복한 추억으로 기억될 것입니다.

가족과의 소중한 시간, 삐삐와 함께라면 언제나 더 특별한 것 같아요! ❤️


> "Once a day, cherish the connection with your family!
Family is an essential part of life, yet amidst the fast-paced routine, meaningful communication often takes a back seat. Now, with the 'Daily Survival Report' project by Pippy, initiate a new era of communication with your loved ones!
Every day, through simple messages and photos, you can share your survival with your family, creating moments of togetherness. Never forget, with Pippy, embark on a journey of stylish and warm family communication. Later on, these small efforts will be remembered as joyful memories.
In the precious time spent with family, everything feels more special with Pippy by your side! ❤️"


<br />

### 🎇 Project Contributors

<table>
    <tbody>
    <tr>
         <td align="center" valign="top" width="22.28%"><a href="https://github.com/cchuyong"><img src="https://avatars.githubusercontent.com/u/67673493?v=4" width="140px;" alt="CChuYong"/><br /><sub><b>Yeongmin Song</b></sub></a><br /><span>백엔드 개발(파트장)</span></td>
         <td align="center" valign="top" width="22.28%"><a href="https://github.com/cchuyong"><img src="https://avatars.githubusercontent.com/u/69844138?v=4" width="140px;" alt="CChuYong"/><br /><sub><b>Jisoo Lim</b></sub></a><br /><span>백엔드 개발</span></td>
         <td align="center" valign="top" width="22.28%"><a href="https://github.com/cchuyong"><img src="https://avatars.githubusercontent.com/u/49567744?v=4" width="140px;" alt="CChuYong"/><br /><sub><b>Soonchan Kwon</b></sub></a><br /><span>백엔드 개발</span></td>
    </tr>
    </tbody>
</table>

<br/>

### 🖥️ Project Tech Stacks

- JVM Runtime Amazon Corretto 17
- SpringBoot 3.1.5 (Servlet MVC)
- Spring Data JPA with QueryDSL
- Stateless Session Management with JWT + Spring Security
- Module Architecture with Gradle Multi-Project
<br/><br/>

### 🛠 환경변수

| 이름                         | 설명                          |
|----------------------------|-----------------------------|
| MYSQL_URL                  | MYSQL 주소입니다 (JDBC 형태여야 합니다) |
| MYSQL_USERNAME             | MYSQL 사용자 명 입니다.            |
| MYSQL_PASSWORD             | MYSQL 비밀번호 입니다.             |
| SLACK_WEBHOOK_URL          | 슬랙 웹훅 URL 입니다.              |
| TOKEN_SECRET_KEY           | JWT 토큰용 시크릿 키 입니다.          |
| OBJECT_STORAGE_REGION      | NCP Region 입니다.             |
| OBJECT_STORAGE_END_POINT   | NCP 엔드포인트 입니다.              |
| OBJECT_STORAGE_ACCESS_KEY  | NCP 액세스 키 입니다.              |
| OBJECT_STORAGE_SECRET_KEY  | NCP 시크릿 키 입니다.              |
| OBJECT_STORAGE_BUCKET_NAME | NCP ObjectStroage 버킷명 입니다.  |
| GOOGLE_CLIENT_ID           | 구글 로그인 클라이언트 ID 입니다.        |
