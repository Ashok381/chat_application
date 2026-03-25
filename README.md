# 💬 Java Chat Application

A real-time multi-user chat system built using **Java Socket Programming**, featuring a **Swing-based GUI**, **multithreaded server**, and **MySQL integration** for authentication and message persistence.

---

## 🚀 Overview

This project implements a **client-server chat architecture** where multiple users can connect to a central server and exchange messages in real time. It also integrates a database layer to handle user authentication and store chat-related data.

---

## ✨ Features

* 🔐 User authentication (Login / Signup)
* 💬 Real-time messaging using sockets
* 🧑‍🤝‍🧑 Private messaging (user-to-user)
* 🖥️ GUI interface using Java Swing
* 📡 Multithreaded server handling multiple clients
* 🗄️ MySQL database integration
* 📜 Chat history storage (database-backed)

---

## 🏗️ System Architecture

### 🔹 Server

* Accepts multiple client connections
* Uses **multithreading** to handle each client independently
* Maintains active users using a mapping structure
* Routes messages based on receiver ID

### 🔹 Client (GUI)

* Built with **Java Swing**
* Uses **CardLayout** for screen transitions (Login → Chat)
* Allows sending/receiving messages in real time

### 🔹 Database Layer

* Handles:

  * User credentials
  * Conversations
  * Messages
* Implemented using **JDBC**

---

## 🛠️ Tech Stack

| Component    | Technology         |
| ------------ | ------------------ |
| Language     | Java               |
| GUI          | Swing              |
| Networking   | Socket Programming |
| Database     | MySQL              |
| Connectivity | JDBC               |

---

## 📂 Project Structure

```bash
chatapplication/
│── ChatServer.java        # Server-side logic (multi-client handling)
│── GUIchat.java           # Swing-based client interface
│── Multiuser.java         # Utility for testing multiple clients
│── Dbconnection.java      # Database connection setup
│── QueryCaller.java       # SQL query execution layer
```

---

## ⚙️ Setup & Execution

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/chat-application.git
cd chat-application
```

---

### 2. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE chatdata;
```

Update database credentials in:

```
Dbconnection.java
```

---

### 3. Compile the Project

```bash
javac chatapplication/*.java
```

---

### 4. Run the Server

```bash
java chatapplication.ChatServer
```

---

### 5. Run the Client

```bash
java chatapplication.GUIchat
```

---

## ⚠️ Limitations

* No encryption (messages are sent as plain text)
* Passwords are not hashed (security risk)
* Server uses non-thread-safe structures
* Limited error and exception handling
* Tight coupling between GUI and backend logic

---

## 🔮 Future Improvements

* Implement password hashing (e.g., bcrypt)
* Use `ConcurrentHashMap` for thread safety
* Add group chat functionality
* Improve UI/UX design
* Add message timestamps in UI
* Implement proper client disconnection handling
* Refactor into layered architecture (MVC / service-based)
* Deploy server to cloud environment

---

## 📌 Key Learning Outcomes

* Socket Programming in Java
* Multithreading and concurrency basics
* GUI development using Swing
* Database integration using JDBC
* Client-server architecture design

---

## 👤 Author

*Ashok Samrat*
Computer Science Student
<img width="1509" height="478" alt="Screenshot 2026-03-26 012603" src="https://github.com/user-attachments/assets/0d88d0d4-42be-48f7-8e31-bdc586da590d" />

---

## ⭐ Contributing

Contributions are welcome. Feel free to fork this repository and submit pull requests.

---

## 📜 License

This project is intended for educational purposes.
