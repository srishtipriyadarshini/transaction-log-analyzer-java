# Java Core Assignment — README

## Prerequisites
- JDK 17+
- Maven 3.8+

## Build
```bash
cd java-assignment
mvn clean compile
```

## Run All Three Problems

Each problem has its own main class. Run them from the `java-assignment/` directory using the `exec-maven-plugin`:

---

## Problem 1 — Transaction Log Analyzer

**Entry point:** `com.assignment.problem1.TransactionAnalyzerApp`

**Sample CSV:** `src/main/resources/transactions.csv` (loaded automatically from the classpath — no file path argument needed).

**Using Maven:**
```bash
mvn exec:java -Dexec.mainClass="com.assignment.problem1.TransactionAnalyzerApp"
```

**Using direct java command:**
```bash
cd java-assignment
javac -d target/classes src/main/java/com/assignment/problem1/*.java
java -cp target/classes com.assignment.problem1.TransactionAnalyzerApp
```

The app reads `transactions.csv` from the classpath, then prints:
- Summary totals by category
- Top 3 transactions by amount
- Transactions in a hard-coded date range
- Lookup of transaction `T002`

To use a different CSV, replace `src/main/resources/transactions.csv` and recompile (`mvn compile`).

---

## Problem 2 — Employee/Department CRUD (JDBC)

**Entry point:** `com.assignment.problem2.EmployeeApp`

**Database:** H2 in-memory (no external DB install needed).

| Setting | Value |
|---------|-------|
| JDBC URL | `jdbc:h2:mem:empdb;DB_CLOSE_DELAY=-1` |
| Driver | `org.h2.Driver` (included via the `com.h2database:h2:2.2.224` Maven dependency) |
| User | `sa` |
| Password | *(empty)* |

**Schema:** `src/main/resources/schema.sql` is loaded automatically at startup by `DatabaseConnection.createH2DataSource()`. It creates the `departments` and `employees` tables and inserts seed data. No manual setup is required.

**Using Maven:**
```bash
mvn exec:java -Dexec.mainClass="com.assignment.problem2.EmployeeApp"
```

**Using direct java command:**
```bash
cd java-assignment
H2_JAR=$(find ~/.m2/repository/com/h2database -name '*.jar' | head -1)
javac -cp "$H2_JAR" -d target/classes src/main/java/com/assignment/problem2/domain/*.java \
  src/main/java/com/assignment/problem2/exception/*.java \
  src/main/java/com/assignment/problem2/dao/*.java \
  src/main/java/com/assignment/problem2/dao/impl/*.java \
  src/main/java/com/assignment/problem2/*.java
java -cp "target/classes:$H2_JAR" com.assignment.problem2.EmployeeApp
```

To use MySQL or PostgreSQL instead of H2:
1. Update the JDBC URL, user, and password in `DatabaseConnection.java`.
2. Add the corresponding driver dependency to `pom.xml`.
3. Run `schema.sql` manually on your database (change `AUTO_INCREMENT` to `SERIAL` for PostgreSQL).

---

## Problem 3 — Report Generator

**Entry point:** `com.assignment.problem3.ReportApp`

**Config file:** `src/main/resources/report_config.properties` (loaded automatically from the classpath).

Default config contents:
```properties
report.type=EMPLOYEES_BY_DEPARTMENT
department.id=1
output.file=report_employees.csv
```

**Output path:** The report is written to the file specified by `output.file` in the config — by default `report_employees.csv` in the project working directory.

**Using Maven:**
```bash
mvn exec:java -Dexec.mainClass="com.assignment.problem3.ReportApp"
```

**Using direct java command:**
```bash
cd java-assignment
H2_JAR=$(find ~/.m2/repository/com/h2database -name '*.jar' | head -1)
javac -cp "$H2_JAR" -d target/classes \
  src/main/java/com/assignment/problem2/domain/*.java \
  src/main/java/com/assignment/problem2/exception/*.java \
  src/main/java/com/assignment/problem2/dao/*.java \
  src/main/java/com/assignment/problem2/dao/impl/*.java \
  src/main/java/com/assignment/problem2/*.java \
  src/main/java/com/assignment/problem3/*.java
java -cp "target/classes:$H2_JAR" com.assignment.problem3.ReportApp
```

Problem 3 reuses Problem 2's H2 database and DAO layer, so the same in-memory DB (with `schema.sql` seed data) is created on each run.

To change the report, edit `src/main/resources/report_config.properties` and recompile. Supported `report.type` values: `EMPLOYEES_BY_DEPARTMENT`, `ALL_EMPLOYEES`.

---

## Project Structure

```
java-assignment/
├── pom.xml
├── src/main/
│   ├── java/com/assignment/
│   │   ├── problem1/
│   │   │   ├── Transaction.java
│   │   │   ├── TransactionLoader.java
│   │   │   ├── TransactionService.java
│   │   │   └── TransactionAnalyzerApp.java
│   │   ├── problem2/
│   │   │   ├── domain/
│   │   │   │   ├── Employee.java
│   │   │   │   └── Department.java
│   │   │   ├── dao/
│   │   │   │   ├── EmployeeDao.java        (interface)
│   │   │   │   ├── DepartmentDao.java      (interface)
│   │   │   │   └── impl/
│   │   │   │       ├── EmployeeDaoImpl.java
│   │   │   │       └── DepartmentDaoImpl.java
│   │   │   ├── exception/
│   │   │   │   └── EmployeeNotFoundException.java
│   │   │   ├── DatabaseConnection.java
│   │   │   └── EmployeeApp.java
│   │   └── problem3/
│   │       ├── ReportConfig.java
│   │       ├── ReportGenerator.java
│   │       └── ReportApp.java
│   └── resources/
│       ├── transactions.csv
│       ├── schema.sql
│       └── report_config.properties
```
