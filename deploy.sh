#!/bin/sh

WORKSPACE=$1
NAME_PROJECT="template"
DIRECTORY="/home/JENKINS/template/api/"
FILENAME="loginJwtRSA-0.0.1-SNAPSHOT.jar"
VALUE_PROFILE="develop"
FILEPATH=${DIRECTORY}${FILENAME}
KEY_PROFILE="spring.profiles.active"
OPTION="-D${KEY_PROFILE}=${VALUE_PROFILE}"

java -version

echo USER : $USER
echo WORKSPACE : ${WORKSPACE}
echo NAME_PROJECT : ${NAME_PROJECT}
echo DIRECTORY : ${DIRECTORY}
echo FILENAME : ${FILENAME}
echo VALUE_PROFILE : ${VALUE_PROFILE}
echo FILEPATH : ${FILEPATH}
echo KEY_PROFILE : ${KEY_PROFILE}
echo OPTION : ${OPTION}

echo ========== Terminating ${NAME_PROJECT} process ==========

PID=`ps -ef | grep ${FILEPATH} | grep ${KEY_PROFILE}=${VALUE_PROFILE} | grep -v grep | awk '{print $2}'`
echo PID : ${PID}

if [ -n "${PID}" ]
then
    kill -9 ${PID}
    echo process is killed.
else
    echo running process not found.
fi

echo ========== Terminated ${NAME_PROJECT} process ==========

sleep 1

echo ========== copying ${NAME_PROJECT} file ==========

rm -rf ${FILEPATH}
echo removed previous ${NAME_PROJECT} file
cp -r ${WORKSPACE}/build/libs/${FILENAME} ${FILEPATH}
echo copied new ${NAME_PROJECT} file

echo ========== copied ${NAME_PROJECT} file ==========

sleep 1

echo ========== Starting ${NAME_PROJECT} process ==========

java -jar ${OPTION} "${FILEPATH}" &

echo ========== Started ${NAME_PROJECT} process ==========
