# 사용할 Java 런타임 이미지를 선택합니다.
FROM openjdk:21-jre-slim

# 컨테이너 내에서 애플리케이션이 실행될 디렉토리를 설정합니다.
WORKDIR /app

# 빌드 결과물인 JAR 파일을 컨테이너 내부의 /app 디렉토리로 복사합니다.
# 실제 JAR 파일 이름은 워크플로우의 artifact 이름과 빌드 결과에 따라 달라질 수 있습니다.
COPY build/libs/*.jar app.jar

# 컨테이너가 시작될 때 실행할 명령어를 정의합니다.
# spring-boot-maven-plugin 또는 spring-boot-gradle-plugin을 사용하는 경우
# 실행 가능한 JAR 파일이 생성되므로 java -jar 명령어를 사용할 수 있습니다.
ENTRYPOINT ["java", "-jar", "app.jar"]

# (선택 사항) 애플리케이션이 사용하는 포트를 노출합니다.
EXPOSE 8080
