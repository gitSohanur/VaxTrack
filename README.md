# VaxTrack: Intelligent Vaccine Tracking System

VaxTrack is a Java-based vaccine tracking and immunization management system developed as an Object-Oriented Programming (OOP) university project.  
The system provides a centralized digital platform for storing vaccination records, tracking dose schedules, managing patient information, and improving immunization monitoring through a secure and scalable architecture.

The project focuses on applying real-world software engineering practices using core OOP principles, layered architecture, database integration, and modern Java development tools.

---

# Features

- Secure user authentication
- Patient registration and profile management
- Vaccine record management
- Dose schedule tracking
- Vaccination history viewing
- Admin dashboard for vaccine management
- Search and filter patient records
- MySQL database integration using JDBC
- JavaFX graphical user interface
- Object-Oriented architecture following SOLID principles


## 📋 Project Info

| Field | Detail |
|---|---|
| **Course** | Object-Oriented Programming |
| **Language** | Java 17 |
| **UI** | JavaFX 21 |
| **Database** | MySQL 8 |
| **Build** | Maven |
| **IDE** | IntelliJ IDEA |

---

## 🏗️ Architecture

```
UI Layer       → JavaFX Screens
Controller     → Connects UI to Services
Service Layer  → Business Logic + Validation
DAO Layer      → Database Operations (JDBC)
Database       → MySQL (vaxtrack_db)
```

---

## ✅ Features

### Admin
- Secure login system
- Add / Edit / Delete patients
- Add / Delete vaccine records
- Search patients by name or phone
- Unified search across patients and records
- Dashboard with live statistics

### Patient Data
- Full name, date of birth, gender, phone, address
- Auto-calculated age

### Vaccine Records
- Link patients to vaccines
- Track dose number
- Record date given and administering doctor
- Prevent duplicate doses
- Enforce dose limits per vaccine type

---

## 🔷 OOP Pillars Demonstrated

| Pillar | Where |
|---|---|
| Encapsulation | All model classes — private fields + getters/setters |
| Inheritance | User, Patient, Vaccine, VaccineRecord extend BaseEntity |
| Abstraction | BaseEntity abstract class + CrudDAO interface |
| Polymorphism | getDisplayInfo() overridden in each model |
| Interfaces | Identifiable, Displayable, CrudDAO<T> |

---

## 🗂️ Project Structure

```
src/main/java/com/vaxtrack/
├── model/          → BaseEntity, User, Patient, Vaccine,
│                     VaccineRecord, SearchResult
├── database/
│   ├── dao/        → CrudDAO, UserDAO, PatientDAO,
│   │                 VaccineDAO, VaccineRecordDAO, SearchDAO
│   ├── DatabaseConnection.java
│   └── DatabaseConfig.java
├── service/        → AuthService, PatientService,
│                     VaccineRecordService, SearchService
├── controller/     → LoginController, DashboardController,
│                     PatientController, VaccineRecordController,
│                     SearchController
├── ui/             → All JavaFX screen classes + UIConstants
└── utils/          → Role, SessionManager, AlertHelper
```

---

## ⚙️ Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/gitSohanur/VaxTrack.git
cd VaxTrack
```

### 2. Set up MySQL
```sql
CREATE DATABASE vaxtrack_db;
CREATE USER 'vaxtrack_user'@'localhost' IDENTIFIED BY 'vaxtrack123';
GRANT ALL PRIVILEGES ON vaxtrack_db.* TO 'vaxtrack_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Run the schema
```
Open docs/schema.sql in MySQL Workbench and execute it.
```

### 4. Configure database (if needed)
```
Edit: src/main/java/com/vaxtrack/database/DatabaseConfig.java
Change HOST, USERNAME, PASSWORD to match your setup.
```

### 5. Run the app
```bash
mvn javafx:run
```

### Default Login
```
Username: admin
Password: admin123
```

---

## 🧪 Testing

See `docs/test-checklist.md` for the full test record.

---

## 📊 Database Schema

| Table | Purpose |
|---|---|
| `users` | Admin login accounts |
| `patients` | Patient registration data |
| `vaccines` | Vaccine types and dose requirements |
| `vaccine_records` | Vaccination events (patient ↔ vaccine) |

---

## 👨‍💻 Developer: **Code&Coffee** Team

**Faiza Nazeer Najmi** |
Student ID: 1702500297

**Naznin Sultana** |
Student ID: 1702500269

**Sohanur R.** |
Student ID: 1702500373

Course: Object-Oriented Programming

Institution: UCSI University

