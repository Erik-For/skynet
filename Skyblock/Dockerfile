FROM skynet/server-base

WORKDIR /server

COPY ./world/ /server/world/
COPY ./target/skyblock-1.0-SNAPSHOT.jar /server/plugins/Skyblock.jar

ENV SERVER_TYPE=SKYBLOCK

CMD ["java", "-jar", "server.jar"]