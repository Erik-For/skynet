FROM skynet/server-base

WORKDIR /server

COPY ./world/ /server/world/
COPY ./target/Skywars-1.0-SNAPSHOT.jar /server/plugins/Skywars.jar
COPY ./loot.yml /server/loot.yml

ENV SERVER_TYPE=SKYWARS

CMD ["java", "-jar", "server.jar"]