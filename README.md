# LABORATORIO 06 - PATRONES ARQUITECTURALES
Construir una aplicación web utilizando una arquitectura basada en microservicios y cómo desplegarla en AWS utilizando EC2 y Docker. La aplicación consistirá en un servicio MongoDB que almacenará las cadenas enviadas por el cliente web y un servicio REST llamado LogService que recibirá las cadenas, las almacenará en la base de datos y responderá con un objeto JSON que incluya las 10 últimas cadenas almacenadas y las fechas en que fueron almacenadas.
El cliente web de la aplicación tendrá un campo de texto y un botón. Cada vez que el usuario envíe un mensaje, este se lo enviará al servicio REST. El servicio REST implementará un algoritmo de balanceo de cargas de Round Robin, delegando el procesamiento del mensaje y el retorno de la respuesta a cada una de las tres instancias del servicio LogService.

## HERRAMIENTAS 
- [MAVEN](https://maven.apache.org) : Para el manejo de las dependecias.
  <p align="center">
  <IMG src=https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Apache_Maven_logo.svg/1280px-Apache_Maven_logo.svg.png height=150 width=250 >
    <p/>
- [GIT](https://git-scm.com) : Para el manejo de las versiones.
  <p align="center">
  <IMG src=https://logowik.com/content/uploads/images/git6963.jpg height=150 width=250 >
    <p/>
- [JAVA](https://www.java.com/es/) : Lenguaje de programación manejado.
  <p align="center">
  <IMG src=https://1000marcas.net/wp-content/uploads/2020/11/Java-logo.png height=150 width=250> 
  <p/>
- [DOCKER](https://www.docker.com/): Contenedor
  <p align="center">
   <IMG src=https://static-00.iconduck.com/assets.00/docker-icon-2048x1753-uguk29a7.png height=150 width=250> 
  <p/>
## DOCKERHUB 
Se encuntra en un repositorio de Docker Hub que es: [juc08/lab06arep](https://hub.docker.com/repository/docker/juc08/lab06arep/general)

# ARQUITECTURA 
La arquitectura de este sistema de registro se basa en un enfoque **round-robin** para distribuir las solicitudes de registro entre varios servidores. Los servidores de registro se encargan de almacenar las cadenas de registro en una base de datos **MongoDB**.

La clase LogService es el servidor de registro que recibe las solicitudes GET y registra el mensaje en la base de datos MongoDB. La clase **LogService** utiliza el controlador de MongoDB para interactuar con la base de datos.

El flujo de registro es el siguiente:

1. El cliente envía una solicitud GET al servidor de registro actual con el mensaje que desea registrar.
2. El servidor de registro recibe la solicitud GET y registra el mensaje en la base de datos MongoDB.
3. El servidor de registro recupera las 10 últimas cadenas de registro con sus fechas de la base de datos MongoDB.
4. El servidor de registro crea un objeto JSON con las últimas 10 cadenas de registro y sus fechas.
5. El servidor de registro devuelve el objeto JSON al cliente.

El enfoque **round-robin** garantiza un equilibrio en la carga de trabajo entre los servidores de registro. Además, al utilizar MongoDB para almacenar las cadenas de registro, se garantiza la persistencia de los datos incluso si un servidor de registro falla.

# DISEÑO DE CLASES 
 2 componentes principales: 
  1. Round Robin --> Tiene dos clases que son:
     - LogServiceFacade: Implementado utilizando el framework Spark en Java. El servicio utiliza MongoDB como base de datos para almacenar y recuperar los mensajes registrados.

        Descripción de los métodos principales en la clase:
       
       * main: Este es el método principal de la clase. Aquí se configura el servidor Spark y se define la ruta de acceso para el servicio de registro de mensajes. La ruta es "/          logservice" y acepta una consulta de parámetro "message". Cuando se accede a esta ruta con una consulta de parámetro "message", se llama al método logMessage con el              valor de la consulta de parámetro "message".
       * logMessage: Este método se encarga de registrar el mensaje en MongoDB y de recuperar las 10 últimas entradas de registro. Primero, se conecta al servidor MongoDB                 utilizando el cliente MongoClient. Luego, se obtiene la base de datos "mydb" y la colección "log". Se inserta el mensaje en la colección con una marca de tiempo en un            documento Document. Luego, se recuperan las 10 últimas entradas de registro de la colección y se convierten en una lista de mapas. Finalmente, se crea un objeto JSON             con las últimas 10 entradas de registro y sus marcas de tiempo utilizando la biblioteca Gson.
       * getPort: Este método devuelve el número de puerto en el que se ejecuta el servidor. Si la variable de entorno "PORT" está configurada, se devuelve su valor. De lo                contrario, se devuelve el número de puerto predeterminado 4568.
         
     - RemoteLogService: Es un servicio de registro remoto que utiliza una técnica de equilibrio de carga round-robin para distribuir las solicitudes de registro entre varios            servidores.
  
         Descripción de los métodos principales en la clase:
         
          * getLogs(String message) realiza una solicitud GET a un servidor de registro. La URL de la solicitud se construye utilizando el servidor actual y el mensaje                       de registro. La solicitud se realiza utilizando la clase HttpURLConnection. Si la solicitud se realiza correctamente (es decir, el código de respuesta es HTTP_OK),               se  devuelve la respuesta del servidor como una lista de cadenas.
          * rotateRoundRobinServer() se encarga de rotar el servidor actual al siguiente en el array de servidores. Después de obtener el servidor actual, se incrementa el                   contador currentServer y se realiza la división módulo para asegurar que el índice del servidor actual esté dentro de los límites del array de servidores.
          * getLogs(String message) con el mensaje de registro que se desea enviar. El método se encargará de enviar la solicitud al servidor actual y devolver la respuesta del              servidor.
     
  2. RobbingS --> Tiene una clase que es:
    
          
     - LogServer: Es una aplicación web que expone una ruta ("/log") que acepta solicitudes GET y envía los parámetros de consulta a la clase RemoteLogService para que se               registren.
       
       Descripción de los métodos principales en la clase:
       
        - main de la clase LogServerFacade inicializa el servidor web Spark y configura la ubicación de los archivos estáticos en "/public". También define una ruta ("/log")               que acepta solicitudes GET y envía los parámetros de consulta a la clase RemoteLogService para que se registren.
        - getPort() devuelve el número de puerto en el que se ejecutará el servidor web. Si se establece la variable de entorno "PORT", se utilizará ese valor como puerto. De lo           contrario, se utilizará el puerto 4567.
        - port(int port) establece el puerto en el que se ejecutará el servidor web.
        - staticFiles.location("/public") establece la ubicación de los archivos estáticos en "/public". Esto significa que cualquier archivo solicitado en una ruta que comience           con "/public" se servirá desde el directorio "public" en el sistema de archivos.
        - get("/log",(req,res) -> {...}) define una ruta ("/log") que acepta solicitudes GET. El parámetro de consulta "value" se pasa a la clase RemoteLogService para que se              registre. La respuesta del método RemoteLogService.getLogs(val) se devuelve a la solicitud.
          
# INSTALACIÓN 
+ Se clona el repositorio en una máquina local con el siguiente comando:
  
    ```
    git clone https://github.com/Juc28/AREP_LAB06.git
    ```
+ Entrar al directorio del proyecto con el siguiente comando:
    ```
    cd AREP_LAB06
    ```
+ Compilar cada proyecto:
  ```
  cd RobbingS
  mvn clean install
  ```
  
  ```
  cd RoundRobin
  mvn clean install
  ```
+ Construir las imagenes de docker, cada uno en el directorio correpondiente:
  ```
  docker build --tag robbings .
  ```
  ![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/57c65482-6fe3-4beb-b9b2-9fa082f3d458)

  ```
  docker build --tag roundrobin  .
  ```
  ![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/fa11690a-b8d3-486d-8b5e-d440833970cd)
  
+ Docker Hub:
  ```
  docker pull juc08/lab06arep:roundrobin
  docker pull juc08/lab06arep:robbings
  ```
+ Realizamos el ajuste de nombres para el docker compose:
  ```
  docker pull juc08/lab06arep:roundrobin roundrobin
  docker pull juc08/lab06arep:robbings robbings
  ```
+ Teniendo en cuenta que se está en el directorio donde se encuentra el docker compose se da lo siguiente:
  ```
  docker-compose up -d
  ```
  ![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/a5f93a54-5fbb-4069-8ac0-b323468de752)
# PRUEBAS 
## Local 
* Abrir en el navegador:
 ```
 http://localhost:4567/formulario.html
 ```
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/ea0ebdc1-0e16-4918-ae1a-219bb0bed957)
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/5e574976-7cd6-444e-8dc3-8e4728a97cb2)

## Local con Docker-Compose 
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/a5f93a54-5fbb-4069-8ac0-b323468de752)
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/d7fed0d2-e400-458c-a150-853ae60a3a5b)

## AWS
* Abrir en el navegador:
 ```
 http://ec2-3-91-246-67.compute-1.amazonaws.com:4567/formulario.html
 ```
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/82eba833-690b-4363-9c18-ab8f9425ee10)
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/af41f933-d18e-468f-92e9-028a7151ae76)
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/d8972e3f-4ee8-4b94-87ef-ff7a130cfe08)

# Prueba del Despliegue: 
[VIDEO DESPLIEGUE AWS](https://youtu.be/WzMq4r04fDQ)
# Autor 
Erika Juliana Castro Romero [Juc28](https://github.com/Juc28)
