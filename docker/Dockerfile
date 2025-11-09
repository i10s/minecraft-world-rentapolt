FROM openjdk:17-jdk-slim

# Install wget and other utilities
RUN apt-get update && \
    apt-get install -y wget curl && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /server

# Download Fabric server installer
RUN wget https://meta.fabricmc.net/v2/versions/loader/1.20.1/0.17.3/1.0.0/server/jar -O fabric-server-launch.jar

# Create necessary directories
RUN mkdir -p /server/mods /server/world /server/config /server/logs

# Accept EULA automatically
RUN echo "eula=true" > eula.txt

# Copy startup script
COPY startup.sh /server/startup.sh
RUN chmod +x /server/startup.sh

# Expose Minecraft server port
EXPOSE 25565

# Set default memory allocation
ENV JAVA_OPTS="-Xmx4G -Xms2G"

# Run the server
CMD ["/server/startup.sh"]
