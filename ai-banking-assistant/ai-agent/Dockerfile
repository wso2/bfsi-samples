# Use official Ballerina runtime image
FROM ballerina/ballerina:2201.8.6

# Set working directory
WORKDIR /app

# Copy Ballerina project files
COPY Ballerina.toml .
COPY Dependencies.toml .
COPY Config.toml .
COPY *.bal ./

# Build the Ballerina application
RUN bal build

# Expose port (adjust based on your agent's port)
EXPOSE 9090

# Add health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:9090/health || exit 1

# Command to run the application
CMD ["bal", "run"]
