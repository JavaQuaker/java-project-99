#ci:
#    docker-compose -f docker-compose.yml run app make setup
#    docker-compose -f docker-compose.yml up --abort-on-container-exit
#
#compose-setup: compose-build compose-app-setup
#
#compose-build:
#    docker-compose build
#
#compose-app-setup:
#    docker-compose run --rm app make setup
#
#compose-bash:
#    docker-compose run --rm --service-ports app bash
#
#compose-lint:
#    docker-compose run --rm app make lint
#
#compose-test:
#    docker-compose -f docker-compose.yml up --abort-on-container-exit
#
#compose:
#    docker-compose up
#
#compose-down:
#    docker-compose down -v --remove-orphans
#
#setup:
##    cd code/app && ./gradlew clean build
#    ./gradlew clean build
#    gradle clean compileTest
#
#test:
#    gradle test
#
#lint:
#    gradle checkstyleTest checkCode
#
#code-start:
#    make/app -C code start
#
#check-updates:
#    gradle dependencyUpdates
#
#deploy:
#    git subtree push --prefix code heroku main
setup:
	./gradlew wrapper --gradle-version 8.4

clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew installDist

start-dist:
	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

generate-migrations:
	./gradlew diffChangeLog

db-migrate:
	./gradlew update

.PHONY: build