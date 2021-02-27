# sbdemo

./gradlew build

java -jar microservices/product-service/build/libs/*.jar &
curl http://localhost:7001/product/123

java -jar microservices/price-service/build/libs/*.jar &
curl http://localhost:7002/price?productId=1
