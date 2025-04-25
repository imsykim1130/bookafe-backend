FROM openjdk:21-jdk-slim as build
WORKDIR /app

COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
RUN chmod +x gradlew
RUN ./gradlew dependencies

# 전체 프로젝트 복사 및 빌드
COPY . .
# jar 파일만 필요하면 bootJar
#RUN ./gradlew bootJar
# 코드 컴파일, 테스트, jar 생성 한 번에 하고 싶으면 build
# 테스트 불포함
RUN ./gradlew clean build -x test

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]