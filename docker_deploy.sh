#!/bin/bash
#Script for Ubuntu 14.04

# Update
#sudo apt-get update -y

# Docker + Docker-compose installation
if [[ ! $(which wget) ]] 
then
	sudo apt-get install wget
fi

if [[ (! $(which docker)) && (! $(which docker.io)) ]]
then
	echo "[INSTALL] Docker"
	wget -qO- https://get.docker.com/ | sh
fi

if [[ ! $(which docker-compose) ]]
then
	if [[ ! $(which curl) ]]
	then
		sudo apt-get install curl
	fi

	echo "[INSTALL] Docker-compose"
	curl -L https://github.com/docker/compose/releases/download/1.1.0/docker-compose-`uname -s`-`uname -m` > docker-compose
	sudo mv docker-compose /usr/local/bin/
	sudo chmod +x /usr/local/bin/docker-compose
fi

# Creation of the docker group
if [[ ! $(egrep -i "^docker" /etc/group) ]]
then
	echo "Creation of the group docker - You should log out and then log back in to make sure the changes took effect"
	sudo groupadd docker
	sudo chown root:docker /var/run/docker.sock
fi

# Add the current user to the group
echo "Current user will be added to the group docker"
sudo usermod -a -G docker ${USER}
sudo service docker restart
#newgrp docker

# Downloading and installation of media-home
d=~/media-home/
sm_docker_d=$d"docker/composer/composerSnapmail"
echo $sm_docker_d
if [[ ! -d $d ]]
then
	cd ~
	git clone https://github.com/dngroup/media-home
else
	cd $d
#	git pull
fi

# Creation of the cron job
freq="0 0 * * *"
command=$d"docker_run.sh"
cronjob=$freq$command

if [[ ! $(crontab -l | grep "$command") ]]
then
	echo "[CREATE] cronjob to update media-home : $cronjob"
	sudo printf "$(crontab -l)\n$cronjob" | sudo dd of=/var/spool/cron/crontabs/${USER}
fi

cd $cd
chmod +x docker_run.sh
