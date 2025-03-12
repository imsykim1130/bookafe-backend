## 1. gradle 로 빌드
#FROM gradle:8.5-jdk21 as build
#WORKDIR /app
#
## gradle 캐시 최적화를 위해 필요한 파일들만 먼저 복사
#COPY gradle gradle
#COPY build.gradle settings.gradle gradlew ./
#RUN chmod +x gradlew
#RUN ./gradlew dependencies
#
## 전체 프로젝트 복사 및 빌드
#COPY . .
## jar 파일만 필요하면 bootJar
##RUN ./gradlew bootJar
#
## 코드 컴파일, 테스트, jar 생성 한 번에 하고 싶으면 build
#RUN ./gradlew build
##RUN ./gradlew build -x test # 테스트 불포함

# springboot layer 로 최적화
FROM openjdk:21-jdk as build
WORKDIR /app
COPY /build/libs/*.jar ./app.jar
RUN java -Djarmode=layertools -jar app.jar extract


# 실행할 컨테이너(크기를 줄이기 위해 slim 이미지 사용)
FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build app/dependencies/ .
COPY --from=build app/spring-boot-loader/ .
COPY --from=build app/snapshot-dependencies/ .
COPY --from=build app/application/ .

EXPOSE 8080

CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]