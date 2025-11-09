# Makefile for Rentapolt Docker Server Management

.PHONY: help build deploy start stop restart logs clean update-mod

help:
	@echo "Rentapolt Docker Server Commands:"
	@echo ""
	@echo "  make deploy       - Build mod, copy to server, and restart container"
	@echo "  make update-mod   - Rebuild mod and copy JAR to mods folder"
	@echo "  make start        - Start the Docker container"
	@echo "  make stop         - Stop the Docker container"
	@echo "  make restart      - Restart the Docker container"
	@echo "  make logs         - Show server logs (last 50 lines)"
	@echo "  make logs-live    - Follow server logs in real-time"
	@echo "  make build        - Build the Docker image"
	@echo "  make clean        - Stop container and remove volumes (WARNING: deletes world data)"
	@echo ""

update-mod:
	@echo "Updating Rentapolt mod..."
	./update-mod.sh

deploy: update-mod
	@echo "Restarting Docker container..."
	docker compose restart
	@echo ""
	@echo "✓ Deployment complete! Server restarting..."
	@echo "Run 'make logs-live' to monitor startup"

start:
	docker compose up -d

stop:
	docker compose down

restart:
	docker compose restart

logs:
	docker logs rentapolt_server --tail 50

logs-live:
	docker logs -f rentapolt_server

build:
	docker compose build

clean:
	@echo "WARNING: This will delete all world data!"
	@read -p "Are you sure? [y/N] " -n 1 -r; \
	echo; \
	if [[ $$REPLY =~ ^[Yy]$$ ]]; then \
		docker compose down -v; \
		echo "✓ Cleaned up containers and volumes"; \
	fi
