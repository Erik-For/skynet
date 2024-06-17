FROM openjdk:17

WORKDIR /server

ENV PROTOCOL_VERSION=47

COPY ./server/ /server
COPY ./target/SkynetProxy-1.0-SNAPSHOT.jar /server/plugins/SkynetProxy.jar

CMD ["java", "-jar", "BungeeCord.jar"]