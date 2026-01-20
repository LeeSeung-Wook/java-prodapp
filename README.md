# 상품 관리 소켓 프로그램

이 프로젝트는 **Java Socket + Gson + JDBC(MySQL)** 를 사용하여
간단한 **상품 관리 시스템**을 구현하는 실습용 프로그램이다.

클라이언트와 서버가 소켓으로 통신하며,
JSON 형식의 요청(Request)과 응답(Response)을 주고받는다.

---

## 1. 프로그램 구성

프로그램은 크게 **Client / Server / DTO / Repository** 구조로 나뉜다.

```
client/
 └─ MyClient.java        # 사용자 입력 및 서버 요청

server/
 ├─ MyServer.java        # 소켓 서버, 요청 분기 처리
 ├─ ProductService.java  # 비즈니스 로직
 ├─ ProductRepository.java # DB 접근 (CRUD)
 ├─ Product.java         # 상품 엔티티
 └─ DBConnection.java    # DB 연결 관리

dto/
 ├─ RequestDTO.java      # 요청 DTO
 └─ ResponseDTO.java     # 응답 DTO
```

---

## 2. 동작 흐름

### 전체 흐름

1. 서버(MyServer)가 20000번 포트에서 대기한다.
2. 클라이언트(MyClient)가 서버에 소켓으로 연결한다.
3. 사용자가 명령어를 입력한다.
4. 클라이언트가 입력을 JSON(RequestDTO)으로 변환해 서버로 전송한다.
5. 서버는 요청을 파싱하여 서비스 로직을 실행한다.
6. 서버는 결과를 JSON(ResponseDTO)으로 클라이언트에 응답한다.
7. 클라이언트는 응답을 파싱하여 콘솔에 출력한다.

---

## 3. 사용 기술

* Java Socket (TCP 통신)
* Gson (JSON 직렬화 / 역직렬화)
* JDBC + MySQL
* DTO 패턴
* Repository / Service 계층 분리

---

## 4. 요청 / 응답 구조

### RequestDTO 구조

```json
{
  "method": "get",
  "queryString": { "id": 1 },
  "body": null
}
```

* method : 요청 종류 (get, getall, insert, delete)
* queryString : 단순 조회용 파라미터
* body : 객체 데이터 전송용

---

### ResponseDTO 구조

```json
{
  "msg": "ok",
  "body": {
    "id": 1,
    "name": "사과",
    "price": 1000,
    "qty": 5
  }
}
```

* msg : 처리 결과 메시지
* body : 응답 데이터 (상품, 상품목록, null)

---

## 5. 지원 기능

### 1️⃣ 상품 상세 조회

```
get 1
```

* id에 해당하는 상품을 조회한다.

---

### 2️⃣ 상품 전체 조회

```
getall
```

* 모든 상품 목록을 조회한다.

---

### 3️⃣ 상품 등록

```
insert 상품명 가격 수량
```

예시:

```
insert 바나나 2000 10
```

* 상품을 DB에 저장한다.

---

### 4️⃣ 상품 삭제

```
delete 3
```

* id에 해당하는 상품을 삭제한다.

---

### 5️⃣ 종료

```
exit
```

* 클라이언트 및 서버 연결을 종료한다.

---

## 6. 설계 포인트

* DTO를 사용하여 클라이언트/서버 간 데이터 형식을 통일했다.
* ResponseDTO를 제네릭으로 설계하여 다양한 응답 타입을 처리했다.
* Repository는 DB 접근만 담당하도록 역할을 분리했다.
* Service 계층에서 예외를 제어하여 서버 로직을 단순화했다.

---

## 7. 학습 목적

이 프로젝트의 목적은 다음과 같다.

* 소켓 통신의 전체 흐름 이해
* JSON 기반 통신 구조 이해
* DTO와 제네릭의 활용
* JDBC를 이용한 CRUD 실습
* 서버/클라이언트 책임 분리 설계 연습

---

## 8. 한 줄 요약

> 이 프로그램은 **소켓 기반 JSON 통신으로 상품을 관리하는 서버-클라이언트 구조 실습 프로젝트**다.
