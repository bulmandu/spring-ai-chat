# Week4 - Spring AI Chat Application

OpenAI 호환 API를 제공하는 Spring AI 기반 채팅 애플리케이션입니다. Anthropic Claude와 Ollama 모델을 지원하며, Open-WebUI를 통한 웹 인터페이스를 제공합니다.

## 주요 기능

- OpenAI 호환 Chat Completions API (`/v1/chat/completions`)
- Stream 및 Sync 채팅 모드 지원
- 다중 AI 모델 지원 (Claude, Ollama)
- Open-WebUI 웹 인터페이스 통합
- Docker Compose를 통한 간편한 배포

## 기술 스택

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring AI 1.0.0**
- **Spring WebFlux** (Stream 채팅)
- **Lombok**
- **Gradle**
- **Docker & Docker Compose**

## 지원 AI 모델

### 1. Anthropic Claude
- Model: `claude-sonnet-4-5-20250929`
- Temperature: `0.7`
- Max Tokens: `4096`

### 2. Ollama
- Model: `qwen2.5:3b`
- Temperature: `0.7`
- Max Predict: `1000`

## 프로젝트 구조

```
src/main/java/com/sparta/week4/
├── AiChatController.java              # REST API 컨트롤러
├── Week4Application.java              # 메인 애플리케이션
├── common/
│   └── ChatClientProvider.java        # Chat Client 제공자
├── config/
│   ├── AnthropicConfig.java           # Anthropic 설정
│   ├── OllamaConfig.java              # Ollama 설정
│   └── OllamaProperties.java          # Ollama 프로퍼티
├── converter/
│   └── ChatMessageConverter.java      # 메시지 변환기
├── dto/
│   ├── ChatCompletionRequest.java     # 채팅 요청 DTO
│   ├── ChatCompletionResponse.java    # 채팅 응답 DTO
│   ├── ChatModelResponse.java         # 모델 목록 응답 DTO
│   ├── ModelData.java                 # 모델 데이터 DTO
│   ├── Message.java                   # 메시지 DTO
│   ├── MessageResponse.java           # 메시지 응답 DTO
│   ├── Choice.java                    # 선택지 DTO
│   └── Usage.java                     # 토큰 사용량 DTO
├── enums/
│   └── ChatClientModelType.java       # 모델 타입 enum
└── service/
    ├── AiChatService.java             # AI 채팅 서비스 인터페이스
    ├── MyAiChatService.java           # AI 채팅 서비스 구현
    └── ChatModelService.java          # 모델 조회 서비스
```

## 환경 설정

### 1. `.env` 파일 생성

프로젝트 루트에 `.env` 파일을 생성하고 Anthropic API 키를 추가합니다:

```env
ANTHROPIC_API_KEY=sk-ant-api03-xxxxxxxxxxxxx
```

### 2. `application.yml` 설정

```yaml
spring:
  application:
    name: week4

  config:
    import: optional:file:.env[.properties]

  ai:
    anthropic:
      api-key: ${ANTHROPIC_API_KEY}
      chat:
        options:
          model: claude-sonnet-4-5-20250929
          temperature: 0.7
          max-tokens: 4096
    ollama:
      base-url: http://ollama:11434  # Docker 환경
      chat:
        options:
          model: qwen2.5:3b
          temperature: 0.7
          num-predict: 1000

logging:
  level:
    org.springframework.ai: DEBUG
```

## 실행 방법

### Docker Compose로 전체 스택 실행 (권장)

```bash
# 모든 서비스 시작 (Ollama, Spring-AI, Open-WebUI)
docker-compose up -d

# 컨테이너 상태 확인
docker-compose ps

# Ollama 모델 다운로드
docker exec -it ollama ollama pull qwen2.5:3b

# 로그 확인
docker-compose logs -f

# Open-WebUI 접속
# http://localhost:3000
```

### 로컬 개발 환경 실행

```bash
# 1. Ollama만 Docker로 실행
docker-compose up -d ollama

# 2. Ollama 모델 다운로드
docker exec -it ollama ollama pull qwen2.5:3b

# 3. Spring Boot 애플리케이션 실행
./gradlew bootRun
```

