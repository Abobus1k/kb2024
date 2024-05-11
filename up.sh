#!/bin/bash

# Перед запуском этого скрипта нужно из-под папки java выполнить ./mvnw clean package

#sudo docker compose -f monitor/docker-compose.yml up -d --build
# Pause for 8 seconds
#sleep 8

sudo docker compose -f java/AiConnector/docker-compose.yml up -d --build
sudo docker compose -f java/Redirector/docker-compose.yml up -d --build
