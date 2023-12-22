#!/bin/sh

WORKSPACE=$1
NAME_PROJECT=$2
DIRECTORY=$3
FILENAME=$4
FILEPATH=${DIRECTORY}${FILENAME}
PROFILE=$5

echo ========== copying ${NAME_PROJECT} file ==========

java -version

echo USER : $USER
echo WORKSPACE : ${WORKSPACE}
echo NAME_PROJECT : ${NAME_PROJECT}
echo DIRECTORY : ${DIRECTORY}
echo FILENAME : ${FILENAME}
echo FILEPATH : ${FILEPATH}
echo PROFILE : ${PROFILE}

rm -rf ${FILEPATH}
echo removed previous ${NAME_PROJECT} file
cp -r ${WORKSPACE}/build/libs/${FILENAME} ${FILEPATH}
echo copied new ${NAME_PROJECT} file

echo ========== copied ${NAME_PROJECT} file ==========
