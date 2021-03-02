# sbdemo

Generar los servicios (Java 11)
$ ./gradlew build

Iniciar los servicios:
$ java -jar microservices/product-service/build/libs/*.jar &
$ java -jar microservices/price-service/build/libs/*.jar &
$ java -jar microservices/product-composite-service/build/libs/*.jar &

Recibir solicitudes:
$ curl http://localhost:7000/product-composite/1
$ curl http://localhost:7001/product/123
$ curl http://localhost:7002/price?productId=1

$ curl http://localhost:7000/product-composite/1 -s | jq .

$ kill $(jobs -p)
