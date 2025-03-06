# 🏏 CricLab - Backend

This is the **backend** for the CricLab web application, built with **Java Spring Boot**. It provides **live scores, match history, and points table data** via REST APIs.

---

## 🚀 Tech Stack

- **Backend:** Java 23, Spring Boot
- **Database:** MySQL, JPA (Hibernate)
- **API Documentation:** Swagger (SpringDoc OpenAPI)
- **Web Scraping:** JSoup (for match details)
- **Monitoring:** Spring Boot Actuator
- **Containerization:** Docker
- **Security:** CORS configured for frontend (`http://localhost:4200`)
- **Performance Enhancements:**
  - **Rate Limiting** – Prevents API abuse
  - **Background Jobs** – Automates live match updates
---

## ✨ Features

✅ **Live Matches API** – Fetch real-time cricket match updates  
✅ **Match History API** – View previously played matches  
✅ **Points Table API** – Retrieve ICC 2025 standings  
✅ **OpenAPI (Swagger UI)** – Interactive API documentation  
✅ **Spring Boot Actuator** – Health monitoring & metrics  
✅ **JSoup Web Scraping** – Extract match details from external sources  
✅ **Rate Limiting** – Controls excessive API requests    
✅ **Scheduled Background Jobs** – Updates live scores periodically  
✅ **Docker Support** – Easily deploy as a container  

---

## 🛡️ Rate Limiting

To prevent excessive API calls, **Spring Boot Rate Limiting** is implemented using a **custom interceptor** with `Bucket4j` library.

- Limits requests per user based on IP.
- Helps prevent abuse of public APIs.

---

## ⏳ Background Jobs (Live Score Updates)

Live match scores are updated automatically using **Spring's @Scheduled annotation**.

- Runs every **60 seconds** to fetch the latest scores.
- No need for manual API calls to update live scores.

---

## 🛠️ Installation and Running the Project

### **1️⃣ Prerequisites**
- **Java 23** installed
- **Maven** (`brew install maven` or `choco install maven`)
- **MySQL** (or use Docker)
- **Docker** (if running via container)

### **2️⃣ Clone the Repository**
```sh
git clone https://github.com/Avaneesh-Chopdekar/criclab-backend.git
cd criclab-backend
```

### **3️⃣ Configure the Database**
Create a MySQL database:
```sql
CREATE DATABASE criclab_db;
```

Update `application.yaml` (or use environment variables):
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:${DB_URL:mysql://localhost:3306/criclab_db}?serverTimezone=UTC
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root}
  jpa:
    hibernate:
      ddl-auto: ${DB_DDL:update}

server:
  port: ${SERVER_PORT:8080}
```

### **4️⃣ Build and Run the Application**
```sh
mvn clean install
mvn spring-boot:run
```
The backend will be available at **`http://localhost:8080/`**.

---

## 🐳 Running with Docker

### **1️⃣ Setup Environment Variables** 
Rename the `.env.example` file to `.env` and update the required values
```sh
cp .env.example .env
```


### **2️⃣ Run the Container**
```sh
docker-compose --env-file .env up --build -d
```

---

## 🔥 API Documentation (Swagger UI)

After running the backend, open **Swagger UI** in your browser:

📌 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.htm)**

---

## 🏗️ Project Structure
```
📂 src/
 ┣ 📂 main/java/com/criclab/backend
 ┃ ┣ 📂 controller/    # REST API controllers
 ┃ ┣ 📂 service/       # Business logic & data fetching
 ┃ ┣ 📂 entity/        # JPA Entities
 ┃ ┣ 📂 repository/    # Database interaction (Spring Data JPA)
 ┃ ┣ 📂 config/        # Swagger config
```

---

## 🔗 API Endpoints

### **1️⃣ Matches API**
- **Get all matches:** `GET /api/v1/matches`
- **Get live matches:** `GET /api/v1/matches/live`
- **Get points table:** `GET /api/v1/matches/point-table`

---

## 🤝 Contributing

We welcome contributions! To contribute:

1. **Fork** the repository.
2. **Create** a new branch (`git checkout -b feature-name`).
3. **Commit** changes (`git commit -m "Added a new feature"`).
4. **Push** the branch (`git push origin feature-name`).
5. **Create a Pull Request** 🚀.

---

## 📜 License

This project is licensed under the **MIT License**.  
Feel free to use, modify, and distribute. 🏏

---

Made with ❤️ by [Avaneesh Chopdekar](https://github.com/Avaneesh-Chopdekar)