# 💰 Budget Buddy

![Java](https://img.shields.io/badge/Java-17-blue?logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-orange?logo=apachemaven&logoColor=white)
![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)
![Platform](https://img.shields.io/badge/Platform-CLI-lightgrey)

> **Budget Buddy** is a simple yet powerful Java CLI application for managing your personal finances — built with pure Java, Maven, and Gson for JSON storage.

---

## ✨ Features
- 📅 **Add/Edit/Delete** expenses with date, category, notes, and amount
- 📋 **List all expenses** in a clean table format
- 📊 **Summary** of expenses per category/month
- 📂 **Export & Import** data in CSV format
- 💾 **Persistent storage** in JSON file (no database needed)
- ⏱ Lightweight & offline — runs anywhere Java 17+ is available

---

## 📦 Installation

**Prerequisites**:
- Java 17+
- Maven 3.8+

```bash
# Clone the repo
git clone https://github.com/Nadir-N3/Budget-Buddy.git
cd Budget-Buddy

# Build the project
mvn clean package
```

---

## 🚀 Usage

Run the app:
```bash
java -cp target/budgetbuddy-1.0.0.jar:$HOME/.m2/repository/com/google/code/gson/gson/2.11.0/gson-2.11.0.jar com.budgetbuddy.Main
```

You will see:
```
== BudgetBuddy (Java CLI) ==

1) Add  2) Edit  3) Delete  4) List  5) Summary  6) Export CSV  7) Import CSV  0) Exit
Choose:
```

---

## 📂 Project Structure
```
Budget-Buddy/
 ├── src/
 │   ├── main/java/com/budgetbuddy/    # Source code
 │   └── main/resources/               # Resources (if any)
 ├── pom.xml                            # Maven build config
 └── README.md
```

---

## 📜 License
This project is licensed under the [MIT License](LICENSE).

---

## 📬 Connect
[![X](https://img.shields.io/badge/X-@Naadiir__08-black?logo=x)](https://x.com/Naadiir_08)
[![Instagram](https://img.shields.io/badge/Instagram-__naadiir.fx-purple?logo=instagram)](https://instagram.com/__naadiir.fx)
