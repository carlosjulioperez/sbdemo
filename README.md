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

Ejecutar las pruebas JUnit 5:
$ ./gradlew test

$ curl http://localhost:7000/product-composite/1 -s | jq .

Detener las instancias:
$ kill $(jobs -p)

Equipo de desarrollo: MacBook Pro, 8GB Ram, 4 CPUs
CPU (cores) disponibles:
  Bash:
  $ top -i, 1
  $ echo 'Runtime.getRuntime().availableProcessors()' | jshell -q
    jshell> Runtime.getRuntime().availableProcessors()$1 ==> 4

  + Java determina todos los recursos del Host, sin restricción. Si no existe la imagen docker, se descarga:
  $ echo 'Runtime.getRuntime().availableProcessors()' | docker run --rm -i openjdk:12.0.2 jshell -q
    jshell> Runtime.getRuntime().availableProcessors()$1 ==> 4

  + Restringir al uso de 3 Cpus:
  $ echo 'Runtime.getRuntime().availableProcessors()' | docker run --rm -i --cpus 3 openjdk:12.0.2 jshell -q
    jshell> Runtime.getRuntime().availableProcessors()$1 ==> 3

  + Restringir una cuota relativa de los CPUs disponibles (cuando Host docker está en alta carga):
  $ echo 'Runtime.getRuntime().availableProcessors()' | docker run --rm -i --cpu-shares 2048 openjdk:12.0.2 jshell -q
    jshell> Runtime.getRuntime().availableProcessors()$1 ==> 2

  + Sin restricciones docker consume 1/4 de memoria:
  $ docker run -it --rm openjdk:12.0.2 java -XX:+PrintFlagsFinal -version | grep MaxHeapSize
    size_t MaxHeapSize                              = 2067791872                                {product} {ergonomic}
  
  + Restringir a 1GB de memoria (1/4 de 1GB=256MB):
  $ docker run -it --rm -m=1024M openjdk:12.0.2 java -XX:+PrintFlagsFinal -version | grep MaxHeapSize
    size_t MaxHeapSize                              = 268435456                                 {product} {ergonomic}

  + Permitir a heap usar 800MB de 1GB disponible:
  $ docker run -it --rm -m=1024M openjdk:12.0.2 java -Xmx800m -XX:+PrintFlagsFinal -version | grep MaxHeapSize
    size_t MaxHeapSize                              = 838860800                                 {product} {command line}

  En GroovyConsole:
  println Runtime.getRuntime().availableProcessors()

Tamaño máximo para asignar a heap:
$ java -XX:+PrintFlagsFinal -version | grep MaxHeapSize

Reducir heap a 200MB
$ java -Xmx200m -XX:+PrintFlagsFinal -version | grep MaxHeapSize

Docker:
