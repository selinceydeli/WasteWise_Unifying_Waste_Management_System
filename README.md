<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://gitlab.ewi.tudelft.nl/cs4505/2024-2025/teams/team-41/">
    <img src="assets/logo.png" alt="Logo" width="160" height="160">
  </a>

  <h3 align="center">WasteWise: Unifying Waste Management</h3>
</div>

## PoC Experiment: MongoDB Sharded Collection and High-Frequency Simulation

Our Proof of Concept (PoC) aims to demonstrate the feasibility of WasteWise by outlining the microservice architecture and illustrate how our database works utilizing database sharding. The system uses a MongoDB database to enable sharding to aid with the scalability goal of the project. Therefore, our experimentation for the PoC consists of creating a sharded database and verifying its functionality.

## Motivation Behind our Experiment

This system uses MongoDB databases and Java Spring Boot to achieve automatic load rebalancing as the system databases change in size. The sharding logic is implemented for all of our microservices, namely `Listing`, `Information Display`, `User`, and `Form`, and it runs locally. Furthermore, database sharding is automated using set-up and start scripts to leverage sharding without requiring extensive manual work.

Although sharding is implemented for all microservices, the focus of the PoC is on the Listing microservice. In our system, the Listing microservice is responsible for creating a platform in which authenticated Private Owners interact with the listings. Each listing offers information about the current availability of waste processing or collection, the quantity that can be handled, the time frame for this handling, and the location where the collection or processing will be conducted. Private Owners can view the listings posted by other Private Owners and can add, edit, or delete their own listings.

Since the ideal WasteWise system will operate throughout the entire EU, we envision every Private Owner interacting with the listings posted for their base country. Therefore, the base filtering applied when a Private Owner wants to fetch the available listings will be location filtering. Each Private Owner is prompted to visualize the listings available in their country. Consequently, location-based filtering will be a frequently performed query to the database, as it will be applied every time a Private Company views the listing platform. Furthermore, the listings will be stored within the database of the Listing microservice.

With the potential for our system to scale across the EU, it is anticipated that this will significantly increase both the query load on our database and the number of entries stored. Therefore, it is essential to add additional servers and distribute the system's data and load across those servers. This can be achieved using database sharding.

## Evaluating Database Sharding Performance with High-Frequency Simulation

We now test our hypothesis - that sharding preserves system performance as data grows - by simulating a high-demand environment and measuring the query latencies of the sharded and non-sharded collections. The "high-frequency" simulation is done by seding a large number of rapid, repetitive queries to each collection across several randomly selected countries, thus imitating realistic scaling requirements.

We compute these measurements several times, gradually increasing the number of documents from 1,000 to 1,000,000 to observe performance impacts for different collection sizes. For each query, we record the average latency, reflecting how quickly each collection can respond under intense load. The latency comparison between the sharded and non-sharded collections illustrates which collection responds faster to queries for documents matching a specific country. 

## Prerequisites to Run Our Experiment

