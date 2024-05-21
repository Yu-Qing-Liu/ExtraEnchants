#!/bin/bash

./gradlew clean shadowJar
cp ./build/libs/* ../../MinecraftTestServer/plugins/ExtraEnchants.jar
