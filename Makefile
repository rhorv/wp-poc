-include docker/postgres-pricing/.env
-include docker/postgres-billing/.env
-include docker/postgres-clearing/.env
-include docker/postgres-funding/.env
-include docker/postgres-onboarding/.env
-include docker/kafka/.env
-include docker/elk/.env

SHELL := /bin/bash

build: ## Builds this project [Default]
	mvn compile && mvn package

check-deps: ## Checks the required dependecies
	@printf "checking for java ... ";
	@if [ "`which java`" != "" ]; then echo "found"; else echo "missing"; fi

	@printf "checking for maven ... ";
	@if [ "`which mvn`" != "" ]; then echo "found"; else echo "missing"; fi

	@printf "checking for docker ... ";
	@if [ "`which docker`" != "" ]; then echo "found"; else echo "missing"; fi

	@printf "checking for docker-compose ... ";
	@if [ "`which docker-compose`" != "" ]; then echo "found"; else echo "missing"; fi

	@printf "checking for docker permissions ... ";
	@if [ "`docker ps | grep 'CONTAINER'`" != "" ]; then echo "pass"; else echo "fail"; fi

	@printf "checking for jq ... ";
	@if [ "`which jq`" != "" ]; then echo "found"; else echo "missing"; fi

init: docker-init ## Initializes the project

docker-init: ## Initializes the docker network adapter and creates required directories
	@if [ "`docker network ls | grep poc-wp`" == "" ]; then docker network create poc-wp; else echo "Docker network already exists"; fi
	mkdir -p `grep 'KAFKA_HOME' docker/kafka/.env | cut -d "=" -f 2`;
	mkdir -p `grep 'ELK_HOME' docker/elk/.env | cut -d "=" -f 2`/elasticsearch-data;

kafka-setup: ## Initializes the kafka topics
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic clearing-payment --bootstrap-server localhost:9092
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic clearing-internal --bootstrap-server localhost:9092
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic pricing-payment --bootstrap-server localhost:9092
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic pricing-internal --bootstrap-server localhost:9092
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic billing-payment --bootstrap-server localhost:9092
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic billing-internal --bootstrap-server localhost:9092
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic funding-payment --bootstrap-server localhost:9092
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic funding-internal --bootstrap-server localhost:9092
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic onboarding-merchant --bootstrap-server localhost:9092
	docker exec -it kafka /bin/bash /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic onboarding-internal --bootstrap-server localhost:9092

kafka-start: ## Starts the kafka docker service
	@export PWD=`pwd`
	@cd ${PWD}/docker/kafka; docker-compose up -d
	@cd ${PWD}

kafka-stop: ## Stops the kafka docker stack
	@export PWD=`pwd`;
	@cd ${PWD}/docker/kafka; docker-compose down;
	@cd ${PWD}

elk-start: ## Starts the ELK stack docker service
	@export PWD=`pwd`;
	@cd ${PWD}/docker/elk; docker-compose up -d;
	@cd ${PWD}

elk-stop: ## Stops the ELK stack docker service
	@export PWD=`pwd`;
	@cd ${PWD}/docker/elk; docker-compose down;
	@cd ${PWD}

clearing-db-start: ## Starts the postgres clearing db docker service
	@export PWD=`pwd`
	@cd ${PWD}/docker/postgres-clearing; docker-compose up -d
	@cd ${PWD}

clearing-db-stop: ## Stops the postgres clearing db docker stack
	@export PWD=`pwd`;
	@cd ${PWD}/docker/postgres-clearing; docker-compose down;
	@cd ${PWD}

pricing-db-start: ## Starts the postgres pricing db docker service
	@export PWD=`pwd`
	@cd ${PWD}/docker/postgres-pricing; docker-compose up -d
	@cd ${PWD}

pricing-db-stop: ## Stops the postgres pricing db docker stack
	@export PWD=`pwd`;
	@cd ${PWD}/docker/postgres-pricing; docker-compose down;
	@cd ${PWD}

billing-db-start: ## Starts the postgres billing db docker service
	@export PWD=`pwd`
	@cd ${PWD}/docker/postgres-billing; docker-compose up -d
	@cd ${PWD}

billing-db-stop: ## Stops the postgres billing db docker stack
	@export PWD=`pwd`;
	@cd ${PWD}/docker/postgres-billing; docker-compose down;
	@cd ${PWD}

