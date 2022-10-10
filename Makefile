clean:
	make -C app clean

build:
	make -C app build

install:
	make -C app install

start:
	make -C app start

lint:
	make -C app lint

test:
	make -C app test

report:
	make -C app report

check-updates:
	make -C app check-updates

.PHONY: build test