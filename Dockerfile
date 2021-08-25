FROM java:8-jdk-alpine
COPY ./target/* /srv/
COPY ./bin/* /srv/
WORKDIR /srv
