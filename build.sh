#!/bin/sh

./gradlew clean build
cp ./build/libs/* ../DevServer/plugins/ExtraEnchants.jar
