# VaxTrack: UML Class Diagram

```
«interface»                    «interface»
Identifiable                   Displayable
─────────────                  ────────────
+getId(): int                  +getDisplayInfo(): String
+setId(int): void
        │                            │
        └──────────┬─────────────────┘
                   ▼
           «abstract»
           BaseEntity
           ─────────────────────────────
           -id: int
           -createdAt: LocalDateTime
           ─────────────────────────────
           +getId(): int
           +setId(int): void
           +getCreatedAt(): LocalDateTime
           +getDisplayInfo(): String  «abstract»
           +toString(): String
                   │
       ┌───────────┼────────────┬──────────────────┐
       ▼           ▼            ▼                  ▼
     User       Patient       Vaccine        VaccineRecord
  ──────────  ──────────    ──────────      ──────────────
  -username   -fullName     -vaccineName    -patientId
  -password   -dateOfBirth  -manufacturer   -vaccineId
  -role       -gender       -dosesRequired  -doseNumber
              -phone        -description    -dateGiven
              -address                      -administeredBy
                                            -notes
                                            -patientName*
                                            -vaccineName*
  +getDisplayInfo()         +getDisplayInfo() +getDisplayInfo()
  +getDisplayInfo()         +getAge(): int

* = joined/display field


«interface»
CrudDAO<T>
──────────────────────────────
+insert(T): void
+findById(int): T
+findAll(): List<T>
+update(T): void
+delete(int): void
        │
        ├──────────── UserDAO
        │             ──────────────────────────
        │             +findByCredentials(): User
        │
        ├──────────── PatientDAO
        │             ──────────────────────────
        │             +searchByNameOrPhone(): List
        │
        ├──────────── VaccineDAO
        │
        └──────────── VaccineRecordDAO
                      ──────────────────────────
                      +findByPatientId(): List
                      +doseAlreadyRecorded(): boolean

SearchDAO  (NOT CrudDAO — read-only)
──────────────────────────────────
+searchPatients(): List<Patient>
+searchRecords(): List<VaccineRecord>
+unifiedSearch(): List<SearchResult>
+getTotalPatients(): int
+getTotalVaccinations(): int
+getTotalVaccines(): int


«singleton»              «singleton»
SessionManager           DatabaseConnection
───────────────────      ──────────────────────
-instance                -connection
-currentUser: User       ──────────────────────
───────────────────      +getConnection(): Conn
+getInstance()           +closeConnection()
+getCurrentUser()
+setCurrentUser()
+isAdmin(): boolean
+clear()


Services
──────────────────────────────────────────────────
AuthService          PatientService        VaccineRecordService     SearchService
────────────         ────────────────      ────────────────────     ─────────────
-userDAO             -patientDAO           -recordDAO               -searchDAO
────────────         ────────────────      ────────────────────     -recordDAO
+login()             +addPatient()         +addRecord()             ─────────────
+logout()            +getAllPatients()     +getPatientHistory()     +search()
+isLoggedIn()        +searchPatients()     +getAllRecords()          +searchPatients()
                     +updatePatient()      +getAllVaccines()         +getPatientHistory()
                     +deletePatient()      +deleteRecord()          +getTotalPatients()
                                                                    +getTotalVaccinations()
                                                                    +getTotalVaccines()

Controllers (connect UI ↔ Service)
───────────────────────────────────────────────────────────
LoginController → AuthService → SessionManager
DashboardController → SearchService
PatientController → PatientService
VaccineRecordController → VaccineRecordService + PatientService
SearchController → SearchService
```