#!/bin/bash

mvn clean install assembly:single
rm ./target/ExtraEnchants-1.0-SNAPSHOT-jar-with-dependencies.jar
mv ./target/ExtraEnchants-1.0-SNAPSHOT.jar ./target/ExtraEnchants.jar
cp ./target/ExtraEnchants.jar ../../MinecraftServer/plugins/
