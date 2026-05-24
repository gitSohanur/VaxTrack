# VaxTrack: OOP Pillars Demonstrated

## 1. Encapsulation
- All model fields are `private`
- Access only through `public` getters/setters
- Example: `Patient.java`
```java
  private String fullName;
  public String getFullName() { return fullName; }
  public void setFullName(String name) { this.fullName = name; }
```

## 2. Inheritance
- `User`, `Patient`, `Vaccine`, `VaccineRecord` all extend `BaseEntity`
- They inherit `id`, `createdAt`, `getId()`, `setId()`, `toString()`
- Example:
```java
  public class Patient extends BaseEntity { ... }
  public class Vaccine  extends BaseEntity { ... }
```

## 3. Abstraction
- `BaseEntity` is `abstract` — cannot be instantiated directly
- Forces subclasses to implement `getDisplayInfo()`
- `CrudDAO<T>` is an interface — defines contract without implementation
```java
  public abstract class BaseEntity implements Identifiable, Displayable {
      public abstract String getDisplayInfo(); // subclass MUST implement
  }
```

## 4. Polymorphism
- Any `BaseEntity` reference calls the correct `getDisplayInfo()`
```java
  BaseEntity e1 = new Patient(...);
  BaseEntity e2 = new Vaccine(...);
  System.out.println(e1.getDisplayInfo()); // Patient output
  System.out.println(e2.getDisplayInfo()); // Vaccine output
```
- `CrudDAO<T>` implemented by 4 different DAOs, each with own SQL

## 5. Interfaces
- `Identifiable` — contract: every entity has `getId()`/`setId()`
- `Displayable` — contract: every entity has `getDisplayInfo()`
- `CrudDAO<T>` — contract: every DAO has all 5 CRUD methods

## 6. Design Patterns Used
- **Singleton**: `DatabaseConnection`, `SessionManager`
- **DAO Pattern**: All database access through DAO classes
- **MVC Pattern**: Model / Controller / Screen (View) separation
- **Service Layer**: Business logic isolated from UI and DB