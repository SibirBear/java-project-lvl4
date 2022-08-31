clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew install

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

.PHONY: build