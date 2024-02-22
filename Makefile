build:
	@echo "Building project"
	(cd SkyServerBase/; sh build.sh)
	(cd SkynetProxy/; sh build.sh)
	(cd SkyLobby/; sh build.sh)
	(cd Skywars/; sh build.sh)
	(cd SkyBuild/; sh build.sh)
	docker compose up -d

mvn-init:
	@echo "Initializing maven"
	(cd scripts/; sh mvn-install.sh se.forsum.mc paper-nms 1.8 paper-nms.jar)
	(cd SkyServerBase/; mvn clean install)

skywars:
	@echo "Building server only"
	(cd SkyServerBase/; sh build.sh)
	(cd Skywars/; sh build.sh)
	docker compose up -d