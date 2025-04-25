# Bajaj_Java_Backend_Assessment
# 🏥 Bajaj Finserv Health – Programming Challenge (Spring Boot)

This Spring Boot project was built for the Bajaj Finserv Health (BFH) Programming Challenge (SRM, April 25–30, 2025).  
It automates a REST interaction workflow and solves a problem involving social graph analysis — all triggered at application startup.

---

## 🚀 Features

✅ Automatically sends a POST request on app startup to generate a webhook  
✅ Parses user graph data and access token  
✅ Solves assigned problem based on registration number:
- **Question 1**: Mutual Followers
- **Question 2**: Nth-Level Followers  
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

## 📄 Problem Statement

On startup, the app performs the following steps:

1. **POST** to:
https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook

css
Copy
Edit

2. With JSON body:
```json
{
  "name": "John Doe",
  "regNo": "REG12347",
  "email": "john@example.com"
}
The response includes:

webhook: The final URL to POST results

accessToken: JWT token for auth

data: Social graph input

Based on the last digit of regNo:

Odd → solve Question 1

Even → solve Question 2

🧠 Problem Logic
Question 1: Mutual Followers
Return pairs of users who follow each other (2-node cycles):

Example:

json
Copy
Edit
"outcome": [[1, 2], [3, 4]]
Question 2: Nth-Level Followers
Given a findId and level n, return user IDs exactly n levels deep in the "follows" chain:

Example:

json
Copy
Edit
"outcome": [4, 5]
📂 How to Run
1. Clone the repo
bash
Copy
Edit
git clone https://github.com/yourusername/bajaj-health-challenge.git
cd bajaj-health-challenge
2. Build the JAR
bash
Copy
Edit
mvn clean install
3. Run the JAR
bash
Copy
Edit
java -jar target/bajaj-health-0.0.1-SNAPSHOT.jar
🔁 Retry Mechanism
If the webhook POST fails, it will retry up to 4 times with backoff handling.

📥 Download Final JAR
👉 Raw GitHub Link to JAR

📬 Submission
Submitted via Microsoft Forms as required.

🧑‍💻 Author
Ashmeet Joon
GitHub | LinkedIn

