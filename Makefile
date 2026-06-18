.PHONY: build run test clean d-build d-run d-stop d-log

APP_NAME=idempotent-consumer-batch-api-java17
DOCKER_IMAGE=stochasticlabs/idempotent-consumer-batch-api-java17:1.0.0
PORT=8082
NETWORK=stochastic-labs-infra_stochastic-network
ENV_FILE=.env

build:
	mvn clean package -DskipTests

test:
	mvn test

run:
	mvn spring-boot:run

clean:
	mvn clean

d-build:
	docker build -t $(DOCKER_IMAGE) .

d-run:
	docker run -d -p $(PORT):$(PORT) --network $(NETWORK) --env-file $(ENV_FILE) --name $(APP_NAME) $(DOCKER_IMAGE)

d-stop:
	@docker stop $(APP_NAME) 2>/dev/null || true
	@docker rm $(APP_NAME) 2>/dev/null || true

d-log:
	docker logs -f --tail 100 $(APP_NAME)

reload: d-stop d-build d-run d-log