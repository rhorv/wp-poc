FROM java:8-jdk-alpine
COPY ./target/* /srv/
WORKDIR /srv
CMD ["java", "-jar", "/srv/gs-maven-0.1.0.jar"]
