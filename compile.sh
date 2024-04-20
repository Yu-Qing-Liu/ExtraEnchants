#!/bin/bash

mvn clean install package
mv ./target/ExtraEnchants-1.0-SNAPSHOT.jar ./target/ExtraEnchants.jar
cp ./target/ExtraEnchants.jar ../../MinecraftTestServer/plugins/
cp ./target/ExtraEnchants.jar ../../PersonalMinecraftServer/plugins/
