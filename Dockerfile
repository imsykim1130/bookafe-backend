FROM openjdk:21-jdk-bullseye
WORKDIR /app

# RUN apt-get update && apt-get install -y findutils

COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
RUN chmod +x gradlew
RUN ./gradlew dependencies

# COPY . .
RUN ./gradlew clean build -x test

EXPOSE 8080
CMD ["./gradlew", "bootRun", "--continuous"]