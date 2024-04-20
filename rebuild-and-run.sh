#rebuild all microservices
mvn clean install -DskipTests -e -T 1C -DtrimStackTrace=false -Dmdep.skip=true -am -Denforcer.skip -Dcheckstyle.skip=true -Dmdep.skip=true

#clear log files
echo -n "" > logs/basket-service.log
echo -n "" > logs/order-service.log
echo -n "" > logs/product-service.log

#rebuild docker images and start containers
docker compose build && docker compose up -d