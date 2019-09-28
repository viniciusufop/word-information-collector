#!/bin/sh

echo "********************************************************"
echo "Waiting for the mongodb server to start on port 27017"
echo "********************************************************"
while ! `nc -z word-mongodb 27017`; do sleep 3; done
echo ">>>>>>>>>>>> MongoDB Server has started"

java -Dserver.port=$SERVER_PORT                                 \
     -Dfile.encoding=UTF-8       \
     -jar /usr/local/app/word-information-collector.jar