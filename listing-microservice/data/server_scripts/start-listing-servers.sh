#!/bin/bash

# Function to stop all servers when the script exits
cleanup() {
  echo "Stopping all MongoDB instances..."
  kill $CONFIG_SERVER_PID $SHARD1_PID $SHARD2_PID $SHARD3_PID $MONGOS_PID
  echo "All MongoDB instances stopped."
}

# Trap the EXIT signal (triggered when the script exits or when the application terminates)
trap cleanup EXIT

# Base paths
BASE_PATH="$(dirname "$0")/.."   # This gets the parent directory of the script location
DB_BASE_PATH="$BASE_PATH/dbContents/ListingDB"

echo "DB_BASE_PATH is: $DB_BASE_PATH"

# Step 1: Start Config Servers, Shard Servers, and Mongos
echo "Starting Config Server..."
mongod --configsvr --replSet configReplSet --port 27119 --dbpath "$DB_BASE_PATH/configdb" --bind_ip localhost --logpath "$DB_BASE_PATH/logs/configdb.log" &
sleep 5
CONFIG_SERVER_PID=$!
sleep 2

echo "Starting Shard Server 1..."
mongod --replSet rs1 --shardsvr --port 27120 --dbpath "$DB_BASE_PATH/shard_1" --bind_ip localhost --logpath "$DB_BASE_PATH/logs/shard_1.log" &
sleep 5
SHARD1_PID=$!
sleep 2

echo "Starting Shard Server 2..."
mongod  --replSet rs2 --shardsvr --port 27121 --dbpath "$DB_BASE_PATH/shard_2" --bind_ip localhost --logpath "$DB_BASE_PATH/logs/shard_2.log" &
sleep 5
SHARD2_PID=$!
sleep 2

echo "Starting Shard Server 3..."
mongod --replSet rs3 --shardsvr --port 27122 --dbpath "$DB_BASE_PATH/shard_3" --bind_ip localhost --logpath "$DB_BASE_PATH/logs/shard_3.log" &
sleep 5
SHARD3_PID=$!
sleep 2

echo "Starting Mongos..."
mongos --configdb configReplSet/localhost:27119 --port 27117 --bind_ip localhost --logpath "$DB_BASE_PATH/logs/mongos.log" &
sleep 5
MONGOS_PID=$!
sleep 3

echo "MongoDB Sharding Servers Started!"

wait