#clear log files
echo -n "" > logs/basket-service.log
echo -n "" > logs/order-service.log
echo -n "" > logs/product-service.log

#rebuild docker images (if necessary) and start containers
docker compose build && docker compose up -d