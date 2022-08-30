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

check-updates:
	./gradlew dependencyUpdates

.PHONY: build