#!/bin/bash
java -jar -Dlog.path.prefix=/opt/apps/logs  temail-channel-server-1.0.0-SNAPSHOT.jar --spring.profiles.active=frontend 2>&1 1 > /dev/null &

