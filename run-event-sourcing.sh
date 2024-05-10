#clear log files
bash ./clear-logs.sh

#rebuild docker images (if necessary) and start containers
docker compose -f docker-compose-event-sourcing.yml build && docker compose -f docker-compose-event-sourcing.yml up -d