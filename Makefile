build:
	@echo "Building project"
	(cd SkyServerBase/; sh build.sh)
	(cd SkynetProxy/; sh build.sh)
	(cd SkyLobby/; sh build.sh)
	(cd Skywars/; sh build.sh)
	docker compose up -d
