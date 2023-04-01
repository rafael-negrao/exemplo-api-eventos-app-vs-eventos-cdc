#!/bin/bash

echo "preparando para recupera o hostname"

HOSTNAME_COMMAND=$(curl -s http://169.254.169.254/latest/meta-data/public-hostname)
export HOSTNAME_COMMAND

echo "HOSTNAME_COMMAND = $HOSTNAME_COMMAND"

echo "preparando para carregar o docker-compose"

docker compose up -d