#!/bin/sh

DIRECTORY="/home/nova/template/api/"
FILENAME="loginJwtRSA-0.0.1-SNAPSHOT.jar"
PROFILE="develop"

echo WORKSPACE : $1
echo DIRECTORY : ${DIRECTORY}
echo FILENAME : ${FILENAME}
echo PROFILE : ${PROFILE}

echo Terminate template process

PID=`ps -ef | grep ${FILENAME} | grep -v grep | awk '{print $2}'`
echo PID : ${PID}

if [ -n "${PID}" ]
then
    result1=$(kill -9 ${PID})
    echo process is killed.
else
    echo running process not found.
fi

sleep 1

echo copy template file

rm -rf ${DIRECTORY}bin/${FILENAME}
cp -r $1/build/libs/${FILENAME} ${DIRECTORY}bin/${FILENAME}

sleep 1

echo Start template process

BIN=${DIRECTORY}bin/${FILENAME}
OPT="-Dspring.profiles.active=${PROFILE}"

java -jar ${OPT} "${BIN}" &
