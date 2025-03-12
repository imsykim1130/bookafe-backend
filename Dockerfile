# 빌드
FROM openjdk:21-jdk-slim as build
WORKDIR /app
# gradle 캐시 최적화를 위해 필요한 파일들만 먼저 복사
COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
RUN chmod +x gradlew
RUN ./gradlew dependencies

# 전체 프로젝트 복사 및 빌드
COPY . .
# jar 파일만 필요하면 bootJar
#RUN ./gradlew bootJar
# 코드 컴파일, 테스트, jar 생성 한 번에 하고 싶으면 build
RUN ./gradlew clean build
#RUN ./gradlew build -x test # 테스트 불포함

# 실행
FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]