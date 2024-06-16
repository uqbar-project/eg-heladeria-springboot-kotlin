FROM gradle:8.8.0-jdk-21-and-22-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
ADD . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM amazoncorretto:21-alpine-jdk
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/heladeria-kotlin-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]