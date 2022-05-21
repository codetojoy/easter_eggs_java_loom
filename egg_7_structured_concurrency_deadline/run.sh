#!/bin/bash

set -e

ROOT_DIR=$PWD
SRC_DIR=$ROOT_DIR/src/main/java
TARGET_DIR=$ROOT_DIR/my_build/main
CLASSPATH=$CLASSPATH:$TARGET_DIR

java --enable-preview \
-cp $CLASSPATH \
--add-modules jdk.incubator.concurrent \
net.codetojoy.Runner

echo "run complete"