funding-db-start: ## Starts the postgres funding db docker service
	@export PWD=`pwd`
	@cd ${PWD}/docker/postgres-funding; docker-compose up -d
	@cd ${PWD}

funding-db-stop: ## Stops the postgres funding db docker stack
	@export PWD=`pwd`;
	@cd ${PWD}/docker/postgres-funding; docker-compose down;
	@cd ${PWD}

onboarding-db-start: ## Starts the postgres onboarding db docker service
	@export PWD=`pwd`
	@cd ${PWD}/docker/postgres-onboarding; docker-compose up -d
	@cd ${PWD}

onboarding-db-stop: ## Stops the postgres onboarding db docker stack
	@export PWD=`pwd`;
	@cd ${PWD}/docker/postgres-onboarding; docker-compose down;
	@cd ${PWD}

clearing-db-migrate: ## Runs migration for the clearing db
	mvn clean flyway:migrate -Dflyway.locations=filesystem:src/main/resources/db/clearing/migration -Dflyway.user=${PG_CLEARING_USER} -Dflyway.password=${PG_CLEARING_PASS} -Dflyway.url=jdbc:postgresql://${PG_CLEARING_HOST}:${PG_CLEARING_PORT}/${PG_CLEARING_DB}

pricing-db-migrate: ## Runs migration for the pricing db
	mvn clean flyway:migrate -Dflyway.locations=filesystem:src/main/resources/db/pricing/migration -Dflyway.user=${PG_PRICING_USER} -Dflyway.password=${PG_PRICING_PASS} -Dflyway.url=jdbc:postgresql://${PG_PRICING_HOST}:${PG_PRICING_PORT}/${PG_PRICING_DB}

billing-db-migrate: ## Runs migration for the billing db
	mvn clean flyway:migrate -Dflyway.locations=filesystem:src/main/resources/db/billing/migration -Dflyway.user=${PG_BILLING_USER} -Dflyway.password=${PG_BILLING_PASS} -Dflyway.url=jdbc:postgresql://${PG_BILLING_HOST}:${PG_BILLING_PORT}/${PG_BILLING_DB}

funding-db-migrate: ## Runs migration for the funding db
	mvn clean flyway:migrate -Dflyway.locations=filesystem:src/main/resources/db/funding/migration -Dflyway.user=${PG_FUNDING_USER} -Dflyway.password=${PG_FUNDING_PASS} -Dflyway.url=jdbc:postgresql://${PG_FUNDING_HOST}:${PG_FUNDING_PORT}/${PG_FUNDING_DB}

onboarding-db-migrate: ## Runs migration for the onboarding db
	mvn clean flyway:migrate -Dflyway.locations=filesystem:src/main/resources/db/onboarding/migration -Dflyway.user=${PG_ONBOARDING_USER} -Dflyway.password=${PG_ONBOARDING_PASS} -Dflyway.url=jdbc:postgresql://${PG_ONBOARDING_HOST}:${PG_ONBOARDING_PORT}/${PG_ONBOARDING_DB}

db-migrate: clearing-db-migrate pricing-db-migrate billing-db-migrate funding-db-migrate onboarding-db-migrate ## Runs all db migrations

start-services: kafka-start elk-start  ## Starts all dockerized services

stop-services: elk-stop kafka-stop ## Stops all dockerized services

start-app-services: clearing-db-start pricing-db-start billing-db-start funding-db-start onboarding-db-start ## Starts all dockerized app services

stop-app-services: clearing-db-stop pricing-db-stop billing-db-stop funding-db-stop onboarding-db-stop ## Stops all dockerized app services

test: ## Runs the tests
	mvn test

container: clean build ## Builds the docker container for this project
	docker build -t wp-poc -f docker/app/Dockerfile .

container-db-infra: clean ## Builds the docker container for db infra for this project
	docker build -t wp-poc-db-infra -f docker/app-db-infra/Dockerfile .

clean: ## Cleans the project
	mvn clean

help: ## Prints this help
	@echo 'Usage: make <target>'
	@echo ''
	@echo 'Valid targets:'
	@awk -F ':|##' '/^[^\t].+?:.*?##/ {\
	printf "\033[36m%-30s\033[0m %s\n", $$1, $$NF \
	}' $(MAKEFILE_LIST)
