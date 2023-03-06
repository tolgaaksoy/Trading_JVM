#!/bin/bash

#variables
MAIN_CLASS="main.java.BitvavoApplication" # main class
JAR_NAME="bitvavo.jar" #  jar name
SRC_DIR="src" # source directory
BUILD_DIR="build" #  build directory
MANIFEST_FILE="manifest.mf" #  manifest file name

# Create build directory if it doesn't exist
mkdir -p $BUILD_DIR

# Compile Java source code
javac -d $BUILD_DIR -cp $SRC_DIR $SRC_DIR/main/java/*.java

# Create manifest file
echo "Main-Class: $MAIN_CLASS" > $BUILD_DIR/$MANIFEST_FILE

# Create jar file
jar cfm $JAR_NAME $BUILD_DIR/$MANIFEST_FILE -C $BUILD_DIR .

# Clean up
rm -rf $BUILD_DIR
rm -rf out