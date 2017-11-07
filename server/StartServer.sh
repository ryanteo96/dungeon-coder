#!/bin/bash
pkill java
export CLASSPATH=~/dungeon-coder/server/mysql-connector-java-5.1.44-bin.jar:$CLASSPATH
javac Server.java ServerThread.java
java Server&
