#rebuild all microservices
mvn clean install -DskipTests -e -T 1C -DtrimStackTrace=false -Dmdep.skip=true -am -Denforcer.skip -Dcheckstyle.skip=true -Dmdep.skip=true

bash ./run-outbox.sh