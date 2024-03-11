# LABORATORIO 06 - PATRONES ARQUITECTURALES
Construir una aplicación web utilizando una arquitectura basada en microservicios y cómo desplegarla en AWS utilizando EC2 y Docker. La aplicación consistirá en un servicio MongoDB que almacenará las cadenas enviadas por el cliente web y un servicio REST llamado LogService que recibirá las cadenas, las almacenará en la base de datos y responderá con un objeto JSON que incluya las 10 últimas cadenas almacenadas y las fechas en que fueron almacenadas.
El cliente web de la aplicación tendrá un campo de texto y un botón. Cada vez que el usuario envíe un mensaje, este se lo enviará al servicio REST. El servicio REST implementará un algoritmo de balanceo de cargas de Round Robin, delegando el procesamiento del mensaje y el retorno de la respuesta a cada una de las tres instancias del servicio LogService.

## HERRAMIENTAS 
- [MAVEN](https://maven.apache.org) : Para el manejo de las dependecias. 
- [GIT](https://git-scm.com) : Para el manejo de las versiones.
- [JAVA](https://www.java.com/es/) : Lenguaje de programación manejado.
- [DOCKER](https://www.docker.com/): Contenedor

## DOCKERHUB 
Se encuntra en un repositorio de Docker Hub que es: [juc08/lab06arep]([https://hub.docker.com/repository/docker/juc08/laboratorio05/general](https://hub.docker.com/repository/docker/juc08/lab06arep/general))

# ARQUITECTURA 

# DISEÑO DE CLASES 
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
  
# INSTALACIÓN 

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
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/82eba833-690b-4363-9c18-ab8f9425ee10)
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/af41f933-d18e-468f-92e9-028a7151ae76)
![imagen](https://github.com/Juc28/AREP_LAB06/assets/118181224/d8972e3f-4ee8-4b94-87ef-ff7a130cfe08)

# Prueba del Despliegue: 
[VIDEO DESPLIEGUE AWS]()
# Autor 
Erika Juliana Castro Romero [Juc28](https://github.com/Juc28)
