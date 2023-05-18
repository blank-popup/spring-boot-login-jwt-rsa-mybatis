SSH         18000
HTTP        18010
DB          18020
JENKINS     18990

== SSH
sudo apt install net-tools
audo apt install openssh-server

sudo vi /etc/ssh/sshd_config
------------------
PORT 18000
------------------

sudo vi /etc/hosts.allow
------------------
sshd: 192.168.
------------------

sudo vi /etc/hosts.deny
------------------
sshd: ALL
------------------

== MariaDB
sudo apt update
sudo apt install software-properties-common
sudo apt-key adv --fetch-keys 'https://mariadb.org/mariadb_release_signing_key.asc'
sudo add-apt-repository 'deb [arch=amd64,arm64,ppc64el] http://mirror.rackspace.com/mariadb/repo/10.4/ubuntu focal main'
sudo apt update
sudo apt install mariadb-server
sudo systemctl enable mariadb.service
sudo systemctl start mariadb
sudo mariadb-secure-installation

Switch to unix_socket authentication [Y/n] n
 ... skipping.

Change the root password? [Y/n] y
New password:
Re-enter new password:
Password updated successfully!
Reloading privilege tables..
 ... Success!

Remove anonymous users? [Y/n]
 ... Success!

Disallow root login remotely? [Y/n]
 ... Success!

Remove test database and access to it? [Y/n]
 - Dropping test database...
 ... Success!
 - Removing privileges on test database...
 ... Success!

Reload privilege tables now? [Y/n]
 ... Success!


sudo mysql -u root -p

use mysql;
CREATE DATABASE databaseName;
SHOW DATABASES;
DROP DATABASE databaseName;

CREATE USER 'username'@'localhost' IDENTIFIED BY 'password';
CREATE USER 'username'@'%' IDENTIFIED BY 'password';

SELECT HOST, USER, PASSWORD FROM USER;

DROP USER 'username'@'localhost';
DROP USER 'username'@'%';

GRANT ALL PRIVILEGES ON databaseName.* TO 'username'@'localhost';
GRANT ALL PRIVILEGES ON databaseName.* TO 'username'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'username'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO 'username'@'%';

FLUSH PRIVILEGES;


sudo vi /etc/mysql/mariadb.conf.d/50-server.cnf
------------------
port                   = 18020
------------------


== Jenkins
https://pkg.jenkins.io/debian-stable/direct/
https://pkg.jenkins.io/debian-stable/direct/jenkins_2.346.3_all.deb
wget https://get.jenkins.io/war-stable/2.346.3/jenkins.war
wget https://pkg.jenkins.io/debian-stable/binary/jenkins_2.346.3_all.deb
sudo dpkg -i jenkins_2.346.3_all.deb
sudo apt install git

$WORKSPACE
    /var/lib/jenkins/workspace/ProjectName

sudo vi /usr/lib/systemd/system/jenkins.service
search "PORT"
    /PORT
------------------
Environment="JENKINS_PORT=18990"
------------------


Freestyle Project
    Git
        Repositories
            Repository URL
                https://github.com/someone/sample.git
        Branches to build
            Branch Specifier (blank for any)
                */master
    build
        Invoke Gradle script
            Invoke Gradle
                Gradle Version
                    Gradle 7.6.1
        Tasks
            clean
            build
            -Ptarget=develop
            --info

        Execute shell
            Command
                set BUILD_ID=dontKillMe

                sudo /home/dave/server-aa/api/execute/stop.sh
                sleep 1

                sudo rm -rf /home/dave/server-aa/api/bin/*.jar
                sleep 1

                sudo cp -r $WORKSPACE/build/libs/loginJwtRSA-0.0.1-SNAPSHOT.jar /home/dave/server-aa/api/bin/.
                sleep 1

                sudo /home/dave/server-aa/api/execute/start.sh
                sleep 1

                sudo rm -rf /home/dave/server-aa/api/jwt-rsa-help/*
                sleep 1

                sudo cp -r $WORKSPACE/src/main/resources/static/docs/* /home/dave/server-aa/api/jwt-rsa-help/.
                sleep 1


                # sudo /home/dave/server-aa/api/execute/deploy.sh $WORKSPACE/build/libs/loginJwtRSA-0.0.1-SNAPSHOT.jar


vi /home/dave/server-aa/api/execute/stop.sh
------------------
#!/bin/sh

COMPONENT_ID=loginJwtRSA-0.0.1-SNAPSHOT

PID=`ps -ef | grep $COMPONENT_ID | grep -v grep | awk '{print $2}'`

echo ${PID}

if [ -n "${PID}" ]
then
        result1=$(kill -9 ${PID})
        echo process is killed.
else
        echo running process not found.
fi
------------------


vi /home/dave/server-aa/api/execute/start.sh
------------------
#/bin/sh

DIR="/home/dave/server-aa/api"
SERVICE_NAME="loginJwtRSA"
#LOG="${DIR}/log/${SERVICE_NAME}-$(date '+%Y%m%d').log"
BIN=`ls -tr ${DIR}/bin/${SERVICE_NAME}-0.0.1-SNAPSHOT.jar | tail -n 1`
LOG="${DIR}/log/service.log"

OPT="-Dspring.profiles.active=develop"

java -jar ${OPT} "${BIN}" 2>&1 >> "${LOG}" &
------------------


vi /home/dave/server-aa/api/execute/deploy.sh
------------------
#/bin/sh

BASE_PATH=/home/dave/server-aa/api/bin
EXECUTE_PATH=/home/dave/server-aa/api/execute
SERVICE_NAME="loginJwtRSA-0.0.1-SNAPSHOT.jar"
$EXECUTE_PATH/stop.sh


echo next step
sleep 1

echo step1
rm -rf $BASE_PATH/${SERVICE_NAME}

sleep 1

cp -r $1 $BASE_PATH/.

echo step2

sleep 1

$EXECUTE_PATH/start.sh

echo step3
timeout 60 tail -f /home/dave/server-aa/api/log/service.log

true
------------------


sudo vi /etc/sudoers
------------------
# User privilege specification
root    ALL=(ALL:ALL) ALL
jenkins ALL=(ALL) NOPASSWD: ALL
------------------


== Java
sudo apt install openjdk-17-jdk
sudo apt install openjdk-11-jdk
sudo apt install openjdk-8-jdk

sudo update-alternatives --config java
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java 11081


== Nginx
sudo apt install nginx

sudo vi /etc/nginx/sites-available/default
------------------
# Please see /usr/share/doc/nginx-doc/examples/ for more detailed examples.

server {
    listen 80 default_server;
    listen [::]:80 default_server;

    root /var/www/html;
    index index.html index.htm index.nginx-debian.html;

    server_name _;

    location / {
        try_files $uri $uri/ =404;
    }

    location /jwt-rsa-help {
        root /home/dave/server-aa/api;
        #autoindex on;
        #index index.html;
    }

    location /jwt-rsa {
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header Host $host;
        proxy_pass http://jwt-rsa;
    }
}


upstream jwt-rsa {
    server localhost:18010;
}
------------------
