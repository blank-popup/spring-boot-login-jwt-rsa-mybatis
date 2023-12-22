#!/bin/sh

NAME_PROJECT="template"
DIRECTORY="/home/JENKINS/template/api/"
FILENAME="loginJwtRSA-0.0.1-SNAPSHOT.jar"
PROFILE="develop"

java -version

echo USER : $USER
echo WORKSPACE : ${WORKSPACE}
echo DIRECTORY : ${DIRECTORY}
echo FILENAME : ${FILENAME}
echo PROFILE : ${PROFILE}

echo Terminating ${NAME_PROJECT} process

PID=`ps -ef | grep ${FILENAME} | grep -v grep | awk '{print $2}'`
echo PID : ${PID}

if [ -n "${PID}" ]
then
    kill -9 ${PID}
    echo process is killed.
else
    echo running process not found.
fi
echo Terminated ${NAME_PROJECT} process

sleep 1

echo copying ${NAME_PROJECT} file

FILEPATH=${DIRECTORY}${FILENAME}
rm -rf ${FILEPATH}
echo removed previous ${NAME_PROJECT} file
cp -r ${WORKSPACE}/build/libs/${FILENAME} ${FILEPATH}
echo copied new ${NAME_PROJECT} file
echo copied ${NAME_PROJECT} file

sleep 1

echo Starting ${NAME_PROJECT} process

OPT="-Dspring.profiles.active=${PROFILE}"
echo java -jar ${OPT} "${FILEPATH}" "&"
java -jar ${OPT} "${FILEPATH}" &
echo Started ${NAME_PROJECT} process
