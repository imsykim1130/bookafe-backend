FROM openjdk:21-jdk as build
WORKDIR /app
COPY build/libs/*.jar ./app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build app/dependencies/ .
COPY --from=build app/spring-boot-loader/ .
COPY --from=build app/snapshot-dependencies/ .
COPY --from=build app/application/ .

CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]