#!/bin/bash

HOSTNAME_COMMAND=$(curl -s http://169.254.169.254/latest/meta-data/public-hostname)
export HOSTNAME_COMMAND

docker compose -d