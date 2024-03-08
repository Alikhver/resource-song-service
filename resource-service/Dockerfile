FROM amazoncorretto:17.0.9-al2023-headless
WORKDIR /app
COPY build/libs/app.jar /app/app.jar
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000", "-jar", "app.jar"]