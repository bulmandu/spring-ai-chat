# Week4 - Spring AI Chat Application

Spring AI를 활용한 AI 채팅 애플리케이션입니다. Anthropic Claude와 Ollama 모델을 지원합니다.

## 기술 스택

- **Java 17**
- **Spring Boot 4.0.0**
- **Spring AI 1.1.0-M2**
- **Lombok**
- **Gradle**

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
├── ChatAiController.java           # REST API 컨트롤러
├── Week4Application.java           # 메인 애플리케이션
├── config/
│   ├── AnthropicConfig.java        # Anthropic 설정
│   ├── OllamaConfig.java           # Ollama 설정
│   └── OllamaProperties.java       # Ollama 프로퍼티
└── dto/
    ├── ChatCompletionRequest.java  # 채팅 요청 DTO
    ├── ChatCompletionResponse.java # 채팅 응답 DTO
    ├── Message.java                # 메시지 DTO
    ├── MessageResponse.java        # 메시지 응답 DTO
    ├── Choice.java                 # 선택지 DTO
    └── Usage.java                  # 토큰 사용량 DTO
```

## 환경 설정

### 1. `.env` 파일 생성

프로젝트 루트에 `.env` 파일을 생성하고 Anthropic API 키를 추가합니다:

```
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
      base-url: http://localhost:11434
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

### 1. Docker Compose로 Ollama 실행

```bash
# Ollama 컨테이너 시작
docker-compose up -d

# 컨테이너가 실행 중인지 확인
docker-compose ps

# Ollama 컨테이너에 접속하여 모델 다운로드
docker exec -it ollama ollama pull qwen2.5:3b

# 로그 확인 (선택사항)
docker-compose logs -f ollama
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 3. 종료

```bash
# Ollama 컨테이너 중지
docker-compose down

# 데이터까지 모두 삭제하려면
docker-compose down -v
```

## API 사용법

### Endpoint

```
POST /vi/chat/completions
```

### Request 예시

```json
{
  "model": "qwen3",
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

### Response 예시

```json
{
  "id": "chatcmpl-abc123",
  "object": "chat.completion",
  "created": 1234567890,
  "model": "qwen3",
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

## 빌드

```bash
# 빌드
./gradlew build

# 테스트
./gradlew test
```

## 주의사항

- `.env` 파일은 `.gitignore`에 포함되어 있으므로 Git에 커밋되지 않습니다
- Anthropic API 키는 절대 공개하지 마세요
- Ollama를 사용하려면 로컬에서 Ollama 서버가 실행 중이어야 합니다

## 라이선스

이 프로젝트는 학습 목적으로 제작되었습니다.