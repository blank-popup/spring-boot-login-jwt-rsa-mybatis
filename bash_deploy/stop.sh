#!/usr/bin/bash

JAVA_HOME=$1
COMMAND_JAVA=$2
WORKSPACE=$3
NAME_PROJECT=$4
DIRECTORY=$5
FILENAME=$6
VALUE_PROFILE=$7
FILEPATH=${DIRECTORY}${FILENAME}
KEY_PROFILE="spring.profiles.active"
OPTION="-D${KEY_PROFILE}=${VALUE_PROFILE}"

${COMMAND_JAVA} -version

echo USER : $USER
echo JAVA_HOME : ${JAVA_HOME}
echo COMMAND_JAVA : ${COMMAND_JAVA}
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
