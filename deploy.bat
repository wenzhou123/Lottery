@echo off
echo Starting Lottery System Deployment...

echo Stopping component containers...
docker-compose down

echo Building and starting containers...
docker-compose up -d --build

echo Deployment initiated. Please check logs for status.
docker-compose ps
