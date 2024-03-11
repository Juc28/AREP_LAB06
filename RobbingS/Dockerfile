FROM openjdk:17

WORKDIR /usrapp/bin


COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency

ENV PORT 4568


CMD ["java","-cp","./classes:./dependency/*", "edu.eci.arep.LogService"]