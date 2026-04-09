# 1단계 빌드 환경
FROM amazoncorretto:17 AS builder

WORKDIR /app

COPY discodeit/gradlew .
COPY discodeit/gradle gradle
COPY discodeit/build.gradle .
COPY discodeit/settings.gradle .


RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

COPY discodeit/ .
RUN ./gradlew clean build -x test --no-daemon

# 2단계 러닝 환경
FROM amazoncorretto:17-alpine

WORKDIR /app
ARG PROJECT_NAME
ARG PROJECT_VERSION
ENV PROJECT_NAME=${PROJECT_NAME}
ENV PROJECT_VERSION=${PROJECT_VERSION}
ENV JVM_OPTS=""

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar app.jar"]