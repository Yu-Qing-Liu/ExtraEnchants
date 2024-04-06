#!/bin/bash

mvn clean install compile assembly:single
mv ./target/ExtraEnchants-1.0-SNAPSHOT-jar-with-dependencies.jar ./target/ExtraEnchants.jar
cp ./target/ExtraEnchants.jar ../../MinecraftServer/plugins/
