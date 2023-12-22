#!/bin/sh

WORKSPACE=$1
NAME_PROJECT=$2
DIRECTORY=$3
FILENAME=$4
FILEPATH=${DIRECTORY}${FILENAME}
PROFILE=$5

echo ========== Terminating ${NAME_PROJECT} process ==========

java -version

echo USER : $USER
echo WORKSPACE : ${WORKSPACE}
echo NAME_PROJECT : ${NAME_PROJECT}
echo DIRECTORY : ${DIRECTORY}
echo FILENAME : ${FILENAME}
echo FILEPATH : ${FILEPATH}
echo PROFILE : ${PROFILE}

PID=`ps -ef | grep ${FILENAME} | grep -v grep | awk '{print $2}'`
echo PID : ${PID}

if [ -n "${PID}" ]
then
    kill -9 ${PID}
    echo process is killed.
else
    echo running process not found.
fi

echo ========== Terminated ${NAME_PROJECT} process ==========
