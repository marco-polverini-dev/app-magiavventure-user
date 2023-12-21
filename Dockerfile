#
# BUILD STAGE
#
FROM gradle:8.3.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

#
# PACKAGE STAGE
#
FROM amazoncorretto:17
COPY --from=build /home/gradle/src/build/libs/app-magiavventure-user-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]