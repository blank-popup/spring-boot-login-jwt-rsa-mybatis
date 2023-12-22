WORKSPACE=$1
NAME_PROJECT=$2
DIRECTORY=$3
FILENAME=$4
FILEPATH=${DIRECTORY}${FILENAME}
PROFILE=$5
OPTION="-Dspring.profiles.active=${PROFILE}"

echo ========== Starting ${NAME_PROJECT} process ==========

java -version

echo USER : $USER
echo WORKSPACE : ${WORKSPACE}
echo NAME_PROJECT : ${NAME_PROJECT}
echo DIRECTORY : ${DIRECTORY}
echo FILENAME : ${FILENAME}
echo FILEPATH : ${FILEPATH}
echo PROFILE : ${PROFILE}
echo OPTION : ${OPTION}

java -jar ${OPTION} "${FILEPATH}" &

echo ========== Started ${NAME_PROJECT} process ==========
