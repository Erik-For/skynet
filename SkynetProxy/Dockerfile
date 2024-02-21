FROM openjdk:17

WORKDIR /server

COPY ./server/ /server
COPY ./target/SkynetProxy-1.0-SNAPSHOT.jar /server/plugins/SkynetProxy.jar

CMD ["java", "-jar", "BungeeCord.jar"]