# 🐰 뭉탱이 - 게이미피케이션 기반 스터디 관리 플랫폼

> 재미있는 학습 경험을 위한 컬렉션 & 퀘스트 시스템

**뭉탱이**는 스터디를 지속적으로 할 수 있도록 게이미피케이션 요소를 포함한 스터디 관리 플랫폼입니다.
과제 제출, 댓글 작성 등의 활동으로 경험치를 획득하고, 다양한 '뭉탱이' 캐릭터를 수집하며 학습 동기를 부여합니다.
스터디 과제 부여, 과제 제출, 과제 승인 기능을 통해 스터디 관리를 쉽게 하도록 지원합니다.

## 👥 팀 정보
- **개발 기간**: 2025.11.15 - 2026.01.23
- **팀 구성**: BE - 김지환, BE - 이상현, FE - 김진수, DE - 이요셈

## 📌 주요 기능

### 🎮 게이미피케이션 시스템
- **경험치 & 칭호**: 활동별 XP 획득, 누적 경험치에 따른 칭호 부여 (비기너 → 프로 → 마스터)
- **일일 퀘스트**: 5종 퀘스트 (로그인, 댓글, 과제 제출, 감정표현, 스터디 생성) + 자동 리셋
- **컬렉션 시스템**: 13종 뭉탱이 도감 (경험치, 스터디 활동, 특별 코드 기반 해금)
- **온보딩 미션**: 신규 가입자를 위한 순차적 튜토리얼 (스터디 참여 → 과제 제출 → 댓글 작성 → 프로필 변경)

### 📚 스터디 관리
- **스터디 CRUD**: 생성/조회/수정/삭제 + 초대 코드 기반 참여 시스템
- **과제 관리**: 과제 생성, 할당, 제출, 승인 플로우 + 파일 첨부 (AWS S3)
- **소셜 기능**: 댓글, 감정표현 (리액션), 실시간 알림

### 🤖 AI 기반 과제 관리
- **Google Gemini API 연동**: 과제 자동 생성, 학습 프로세스 추천

### 🔔 알림 시스템
- **이벤트 기반 알림**: 과제 제출, 댓글, 과제 승인 시 자동 알림
- **스케줄러 알림**: 과제 마감 임박(D-1), 미제출 알림(마감 후)

### 👨‍💼 관리자 기능
- **어드민 대시보드**: 회원/스터디 관리, Thymeleaf 기반 서버 사이드 렌더링
- **통계 조회**: 활동 통계, 탑러너 조회 (경험치 상위 5명)

## 🛠 기술 스택

### Backend
- **Java 21** + **Spring Boot 3.5.7**
- **Spring Data JPA** (Hibernate)
- **Spring Security** + **JWT** 인증
- **MySQL** (운영) / **H2** (개발)

### Infrastructure
- **AWS S3**: 과제 첨부 파일 저장
- **Google Gemini API**: AI 기반 과제 생성
- **Spring Scheduler**: 일일 퀘스트 리셋, 알림 발송

### Architecture
- **DDD (Domain-Driven Design)**: 도메인 중심 설계
- **Event-Driven Architecture**: Spring ApplicationEvent 기반 비동기 처리
- **계층 구조**: API → Application → Domain

## 🏗 프로젝트 구조

```
src/main/java/econovation/moongtaengi/
├── admin/          # 관리자 기능 (대시보드, 회원/스터디 관리)
├── collection/     # 컬렉션 도감 (뭉탱이 해금/장착)
├── gamification/   # 게이미피케이션 (일일퀘스트, 경험치)
├── gemini/         # Google Gemini API 연동 (AI 과제 생성)
├── global/         # 공통 설정 (Security, Exception, BaseEntity)
├── home/           # 메인 페이지 (대시보드, 탑러너)
├── member/         # 회원 관리 (인증, 프로필, 칭호)
├── notification/   # 알림 시스템 (이벤트 기반 + 스케줄러)
├── onboarding/     # 온보딩 미션 (신규 가입자 튜토리얼)
├── study/          # 스터디 관리 (과제, 제출, 댓글, 감정표현)
└── title/          # 칭호 시스템 (경험치 기반 등급)
```

