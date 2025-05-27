# 1단계: 빌드 스테이지 (Gradle로 JAR 빌드)
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

COPY --chown=gradle:gradle . .

RUN gradle build --no-daemon

# 2단계: 실행 스테이지 (JRE만 포함된 슬림 이미지)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
