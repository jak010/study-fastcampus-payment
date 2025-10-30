# Fastcampus Payment System Specification

## 1. 개요

이 문서는 본 프로젝트의 주요 기능과 아키텍처를 설명합니다. 

이 시스템은 사용자 지갑 관리, 결제 및 충전 트랜잭션 처리, 외부 PG(Payment Gateway) 연동, 그리고
결제 실패 시 재시도 메커니즘을 제공합니다.

## 2. 주요 기능

### 2.1. 지갑 관리 (Wallet)

- **지갑 생성**: 사용자는 자신의 지갑을 생성할 수 있습니다. (초기 잔액 0원)
- **지갑 조회**: 사용자 ID 또는 지갑 ID를 통해 지갑 정보를 조회할 수 있습니다.
- **잔액 추가/차감**: 지갑에 잔액을 추가하거나 결제 시 잔액을 차감할 수 있습니다.
    - 잔액 부족 시 오류 처리
    - 최대 충전 한도 (100억) 검증

### 2.2. 주문 및 결제 (Order & Checkout)

- **주문 생성**: 사용자, 금액, 요청 ID, (선택적으로) 강좌 ID 및 강좌 이름을 포함하는 주문을 생성합니다.
- **결제 요청**: 생성된 주문에 대해 PG사에 결제 승인을 요청합니다.
    - 일반 결제 (`/confirm`)
    - 충전 결제 (`/charge-confirm`)
- **주문 상태 관리**: 주문의 상태를 `WAIT`, `REQUESTED`, `APPROVED`로 관리합니다.
- **결제 결과 페이지**: 결제 요청, 성공, 실패에 대한 HTML 페이지를 제공합니다.

### 2.3. 트랜잭션 관리 (Transaction)

- **충전 트랜잭션**: 사용자 지갑에 금액을 충전하는 트랜잭션을 기록합니다.
- **결제 트랜잭션**: 사용자 지갑에서 금액을 차감하고 결제 내역을 기록합니다.
- **트랜잭션 조회**: `orderId`를 통해 트랜잭션을 조회할 수 있습니다.
- **트랜잭션 타입**: `CHARGE`와 `PAYMENT` 두 가지 타입으로 트랜잭션을 구분합니다.

### 2.4. 외부 결제 게이트웨이 연동 (External PG)

- **PG 승인 요청**: 외부 PG사에 결제 승인 요청을 보냅니다.
- **인증**: `SECRET` 키를 사용하여 Basic 인증을 수행합니다.
- **응답 처리**: PG사의 응답을 처리하고, 오류 발생 시 예외를 발생시킵니다.

### 2.5. 재시도 메커니즘 (Retry)

- **재시도 요청 생성**: PG사와의 통신 중 `SocketTimeoutException`과 같은 특정 예외 발생 시 재시도 요청을 생성하여 저장합니다.
- **재시도 처리**: 저장된 재시도 요청을 조회하여 다시 결제 처리를 시도합니다.
    - 재시도 횟수 증가
    - 재시도 성공 시 상태 변경 (`SUCCESS`)

## 3. 아키텍처 개요

Fastcampus Payment 시스템은 Spring Boot 기반의 모놀리식 아키텍처로 구성되어 있습니다. 주요 구성 요소는 다음과 같습니다.

- **Controller**: 웹 요청을 처리하고 서비스 계층으로 전달합니다. (예: `CheckoutController`, `ChargeController`,
  `WalletController`, `RetryController`, `TransactionController`)
- **Service**: 비즈니스 로직을 구현하고 트랜잭션을 관리합니다. (예: `PaymentProcessingService`, `WalletService`,
  `TransactionService`, `RetryRequestService`, `PaymentGatewayService`)
- **Repository**: JPA를 사용하여 데이터베이스와 상호작용합니다. (예: `OrderRepository`, `WalletRepository`,
  `RetryRequestRepository`, `TransactionRepository`)
- **Entity/Record**: 데이터베이스 테이블과 매핑되는 도메인 객체 또는 요청/응답 데이터 구조를 정의합니다. (예: `Order`, `Wallet`,
  `RetryRequest`, `Transaction`, `ConfirmRequest`, `CreateWalletRequest` 등)
- **External**: 외부 시스템(PG사)과의 연동을 담당합니다. (예: `PaymentGatewayService`)

## 4. 데이터 모델

- **Order**: 주문 정보를 저장합니다. (ID, 사용자 ID, 금액, 요청 ID, 강좌 ID, 강좌 이름, 상태, 생성/수정 시간)
- **Wallet**: 사용자 지갑 정보를 저장합니다. (ID, 사용자 ID, 잔액, 생성/수정 시간)
- **Transaction**: 결제 및 충전 트랜잭션 내역을 저장합니다. (ID, 사용자 ID, 지갑 ID, 주문 ID, 트랜잭션 타입, 금액, 설명, 생성/수정 시간)
- **RetryRequest**: 결제 실패 시 재시도를 위한 요청 정보를 저장합니다. (ID, 요청 JSON, 요청 ID, 재시도 횟수, 오류 응답, 상태, 생성/수정 시간)

## 5. 기술 스택

- **언어**: Java
- **프레임워크**: Spring Boot
- **데이터베이스**: H2 (개발/테스트), MySQL (예상)
- **ORM**: Spring Data JPA
- **빌드 도구**: Gradle
- **기타**: Lombok, Jackson (ObjectMapper)