## 🎯 핵심 설계 원칙

### 1. 도메인 이벤트 기반 아키텍처
- **느슨한 결합**: 도메인 간 의존성 최소화
- **트랜잭션 분리**: `@TransactionalEventListener` + `REQUIRES_NEW`로 메인 트랜잭션과 분리
- **실패 격리**: 이벤트 처리 실패가 메인 로직에 영향 없음

```java
// 도메인 이벤트 발행 예시
public class Member extends AbstractAggregateRoot<Member> {
    public void addExperience(int amount) {
        this.totalExperience += amount;
        registerEvent(new ExperienceAddedEvent(this.id, this.totalExperience));
    }
}

// 이벤트 리스너 - 다른 도메인에서 독립적으로 처리
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void handleExperienceAdded(ExperienceAddedEvent event) {
    // 경험치 기반 컬렉션 해금 로직
    if (event.totalExperience() >= 100) {
        collectionService.unlockCollection(event.memberId(), CollectionType.PRO);
    }
}
```

### 2. 성능 최적화
- **Custom JPQL/QueryDSL**: `findAll()` + Stream 대신 DB 레벨 필터링
- **정적 자산 관리**: 이미지 URL 등 정적 데이터는 Enum에서 직접 관리 (DB 조회 불필요)

```java
// ❌ Bad: 메모리에 로드 후 필터링
List<Quest> all = repository.findAll();
List<Quest> filtered = all.stream().filter(q -> q.getDate().equals(today)).toList();

// ✅ Good: DB에서 필터링
@Query("SELECT q FROM DailyQuest q WHERE q.date = :date AND q.memberId = :memberId")
List<DailyQuest> findByMemberAndDate(@Param("memberId") Long memberId, @Param("date") LocalDate date);
```

### 3. 도메인별 책임 분리
- **도메인별 EventListener**: 각 도메인은 자신의 이벤트만 처리
  - `NotificationEventListener` → 알림 생성
  - `ExperienceEventListener` → 경험치 지급
  - `CollectionEventListener` → 컬렉션 해금
  - `OnboardingEventListener` → 온보딩 미션 진행
- **단일 책임 원칙**: 하나의 클래스는 하나의 책임만

### 4. 예외 처리 전략
- **도메인별 Exception**: `CollectionException`, `StudyException` 등 도메인별 예외 정의
- **ErrorCode Enum**: HTTP 상태, 에러 코드, 메시지 관리
- **Global Exception Handler**: `@RestControllerAdvice`로 일관된 에러 응답

## 🚀 실행 방법

### 필수 환경
- Java 21
- MySQL 8.0 이상
- AWS S3 버킷 (파일 업로드용)
- Google Gemini API Key

### 빌드 & 실행
```bash
# 빌드
./gradlew build

# 테스트
./gradlew test

# 실행
./gradlew bootRun
```

### 접속
- **API Server**: http://localhost:8080
- **Admin Dashboard**: http://localhost:8080/admin

## 📊 주요 API 엔드포인트

### 인증
- `POST /api/auth/complete-registration` - 회원가입 완료 (OAuth 이후)
- `GET /api/auth/me` - 내 정보 조회

### 스터디
- `GET /api/studies` - 스터디 목록 조회
- `POST /api/studies` - 스터디 생성
- `POST /api/studies/join` - 스터디 참여 (초대 코드)

### 과제
- `GET /api/studies/{studyId}/assignments` - 과제 목록
- `POST /api/studies/{studyId}/assignments` - 과제 생성
- `POST /api/submissions` - 과제 제출

### 게이미피케이션
- `GET /api/collections` - 내 컬렉션 도감
- `POST /api/collections/equip` - 프로필 아이콘 변경
- `GET /api/notifications` - 알림 목록
- `GET /api/notifications/daily-quests` - 일일 퀘스트 진행 상황

---
