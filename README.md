# 🏥 Bajaj Finserv Health – Programming Challenge (Spring Boot)

This Spring Boot project was built for the Bajaj Finserv Health (BFH) Programming Challenge (SRM, April 25–30, 2025).  
It automates a REST interaction workflow and solves a problem involving social graph analysis — all triggered at application startup.

---

## 🚀 Features

✅ Automatically sends a POST request on app startup to generate a webhook  
✅ Parses user graph data and access token  
✅ Solves assigned problem based on registration number:
**Question 1**: Mutual Followers
**Question 2**: Nth-Level Followers  
✅ Sends result back to the webhook with JWT authentication  
✅ Includes a retry mechanism (up to 4 times on failure)

---

## 📦 Technologies Used

- Java 17
- Spring Boot 3.x
- Maven
- RestTemplate (HTTP client)
- Jackson (JSON parser)

---
