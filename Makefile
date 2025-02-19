.PHONY: up down restart logs ps build clean help

# Default target
help:
	@echo "Available commands:"
	@echo "  make up        - Start all services"
	@echo "  make down      - Stop and remove all containers"
	@echo "  make restart   - Restart all services"
	@echo "  make logs      - View logs from all services"
	@echo "  make ps        - List running services"
	@echo "  make build     - Rebuild all services"
	@echo "  make clean     - Remove all containers, volumes, and networks"

# Start services
up:
	docker compose up -d

# Stop services
down:
	docker compose down

# Restart services
restart:
	docker compose restart

# View logs
logs:
	docker compose logs -f

# List running services
ps:
	docker compose ps

# Build services
build:
	cd SkyServerBase && ./build.sh
	cd SkyBuild && ./build.sh
	cd SkyLobby && ./build.sh
	cd Skywars && ./build.sh
	cd SkynetProxy && ./build.sh

# Clean everything
clean:
	docker compose down -v
	docker system prune -f
