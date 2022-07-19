
install:
	./gradlew clean install

run-dist:
	./build/install/app/bin/app

build:
	./gradlew clean build

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

.PHONY: build