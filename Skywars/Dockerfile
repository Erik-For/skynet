FROM skynet/server-base

WORKDIR /server

COPY ./world/ /server/cworld/
COPY ./target/Skywars-1.0-SNAPSHOT.jar /server/plugins/Skywars.jar
COPY ./loot.yml /server/loot.yml
COPY ./start.sh /server/start.sh

ENV SERVER_TYPE=SKYWARS

CMD ["sh", "start.sh"]