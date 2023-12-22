#!/bin/sh

DIRECTORY="/home/nova/template/api/"
FILENAME="loginJwtRSA-0.0.1-SNAPSHOT.jar"
PROFILE="develop"

java -version

echo USER : ${whoami}
echo WORKSPACE : ${WORKSPACE}
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

FILEPATH=${DIRECTORY}${FILENAME}
rm -rf ${FILEPATH}
cp -r ${WORKSPACE}/build/libs/${FILENAME} ${FILEPATH}

sleep 1

echo Start template process

OPT="-Dspring.profiles.active=${PROFILE}"

java -jar ${OPT} "${FILEPATH}" &
