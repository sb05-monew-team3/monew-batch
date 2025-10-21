# 📰 Monu 프로젝트
> MongoDB 및 PostgreSQL 백업/복구 기반 뉴스 통합 플랫폼

- **프로젝트 기간:** 2025.10.21 ~ 2025.11.15  
- **API 명세서:** [Swagger UI ↗](#)  
- **협업 문서:** [Notion ↗](#)  
- **커뮤니케이션:** [Discord 채널 ↗](#)  

---

## 📖 프로젝트 소개
Monu는 여러 뉴스 API를 통합하여 사용자 맞춤 뉴스를 제공하고, 사용자 활동 내역 및 의견을 기록/관리할 수 있는 플랫폼입니다.  
PostgreSQL과 MongoDB 기반으로 데이터를 안전하게 저장하고, Spring Batch로 뉴스 백업/수집을 자동화합니다.  
운영 및 모니터링은 Spring Actuator와 Prometheus를 활용하며, 대용량 데이터 처리와 안정성을 고려한 설계가 적용됩니다.  

---

## 👩🏻‍💻 팀원 구성

| 이름 | 역할 | GitHub |
|------|------|--------|
| 주세훈 | 팀장 / 백엔드 개발 | [GitHub](https://github.com/Jusehun) |
| 정기주 | 백엔드 개발 | [GitHub](https://github.com/jeonggiju) |
| 이성훈 | 백엔드 개발 | [GitHub](https://github.com/polodumbo) |
| 박지석 | 백엔드 개발 | [GitHub](https://github.com/commicat2) |
| 김용희 | 백엔드 개발 | [GitHub](https://github.com/backKim1024) |
| 민재영 | 백엔드/프론트엔드 개발 | [GitHub](https://github.com/minjaeyoung) |

---

## 🧩 기술 스택

### ⚙️ Backend
- Spring Boot
- MapStruct
- JPA

### 🗄 Database
- PostgreSQL
- MongoDB

### 🚀 Batch / Monitoring
- Spring Batch
- Spring Actuator

### 🤝 협업 Tool
- Git / GitHub
- Notion
- Discord

---

## 🗂️ Repository 구성

| Repo | 설명 |
|------|------|
| monu-mvc | 메인 API 서버 (Spring Boot MVC) |
| monu-batch | 뉴스 수집 및 백업 Batch 서비스 |
| monu-actuator | 모니터링 및 상태 관리 서비스 |

---

## 🚀 브랜치 전략
- `main`: 배포용  
- `develop`: 통합 개발 (추후)  
- `feature/*`: 기능 단위 개발  
  - 예시: `feature/news-api`, `feature/user-preference`, `feature/batch-backup`, `feature/actuator-monitor`  

---

## 📄 커밋 컨벤션
- feat: 새로운 기능 추가  
- fix: 버그 수정  
- refactor: 코드 리팩토링  
- test: 테스트 코드 추가  
- docs: 문서 변경  
- chore: 빌드, 설정 관련 작업  

---

## 📂 프로젝트 구조 (예시)

monu-mvc/  
├─ src/  
│   ├─ main/  
│   │   ├─ java/  
│   │   │   └─ com/monu/  
│   │   │       ├─ config/  
│   │   │       ├─ controller/  
│   │   │       ├─ dto/  
│   │   │       ├─ entity/  
│   │   │       ├─ repository/  
│   │   │       ├─ service/  
│   │   │       └─ util/  
│   │   └─ resources/  
│   │       └─ application.yml  
└─ test/  

---

## 📎 팀원별 구현 예정 기능

| 팀원 | 기능 |
|------|------|
| 민재영 | 뉴스 데이터 백업/복구, 파일 스트리밍 다운로드, 사용자 관심사 관리 |
| 주세훈 | API 서버 구조 설계, 팀 리딩, CI/CD 파이프라인 |
| 정기주 | Batch 스케줄링, 모니터링, 에러 처리 |
| 이성훈 | API 서버 개발, DB 쿼리 최적화 |
| 박지석 | 서비스 로직, 테스트 코드 작성 |
| 김용희 | 백업/복구 기능, 데이터 안정성 검증 |
