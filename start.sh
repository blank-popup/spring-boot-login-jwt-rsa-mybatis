WORKSPACE=$1
NAME_PROJECT=$2
DIRECTORY=$3
FILENAME=$4
VALUE_PROFILE=$5
FILEPATH=${DIRECTORY}${FILENAME}
KEY_PROFILE="spring.profiles.active"
OPTION="-D${KEY_PROFILE}=${VALUE_PROFILE}"

echo ========== Starting ${NAME_PROJECT} process ==========

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

java -jar ${OPTION} "${FILEPATH}" &

echo ========== Started ${NAME_PROJECT} process ==========
