FROM java:8-jdk-alpine
COPY ./target/* /srv/
COPY ./bin/* /srv/
COPY src/main/resources/schema/rjs1/generic.json /srv/src/main/resources/schema/rjs1/generic.json
COPY src/main/resources/schema/wpcompact/ /srv/src/main/resources/schema/wpcompact/
WORKDIR /srv
