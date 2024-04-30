#!/bin/bash

cd ../
./gradlew clean build
mv ./plugin/build/libs/ExtraEnchants-1.0.jar ./plugin/build/libs/ExtraEnchants.jar
cp ./plugin/build/libs/ExtraEnchants.jar ../../MinecraftTestServer/plugins/
