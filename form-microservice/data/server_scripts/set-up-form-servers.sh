#!/bin/bash

# Function to stop all servers when the script exits
cleanup() {
  echo "Stopping all MongoDB instances"
  kill $CONFIG_SERVER_PID $SHARD1_PID $SHARD2_PID $SHARD3_PID $MONGOS_PID
  echo "All MongoDB instances stopped."
}

# Trap the EXIT signal (triggered when the script exits or when the application terminates)
trap cleanup EXIT

# Base paths - gets the parent directory of the script location
BASE_PATH="$(dirname "$0")/.."
mkdir -p "$BASE_PATH/dbContents"
mkdir -p "$BASE_PATH/dbContents/FormDB"
DB_BASE_PATH="$BASE_PATH/dbContents/FormDB"

echo "DB_BASE_PATH is: $DB_BASE_PATH"

# Create necessary directories
mkdir -p "$DB_BASE_PATH/configdb" "$DB_BASE_PATH/shard_1" "$DB_BASE_PATH/shard_2" "$DB_BASE_PATH/shard_3" "$DB_BASE_PATH/logs"

# Step 1: Start config servers, shard servers, and mongos
echo "Starting Config Server..."
mongod --configsvr --replSet configReplSet --port 27319 --dbpath "$DB_BASE_PATH/configdb" --bind_ip localhost --logpath "$DB_BASE_PATH/logs/configdb.log" &
sleep 5
CONFIG_SERVER_PID=$!
sleep 2

echo "Starting Shard Server 1..."
mongod --replSet rs1 --shardsvr --port 27320 --dbpath "$DB_BASE_PATH/shard_1" --bind_ip localhost --logpath "$DB_BASE_PATH/logs/shard_1.log" &
sleep 5
SHARD1_PID=$!
sleep 2

echo "Starting Shard Server 2..."
mongod  --replSet rs2 --shardsvr --port 27321 --dbpath "$DB_BASE_PATH/shard_2" --bind_ip localhost --logpath "$DB_BASE_PATH/logs/shard_2.log" &
sleep 5
SHARD2_PID=$!
sleep 2

echo "Starting Shard Server 3..."
mongod --replSet rs3 --shardsvr --port 27322 --dbpath "$DB_BASE_PATH/shard_3" --bind_ip localhost --logpath "$DB_BASE_PATH/logs/shard_3.log" &
sleep 5
SHARD3_PID=$!
sleep 2

echo "Starting Mongos..."
mongos --configdb configReplSet/localhost:27319 --port 27317 --bind_ip localhost --logpath "$DB_BASE_PATH/logs/mongos.log" &
sleep 5
MONGOS_PID=$!

# Wait for MongoDB instances to start up
sleep 5

# Step 2: Initialize Replica Sets for Config Servers and Shards
echo "Initializing Config replica set"
mongosh --port 27319 --eval 'rs.initiate()'
sleep 2

echo "Initializing Shard replica set 1"
mongosh --port 27320 --eval 'rs.initiate()'
sleep 2

echo "Initializing Shard replica set 2"
mongosh --port 27321 --eval 'rs.initiate()'
sleep 2

echo "Initializing Shard replica set 3"
mongosh --port 27322 --eval 'rs.initiate()'

# Wait for replica sets to initialize
sleep 5

# Step 3: Add Shards to the Cluster
echo "Adding Shard 1"
mongosh --port 27317 --eval 'sh.addShard("rs1/localhost:27320")'
sleep 2

echo "Adding Shard 2"
mongosh --port 27317 --eval 'sh.addShard("rs2/localhost:27321")'
sleep 2

echo "Adding Shard 3"
mongosh --port 27317 --eval 'sh.addShard("rs3/localhost:27322")'
sleep 2

# Step 4: Create a new database and collection, and enable sharding
# Sharding is done by default using the Location.country attribute.
echo "Creating database and enabling sharding..."
mongosh --port 27317 --eval '
  sh.enableSharding("FormDB");
  db.createCollection("formCollection");
  db.form.createIndex({ "location.country": 1 });
  sh.shardCollection("FormDB.formCollection", { "location.country": 1 });
'
sleep 3

# Step 5: Verify sharding status
echo "Verifying sharding status"
mongosh --port 27317 --eval 'sh.status()'
sleep 3

echo "MongoDB sharding setup complete!"

# Step 6: Change the maximum chunk size to 1MB - this is because the default is 256MB, which is very big and will
# need too much data to be visualized properly in our context.
echo "Changing chunk size to 1MB"

mongosh --host configReplSet/localhost:27319 --eval "
  var currentChunkSize = db.getSiblingDB('config').settings.find({_id: 'chunksize'}).toArray();
  print('Current chunk size: ' + JSON.stringify(currentChunkSize));

  db.getSiblingDB('config').settings.update({ _id: 'chunksize' },{ \$set: { value: 1 } },{ upsert: true });

  // Verify the change
  var newChunkSize = db.getSiblingDB('config').settings.find({_id: 'chunksize'}).toArray();
  print('New chunk size: ' + JSON.stringify(newChunkSize));
"
sleep 5

echo "Delete extra test database"
mongosh --port 27317 --eval 'db.dropDatabase()'

sleep 5
echo "I think all good in the hood?"

wait