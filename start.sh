#!/bin/bash

# This script is built for macOS or Linux, not for Windows.

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Docker is not running. Please ensure Docker is running."
    exit 1
fi

# Check if containers are already running
if docker compose ps | grep -q "Up"; then
    echo "Containers are already running. No need to start."
    exit 0
fi

# Build and start all containers
echo "Building and starting containers..."
docker compose up --build -d

# Verify that all containers are running
echo "Verifying container status..."
if docker compose ps | grep -q "Exit"; then
    echo "Error: Some containers are not running. Please check the logs."
    exit 1
else
    echo "All containers are running successfully."
fi 