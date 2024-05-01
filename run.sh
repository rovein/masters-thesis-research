#clear log files
bash ./clear-logs.sh

#rebuild docker images (if necessary) and start containers
docker compose build && docker compose up -d