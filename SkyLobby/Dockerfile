FROM skynet/server-base

WORKDIR /server

COPY ./world/ /server/world/

ENV SERVER_TYPE=LOBBY

CMD ["java", "-jar", "server.jar"]