1. **Install MongoDB:**
   - Make sure MongoDB is installed on your local machine. You can download it [here](https://www.mongodb.com/try/download/community).

2. **Verify MongoDB Binaries:**
   - Ensure that the following MongoDB binaries are available:
     - `mongod` and `mongos`: These are included in the MongoDB installation.
     - `mongosh`: Follow the shell installation instructions available [here](https://www.mongodb.com/try/download/shell).

3. **Prepare Your Machine:**
   - Ensure you have **sufficient disk space** and **permissions** to run MongoDB services on your machine.

4. **Download Postman:**
   - [Download Postman](https://www.postman.com/downloads/) to reproduce the experiment. Postman will be used to populate sharded and non-sharded collections and to trigger high-frequency simulations.

5. **(Optional) Download MongoDB Compass:**
   - For better visualizations of sharded and non-sharded collections, query performance, and data storage, consider downloading [MongoDB Compass](https://www.mongodb.com/try/download/compass).

## Directory Structure

The database, along with its collections, shards, and configuration files, is stored in the `listing-microservice/data/dbContents` directory. The database is named `ListingDB` and includes a sharded collection, `listingCollection`. This collection is sharded into three parts, a choice made to improve data distribution visualization.

The collection also has a corresponding `configdb` file, which stores its metadata. Additionally, the `mongos` router facilitates communication between the database, shards, and clients.

The database structure is organized within the `ListingDB` directory, with subdirectories for the config server and shards. These directories have already been created and are located in `listing-microservice/data/dbContents/ListingDB`.

## How to Reproduce Our Experiment

This section outlines the steps required to replicate our experiment on collections with 100,000 entries each. To measure average latency for both sharded and non-sharded collections across different collection sizes, perform each step in a new application run. At **Step 8: Populate Collections with 100,000 Entries**, you may adjust the entry count to modify the database load and analyze the resulting impact on latency.

### 1. Clone the Repository
   - Clone the repository to your local machine.

### 2. Install Dependencies
   - Before starting the microservices, install the required Python libraries listed in `requirements.txt`.
   
   ```bash
   pip install -r requirements.txt
   ```
   
   **Note for macOS Users**: If `pip` doesn’t work, use `pip3` instead:
   
   ```bash
   pip3 install -r requirements.txt
   ```
   
   This command will automatically install the necessary dependencies (`pandas`, `matplotlib`) for data manipulation and visualization.

### 3. Start the Authentication and Listing Microservices

   #### 3.1 Authentication Microservice
   - **Locate the File**: Navigate to `authentication-microservice/src/main/java/wastewise/authentication/Authentication.java`.
   - **Run the Authentication Manager**:
     - Open `Authentication.java` in your IDE and run `Authentication.main()` to start the Authentication Manager.
     - **Note**: Since this is a **Gradle** project with dependencies, we recommend running it from an IDE rather than the command line for simplicity.

   #### 3.2 Listing Microservice
   - Navigate to `listing-microservice/src/main/java/wastewise/listing/Listing.java`.
   - Run `Listing.main()` to start the Listing microservice.

   - **Console Prompt**: You’ll see a prompt: *"Is this the first time you run the program?"*
     - **If this is the first run**: Enter **"Yes"** to trigger `set-up-listing-servers.sh`, which sets up the shard servers.
       - **Script Location**: `listing-microservice/data/server_scripts/set-up-listing-servers.sh`
     - **If servers are already set up**: Enter **"No"** to trigger `start-listing-servers.sh`, which starts the servers.
       - **Script Location**: `listing-microservice/data/server_scripts/start-listing-servers.sh`

   - **Important Notice**:  
     - On **Linux/Mac** devices, `.sh` shell scripts should execute without additional steps.  
     - On **Windows** devices, if the download does not occur and the server fails to start:
       - Ensure you have **Git Bash** installed to run shell scripts.
       - Navigate to `Settings -> Apps -> Default Apps`, search for the `.sh` file type, and set **Git Bash** as the default application for `.sh` files.
       - The script should now automatically open in **Git Bash** and execute correctly.

### 4. (Optional) Connect to MongoDB Compass
   - Open MongoDB Compass.
   - Add a new connection using: `mongodb://localhost:27117`.
   - Here, you can view the `Listing` Database and the `listingCollection`, the sharded collection created by the scripts.

### 5. Register a User (Using Postman)

   1. Open Postman and add a new **POST** request:
      - URL: `http://localhost:8082/register`
   2. In the **Body** tab, select **raw** and choose **JSON** format.
   3. Enter the following JSON data:
      ```json
      {
          "netId": "Software",
          "password": "password"
      }
      ```
   4. Send the request.

### 6. Authenticate the User (Using Postman)

   1. Add another **POST** request in Postman:
      - URL: `http://localhost:8082/authenticate`
   2. Enter the same JSON data as before:
      ```json
      {
          "netId": "Software",
          "password": "password"
      }
      ```
   3. Send the request.
   4. You will receive an **authentication token** in the response. Copy this token for the following steps.

### 7. Create a Non-Sharded Collection

   1. Add a new **POST** request in Postman:
      - URL: `http://localhost:8084/listings/createCollection?collectionName=nonShardedCollection`
   2. Go to the **Authorization** tab:
      - Select **Bearer Token** as the authorization type.
      - Enter the **authentication token** from the previous step.
   3. Send the request.
   4. (Optional) In MongoDB Compass, you can view the newly created non-sharded collection under the Listing DB.

### 8. Populate Collections with 100,000 Entries

   1. Add a new **POST** request in Postman:
      - URL: `http://localhost:8084/listings/populate?collectionNames=listingCollection&collectionNames=nonShardedCollection&n=100000`
   2. Go to the **Authorization** tab:
      - Select **Bearer Token** as the authorization type.
      - Enter the **authentication token**.
   3. Send the request.
   4. (Optional) In MongoDB Compass, check that both the sharded and non-sharded collections are populated with data.

### 9. Run the High-Frequency Simulation

   1. Add a new **POST** request in Postman:
      - URL: `http://localhost:8084/listings/runFullDemo?queryCount=100`
   2. Go to the **Authorization** tab:
      - Select **Bearer Token** as the authorization type.
      - Enter the **authentication token**.
   3. Send the request.
   4. A line chart illustrating the average latency results from the high-frequency simulation will be displayed.
      - **If not displayed automatically**, navigate to the `sharding_experiment_results` directory to view `latency_comparison_line_chart.png`.

# Available Features in WasteWise Microservices

Besides the databases, the backend of each of our microservices, namely `Listing`, `Information Display`, `User`, and `Form`, is written for the PoC. To understand how **WasteWise** will operate and the functionality it will offer its users, you can run the applications corresponding to the microservices. Before running any microservice, please first run the Authentication microservice, as this microservice authenticates the users who can then use WasteWise. Below are the authentication steps that must be taken using Postman.

### Run the Authentication Microservice
   - **Locate the File**: Navigate to `authentication-microservice/src/main/java/wastewise/authentication/Authentication.java`.
   - **Run the Authentication Manager**:
     - Open `Authentication.java` in your IDE and run `Authentication.main()` to start the Authentication Manager.
     - **Note**: Since this is a **Gradle** project with dependencies, we recommend running it from an IDE rather than the command line for simplicity.

### Register a User (Using Postman)

   1. Open Postman and add a new **POST** request:
      - URL: `http://localhost:8082/register`
   2. In the **Body** tab, select **raw** and choose **JSON** format.
   3. Enter the following JSON data:
      ```json
      {
          "netId": "Software",
          "password": "password"
      }
      ```
   4. Send the request.

### Authenticate the User (Using Postman)

   1. Add another **POST** request in Postman:
      - URL: `http://localhost:8082/authenticate`
   2. Enter the same JSON data as before:
      ```json
      {
          "netId": "Software",
          "password": "password"
      }
      ```
   3. Send the request.
   4. You will receive an **authentication token** in the response. Copy this token for the following steps.

Once you’ve sent the authentication API requests, you can test the functionality of each microservice using Postman. Each microservice’s API mappings are defined in their respective Controller modules, where every method is documented. To understand the functionality available for each microservice, please refer to the Controllers, as they outline the implemented features per microservice. For example, you can locate the Controller for the Information Display microservice at `info-display-microservice/src/main/java/wastewise/controllers/InformationDisplayController.java`.

# Manual Configuration of Sharded Database Cluster

In the event that the automated script for setting up the sharded database encounters issues, the following section provides instructions for manually configuring the databases. As the cluster is hosted locally, it is essential to initialize the databases, collections, and shards before they can be utilized. This manual setup of the sharded database is done in several steps, which we present below.

## Step 1: Create the directories

Navigate to `listing-microservice/data`, and create a new folder called `dbContents`. Within this folder, create a new folder, `ListingDB`. In this directory, add five other folders, namely: `configdb`, `logs`, `shard_1`, `shard_2`, and `shard_3`.

## Step 2: Start Config Servers, Shard Servers, and Mongos

Note! The terminals started on this step must stay open for the servers to remain active.

### Starting the Config Server

Open a terminal and run the following command to start the config server:

```
mongod --configsvr --replSet configReplSet --port 27119 --dbpath abs/path/to/ListingDB/configdb --bind_ip localhost
```

### Starting the Shard Servers

The database will have 3 shards, therefore we need to start 3 different servers, each on its port. To achieve this, run the following commands in separate terminal windows:

_Shard Server 1_

```
mongod --shardsvr --replSet rs1 --port 27120 --dbpath abs/path/to/ListingDB/shard_1 --bind_ip localhost
```

_Shard Server 2_

```
mongod --shardsvr --replSet rs2 --port 27121 --dbpath abs/path/to/ListingDB/shard_2 --bind_ip localhost
```

_Shard Server 3_

```
mongod --shardsvr --replSet rs3 --port 27122 --dbpath abs/path/to/ListingDB/shard_3 --bind_ip localhost
```

### Starting the Mongos Router

Lastly, start the `mongos` router in a new terminal using the following command:

```
mongos --configdb configReplSet/localhost:27119 --port 27117 --bind-ip localhost
```

## Step 3: Initialize Replica Sets for Config and Shard Servers

This step is crucial when setting up the MongoDB sharded collection. A replica set is a group of servers that maintain the same dataset, providing high availability of data. The steps to initialize them can be found below.

### Configdb Server Replica Set

Open `mongosh` in a new terminal and run the following commands:

```
mongosh --port 27119
```

Inside the same `mongosh` shell, then execute:

```
rs.initiate()
```

You can then exit the `mongosh` shell.

This process needs to be repeated for each of the shards:

_Shard Server 1_

```
mongosh --port 27120
```

Inside the same `mongosh` shell, then execute:

```
rs.initiate()
```

You can then exit the `mongosh` shell.

Repeat the same steps with ports 27121 and 27122 for Shard Server 2 and 3 respectively.

## Step 4: Add Shards to the Cluster

Open `mongosh` terminal again, and connect to the router, which is on port 27117, by running:

```
mongosh -- port 27117
```

Followed by the commands:

```
sh.addShard(“rs1/localhost:27120”)
sh.addShard(“rs2/localhost:27121”)
sh.addShard(“rs3/localhost:27122”)
```

## Step 5: Create a New Database and Collection, and Enable Sharding

In the same `mongosh` shell, run:

```
use ListingDB
sh.enableSharding(“ListingDB”)
db.createCollection(“listingCollection”)
sh.shardCollection(“ListingDB.listingCollection”, {“location.country”: 1})
```

The last part of these commands sets the `shardKey`, which will determine how the documents are distributed among shards later on.

## Step 6: Verify Sharding

To verify that sharding is set up correctly, you can run:

```
sh.status()
```

This command will offer information about the sharded cluster, together with the shards and their respective chunk distribution.

## Notes

-- If you encounter `ECONNREFUSED` errors, make sure that all `mongod` and `mongos` processes are running and that they are listening on the specified ports. You can do that by opening a `mongosh` shell and calling their corresponding ports.

- You can stop the servers by terminating the terminal sessions or using the `kill` command to stop the running processes.
- This set up is only done once, when you run the files for the first time.

## In Short

Overview of the steps:

```
# Step 1: Start Config Servers, Shard Servers, and Mongos
mongod --configsvr --replSet configReplSet --port 27119 --dbpath /path/to/config/data --bind_ip localhost
mongod --shardsvr --replSet rs1 --port 27120 --dbpath /path/to/shard1/data --bind_ip localhost
mongod --shardsvr --replSet rs2 --port 27121 --dbpath /path/to/shard2/data --bind_ip localhost
mongod --shardsvr --replSet rs3 --port 27122 --dbpath /path/to/shard3/data --bind_ip localhost
mongos --configdb configReplSet/localhost:27119 --port 27117 --bind_ip localhost

# Step 2: Initialize Replica Sets for Config Servers and Shards
mongosh --port 27119
rs.initiate()
exit
mongosh --port 27120
rs.initiate()
exit
mongosh --port 27121
rs.initiate()
exit
mongosh --port 27122
rs.initiate()
exit

# Step 3: Add Shards to the Cluster
mongosh --port 27117
sh.addShard("rs1/localhost:27120")
sh.addShard("rs2/localhost:27121")
sh.addShard("rs3/localhost:27122")

# Step 4: Create a New Database and Collection, Enable Sharding
use NewDatabase
sh.enableSharding("NewDatabase")
db.createCollection("newCollection")
sh.shardCollection("NewDatabase.newCollection", { "location.country": 1 })

# Step 5: Verify Sharding
sh.status()
```

# Running the Servers after Setup - Normal Program Run

Once the database and collection are set up, we just need to make sure to activate them when the program is running, in order to stay connected to the cluster and access its specific operations. In order to activate them for the program run, we have to go through several steps.

### Step 1: Start the Configdb Server

Open a terminal and run the following command to start the configdb server:

```
mongod --configsvr --replSet configReplSet --port 27119 --dbpath abs\path\to\ListingDB\configdb --bind_ip localhost
```

### Step 2: Start the Shards Servers

In separate terminals, for each shard of the collection, run:

_Shard Server 1_

```
mongod --replSet rs1 --shardsvr --port 27120 --dbpath abs\path\to\ListingDB\shard_1 --bind_ip localhost
```

_Shard Server 2_

```
mongod --replSet rs2 --shardsvr --port 27121 --dbpath abs\path\to\ListingDB\shard_2 --bind_ip localhost
```

_Shard Server 3_

```
mongod --replSet rs3 --shardsvr --port 27122 --dbpath abs\path\to\ListingDB\shard_3 --bind_ip localhost
```

### Step 3: Activate the `mongos` Router

Lastly, open a new terminal and run the following command:

```
mongos --configdb configReplSet/localhost:27119 --port 27117 --bind_ip localhost
```

Now you should be able to run your program and be connected to the microservice. You can notice that when you run Controller requests (see `ListingController` from the `controllers` folder within the `java` directory of the `listing-microservice` module).

## In Summary

To activate the sharding servers when running the program, you need to first run the following commands:

```
# Activate the configdb server
mongod --configsvr --replSet configReplSet --port 27119 --dbpath abs\path\to\ListingDB\configdb --bind_ip localhost

# Activate the shard_1 server
mongod --replSet rs1 --shardsvr --port 27120 --dbpath abs\path\to\ListingDB\shard_1 --bind_ip localhost

# Activate the shard_2 server
mongod --replSet rs2 --shardsvr --port 27121 --dbpath abs\path\to\ListingDB\shard_2 --bind_ip localhost

# Activate the shard_3 server
mongod --replSet rs3 --shardsvr --port 27122 --dbpath abs\path\to\ListingDB\shard_3 --bind_ip localhost

# Activate mongo router
mongos --configdb configReplSet/localhost:27119 --port 27117 --bind_ip localhost
```

