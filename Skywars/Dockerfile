FROM skynet/server-base

WORKDIR /server

COPY ./world/ /server/world/

ENV SERVER_TYPE=SKYWARS

CMD ["java", "-jar", "server.jar"]