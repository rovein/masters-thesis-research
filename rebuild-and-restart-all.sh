docker stop masters-thesis-research-cdc-service-outbox-1
docker stop masters-thesis-research-cdc-service-event-sourcing-1
docker stop masters-thesis-research-mysql-outbox-1
docker stop masters-thesis-research-mysql-event-sourcing-1
docker stop masters-thesis-research-kafka-outbox-1
docker stop masters-thesis-research-kafka-event-sourcing-1

docker rm masters-thesis-research-cdc-service-outbox-1
docker rm masters-thesis-research-cdc-service-event-sourcing-1
docker rm masters-thesis-research-mysql-outbox-1
docker rm masters-thesis-research-mysql-event-sourcing-1
docker rm masters-thesis-research-kafka-outbox-1
docker rm masters-thesis-research-kafka-event-sourcing-1

bash ./rebuild-and-run-all.sh