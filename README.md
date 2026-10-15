# 🛡️ ReenactmentBackend (ANE API)

**ReenactmentBackend** is a robust REST API developed to support the historical reenactment application of the ANE (Asociación Napoleónica Española). Built with modern technologies, this backend manages associations, members, events, and secure accreditations (QR), following industry standards for security and architecture.

🚀 **Technologies and Requirements**

*   **Language:** Java 21
*   **Framework:** Spring Boot 3.x
*   **Database:** MySQL 8.0.32
*   **Containers:** Docker & Docker Compose
*   **Security:** Spring Security 6 (Stateless) + JSON Web Token (JWT)
*   **Documentation:** OpenAPI 3.0 (Swagger)
*   **Other features:** Apache Commons CSV (Export), Logback (Traceability)
*   **Testing:** JUnit (Unit) and Newman (Integration)

To run this project, **it is necessary** to have:
*   **Docker Desktop** (on Windows) or Docker Engine (on Linux/Mac).
*   **Maven** (if you want to run it in manual development mode).
*   **Newman** (to run the integration test collection).

🔒 **Security Configuration (Environment Variables)**

The application uses environment variables to manage sensitive credentials. Before starting, you need to define these variables in your system or in an `.env` file depending on your deployment strategy:

```env
SPRING_PROFILE=dev # or preprod, prod
DB_ROOT_PASSWORD=YourRootSuperSecurePassword123!
DB_NAME=ane_db
DB_USER=ane_user
DB_PASSWORD=YourUserPassword123!
JWT_SECRET=SuperSecretSignatureForTheNapoleonicAssociation1234567890
ADMIN_HASHED_PASSWORD=YourAdminPasswordHashedWithBCrypt
```

### 1. Development Environment (Manual)
Use this mode if you need to modify the code and see changes in real-time. This environment automatically executes the `DatabaseSeeder` to create the default admin user.

Start the database container:
```bash
docker compose -f docker-compose.dev.yml up -d
```

Run the Spring Boot application:
```bash
mvn spring-boot:run
```
*Note: Make sure your `application.properties` has `spring.profiles.active=dev` enabled.*

### 2. Local Pre-Production (Full Docker)
Docker will build the API image directly from your local source code. Ideal for pre-production testing and checking internal Docker networks.

```bash
docker compose -f docker-compose.preprod.yml up -d --build
```

### 3. Production on AWS (Remote Image Deployment)
Designed for EC2 servers. It does not require source code on the server, as it downloads the pre-built image from Docker Hub.

**Step 1: Configure Swap Memory (Critical for t3.micro instances)**
To prevent the server from freezing due to lack of RAM, create a 1GB Swap file:
```bash
sudo fallocate -l 1G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
# Make it permanent:
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
```

**Step 2: Export Environment Variables**
In your EC2 session, you must export the variables before running Docker. Use the `-E` flag in sudo commands to preserve them.

```bash
export SPRING_PROFILE=prod
export DB_ROOT_PASSWORD=YourRootSuperSecurePassword123!
export DB_NAME=ane_db
export DB_USER=ane_user
export DB_PASSWORD=YourUserPassword123!
export JWT_SECRET=SuperSecretSignatureForTheNapoleonicAssociation1234567890
export ADMIN_HASHED_PASSWORD=YourAdminPasswordHashedWithBCrypt
```

**Step 3: Setup `docker-compose.aws.yaml`**
Manually create the `docker-compose.aws.yaml` file on your EC2 instance pointing to your Docker Hub image (`mig1881/ane-api-aws:latest`).

⚠️ **CRITICAL FIRST RUN:** For the very first execution, ensure your docker-compose file includes:
`- SPRING_JPA_HIBERNATE_DDL_AUTO=create`
This allows Hibernate to create the database tables.

**Step 4: Pull and Run**
Download the latest image from Docker Hub:
```bash
sudo -E docker-compose -f docker-compose.aws.yaml pull
```

Start the containers:
```bash
sudo -E docker-compose -f docker-compose.aws.yaml up -d
```

Check the logs to verify a successful startup:
```bash
sudo docker logs ane_backend_aws
```

*Note: Once the database is created successfully, stop the containers (`down`), change `DDL_AUTO=create` to `DDL_AUTO=update` in your yaml file, and start them again.*

🧪 **Testing and Quality**

Run Integration Tests (Newman) to validate endpoints, security, and persistence. Ensure the API is running before executing:
```bash
newman run -e local.postman_environment.json ane_integration_tests.postman_collection.json
```

🔴 **Teardown and Cleanup**

To stop the services and clean up the containers:

Development:
```bash
docker compose -f docker-compose.dev.yml down
```

Pre-production:
```bash
docker compose -f docker-compose.preprod.yml down
```

AWS Production:
```bash
sudo -E docker-compose -f docker-compose.aws.yaml down