### 종료

```bash
# 모든 컨테이너 중지
docker-compose down

# 볼륨까지 모두 삭제
docker-compose down -v
```

## API 사용법

### 1. 사용 가능한 모델 목록 조회

```bash
GET /v1/chat/completions
```

**Response:**
```json
{
  "object": "list",
  "data": [
    {
      "id": "claude",
      "object": "model",
      "created": 1234567890,
      "owned_by": "anthropic"
    },
    {
      "id": "ollama",
      "object": "model",
      "created": 1234567890,
      "owned_by": "ollama"
    }
  ]
}
```

### 2. 채팅 완료 (Sync Mode)

```bash
POST /v1/chat/completions
Content-Type: application/json
```

**Request:**
```json
{
  "model": "claude",
  "messages": [
    {
      "role": "system",
      "content": "You are a helpful assistant."
    },
    {
      "role": "user",
      "content": "안녕하세요!"
    }
  ],
  "temperature": 0.7,
  "maxTokens": 500,
  "stream": false
}
```

**Response:**
```json
{
  "id": "chatcmpl-abc123",
  "object": "chat.completion",
  "created": 1234567890,
  "model": "claude",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "안녕하세요! 무엇을 도와드릴까요?"
      },
      "finishReason": "stop"
    }
  ],
  "usage": {
    "promptTokens": 15,
    "completionTokens": 20,
    "totalTokens": 35
  }
}
```

### 3. 채팅 완료 (Stream Mode)

```bash
POST /v1/chat/completions
Content-Type: application/json
```

**Request:**
```json
{
  "model": "ollama",
  "messages": [
    {
      "role": "user",
      "content": "안녕하세요!"
    }
  ],
  "stream": true
}
```

**Response:** (Server-Sent Events)
```
data: {"id":"chatcmpl-123","object":"chat.completion.chunk","created":1234567890,"model":"ollama","choices":[{"index":0,"delta":{"role":"assistant","content":"안녕"},"finish_reason":null}]}

data: {"id":"chatcmpl-123","object":"chat.completion.chunk","created":1234567890,"model":"ollama","choices":[{"index":0,"delta":{"content":"하세요"},"finish_reason":null}]}

data: {"id":"chatcmpl-123","object":"chat.completion.chunk","created":1234567890,"model":"ollama","choices":[{"index":0,"delta":{},"finish_reason":"stop"}]}

data: [DONE]
```

## Open-WebUI 사용법

1. Docker Compose로 전체 스택 실행
2. 브라우저에서 `http://localhost:3000` 접속
3. 초기 계정 생성 (첫 사용자가 관리자가 됨)
4. 설정에서 모델 선택 (claude 또는 ollama)
5. 채팅 시작

## 개발 가이드

### 빌드

```bash
# 프로젝트 빌드
./gradlew build

# 테스트 실행
./gradlew test

# 클린 빌드
./gradlew clean build
```

### Docker 이미지 빌드

```bash
# Spring Boot 애플리케이션 Docker 이미지 빌드
docker build -t spring-ai:latest .
```

## 트러블슈팅

### Ollama 연결 실패
```bash
# Ollama 컨테이너 상태 확인
docker-compose logs ollama

# Ollama 서비스 재시작
docker-compose restart ollama
```

### 모델 다운로드 확인
```bash
# Ollama 컨테이너 내부에서 모델 목록 확인
docker exec -it ollama ollama list
```

### 네트워크 문제
```bash
# Docker 네트워크 확인
docker network ls
docker network inspect week4_chatbot-network
```

## 주요 API 엔드포인트

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/chat/completions` | 사용 가능한 모델 목록 조회 |
| POST | `/v1/chat/completions` | 채팅 완료 (Sync/Stream) |

## 보안 주의사항

- `.env` 파일은 `.gitignore`에 포함되어 Git에 커밋되지 않습니다
- Anthropic API 키는 절대 공개하지 마세요
- 프로덕션 환경에서는 적절한 인증/인가 메커니즘을 추가하세요

## 라이선스

이 프로젝트는 학습 목적으로 제작되었습니다.