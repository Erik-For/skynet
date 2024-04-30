# SkyNet
## Network
- SkyNet is a system that combines multiple Minecraft servers into a unified network using an application called Bungeecord. Bungeecord acts as a bridge, connecting the servers and making them behave as a single entity.
- To function properly, each server connected to Bungeecord requires specific configuration. SkyNet simplifies this process by automatically configuring Bungeecord on the fly using Redis, a pub/sub messaging channel. This eliminates the need for manual configuration of each individual server.
- The entire SkyNet system is run within Docker containers, which provides the advantage of easy scalability. By containerizing different services

## WARNING
- Before deploying make sure to change passwords in docker-compose.yml
