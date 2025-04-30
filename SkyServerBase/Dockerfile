FROM openjdk:8

WORKDIR /server

EXPOSE 25565

COPY ./server/ /server
COPY ./target/SkyServerBase-1.0-SNAPSHOT.jar /server/plugins/SkyServerBase.jar


ENV SERVER_TYPE=TEST

CMD ["java", "-jar", "server.jar"]
