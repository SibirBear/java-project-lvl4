clean:
	/app/gradlew clean

build:
	/app/gradlew clean build

install:
	/app/gradlew install

start:
    APP_ENV=development /app/gradlew run

lint:
	/app/gradlew checkstyleMain checkstyleTest

test:
	/app/gradlew test

report:
	/app/gradlew jacocoTestReport

check-updates:
	/app/gradlew dependencyUpdates

.PHONY: build