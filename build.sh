#!/bin/sh

./gradlew clean build
cp ./build/libs/* ../Server/plugins/ExtraEnchants.jar
