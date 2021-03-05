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

Generar un servicio específico:
./gradlew :microservices:product-service:build

Generar Docker:
$ cd microservices/product-service
$ docker build -t product-service .

Pruebas con Docker:

Equipo de desarrollo: MacBook Pro, 8GB Ram, 4 CPUs
CPU (cores) disponibles:
  
+ En GroovyConsole:
$ println Runtime.getRuntime().availableProcessors()

+ Bash:
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

+ Tamaño máximo para asignar a heap:
$ java -XX:+PrintFlagsFinal -version | grep MaxHeapSize
    size_t MaxHeapSize                              = 2067791872                                {product} {ergonomic}

+ Reducir heap a 200MB
$ java -Xmx200m -XX:+PrintFlagsFinal -version | grep MaxHeapSize
    size_t MaxHeapSize                              = 209715200                                 {product} {command line}

+ Test de memoria:
  Let's allocate some memory using jshell in a JVM that runs in a container that has been given 1 GB of memory; that is, it has a max heap size of 256 MB.

  a) First, try to allocate a byte array of 100 MB (1024/4=256 MB available)

  $ echo 'new byte[100_000_000]' | docker run -i --rm -m=1024M openjdk:12.0.2 jshell -q
    WARNING: Your kernel does not support swap limit capabilities or the cgroup is not mounted. Memory limited without swap.
    Mar 04, 2021 11:31:33 PM java.util.prefs.FileSystemPreferences$1 run
    INFO: Created user preferences directory.
    jshell> new byte[100_000_000]$1 ==>
  
  The command will respond with $1 ==>, meaning that it worked fine!

  b) Now, let's try to allocate a byte array that is larger than the max heap size, for example, 500 MB:

  $ echo 'new byte[500_000_000]' | docker run -i --rm -m=1024M openjdk:12.0.2 jshell -q
    Mar 04, 2021 11:37:26 PM java.util.prefs.FileSystemPreferences$1 run
    INFO: Created user preferences directory.
    jshell> new byte[500_000_000]|  Exception java.lang.OutOfMemoryError: Java heap space
    |        at (#1:1)


