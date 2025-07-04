# 🔐 SQL Injection Scanner

A lightweight web-based SQL Injection vulnerability scanner built with **Java Spring Boot**. It allows users to enter a URL and checks for potential SQLi vulnerabilities using common payloads.

---

## 🌐 Features

- 🌍 Web interface to scan target URLs
- 🧪 Tests with a set of known SQL injection payloads
- 🛡️ Detects vulnerable parameters in query strings
- 📊 JSON-based output of scan results

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/sql-injection-scanner.git
cd sql-injection-scanner


### 2. Build and run the app
'''bash
mvn clean install
mvn spring-boot:run

### 3. Open in browser
http://localhost:8080

Example Test URL
Use the following intentionally vulnerable test site:
http://testphp.vulnweb.com/artists.php?artist=1

Screenshot
![image](https://github.com/user-attachments/assets/f6082470-4e99-49a2-a1fc-89e4589faaf7)

