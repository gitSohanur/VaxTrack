# VaxTrack: System Architecture

## Multi-Layer Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                   │
│              (com.vaxtrack.ui)                           │
│                                                          │
│  LoginScreen  DashboardScreen  PatientScreen             │
│  VaccineRecordScreen           SearchScreen              │
│  UIConstants                                             │
└──────────────────────┬──────────────────────────────────┘
                       │  JavaFX Events
┌──────────────────────▼──────────────────────────────────┐
│                    CONTROLLER LAYER                      │
│              (com.vaxtrack.controller)                   │
│                                                          │
│  LoginController      DashboardController                │
│  PatientController    VaccineRecordController            │
│  SearchController                                        │
└──────────────────────┬──────────────────────────────────┘
                       │  Method calls
┌──────────────────────▼──────────────────────────────────┐
│                     SERVICE LAYER                        │
│              (com.vaxtrack.service)                      │
│                                                          │
│  AuthService          PatientService                     │
│  VaccineRecordService SearchService                      │
└──────────────────────┬──────────────────────────────────┘
                       │  DAO calls
┌──────────────────────▼──────────────────────────────────┐
│                       DAO LAYER                          │
│              (com.vaxtrack.database.dao)                 │
│                                                          │
│  UserDAO    PatientDAO    VaccineDAO                     │
│  VaccineRecordDAO         SearchDAO                      │
│  CrudDAO<T>  «interface»                                 │
└──────────────────────┬──────────────────────────────────┘
                       │  JDBC
┌──────────────────────▼──────────────────────────────────┐
│                    DATABASE LAYER                        │
│              (com.vaxtrack.database)                     │
│                                                          │
│  DatabaseConnection (Singleton)                          │
│  DatabaseConfig                                          │
└──────────────────────┬──────────────────────────────────┘
                       │  TCP/IP
┌──────────────────────▼──────────────────────────────────┐
│                    MySQL DATABASE                        │
│                   (vaxtrack_db)                          │
│                                                          │
│  users   patients   vaccines   vaccine_records           │
└─────────────────────────────────────────────────────────┘


## Database ER Diagram

┌──────────┐         ┌──────────────────┐         ┌──────────┐
│  users   │         │  vaccine_records  │         │ vaccines │
├──────────┤         ├──────────────────┤         ├──────────┤
│ id  (PK) │         │ id          (PK) │    ┌────│ id  (PK) │
│ username │         │ patient_id  (FK) │────┤    │ name     │
│ password │         │ vaccine_id  (FK) │────┘    │ manufact.│
│ role     │         │ dose_number      │         │ doses_req│
└──────────┘         │ date_given       │         │ desc     │
                     │ administered_by  │         └──────────┘
┌──────────┐    ┌────│ notes            │
│ patients │    │    └──────────────────┘
├──────────┤    │
│ id  (PK) │────┘
│ full_name│
│ dob      │
│ gender   │
│ phone    │
│ address  │
└──────────┘

Relationships:
  patients     1 ──── N  vaccine_records
  vaccines     1 ──── N  vaccine_records
```