#!/bin/bash

./gradlew clean uberJar
cp ./build/libs/* ../../MinecraftTestServer/plugins/ExtraEnchants.jar
