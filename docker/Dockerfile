FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /server

# Copy Fabric server files (pre-downloaded locally)
COPY fabric-server-launch.jar /server/fabric-server-launch.jar
COPY server.jar /server/server.jar
COPY libraries/ /server/libraries/

# Copy startup script
COPY startup.sh /server/startup.sh
RUN chmod +x /server/startup.sh

# Create necessary directories
RUN mkdir -p /server/mods /server/world /server/config /server/logs

# Accept EULA automatically
RUN echo "eula=true" > eula.txt

# Expose Minecraft server port
EXPOSE 25565

# Set default memory allocation
ENV JAVA_OPTS="-Xmx4G -Xms2G -Djava.security.egd=file:/dev/urandom"

# Run the server
CMD ["/server/startup.sh"]
