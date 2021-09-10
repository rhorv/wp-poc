FROM ubuntu
RUN apt-get update && apt-get install -y make maven
RUN cd /tmp && apt-get update && apt-get install -y wget default-jre-headless && wget https://downloads.apache.org/kafka/2.8.0/kafka_2.13-2.8.0.tgz && tar xvf kafka_2.13-2.8.0.tgz
COPY ./ /srv/
WORKDIR /srv
