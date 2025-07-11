## Prerequisites

Before you run the project, ensure you have:

- [NetBeans IDE (version 15+ recommended)](https://netbeans.apache.org/download/index.html)
- [Apache Derby (JavaDB)](https://db.apache.org/derby/) installed or set up in NetBeans
- Java 17 or higher
- Git (or GitHub Desktop)

---

## Cloning the Project

1. Clone using GitHub Desktop or terminal:

```bash
git clone https://github.com/<your-username>/BCWellnessDesktop.git
```
## JavaDB (Derby) Setup

### Step 1: Install/Configure JavaDB

If not already set up:
1. Download Apache Derby from the official site
2. Extract it to a location such as `C:\Derby`
3. In NetBeans, go to **Tools → Java DB**
4. Set:
   - **Java DB installation:** `C:\Derby`
   - **Database location:** `C:\DerbyDatabases`

### Step 2: Start JavaDB in NetBeans

- Open the **Services** tab in NetBeans
- Expand **Java DB**
- Right-click → **Start Server**

### Step 3: Create the Database

1. In the **Services** tab, right-click on **Java DB** → **Create Database**
2. Set:
   - **Database name:** `wellness_db`
   - **Username:** `app`
   - **Password:** `app`
3. Click **OK**

### Step 4: Execute SQL Setup Script

1. Under **Services → Databases**, find and connect to:
```
jdbc:derby://localhost:1527/wellness_db [app on APP]
```
3.  Right-click the DB → **Execute Command**
4. Paste the contents of `setup/setup_wellness_db.sql`
5. Click **Run**

This will create the required tables and insert sample data.


### Step 5: Start the Database Server

- In the **Services** tab → Expand **Java DB**
- Right-click → **Start Server**

## Conencting the App to the Database
```java
Check **BDConnection.java** and make sure it mathces your setup:
private static final String DB_URL = "jdbc:derby://localhost:1527/wellness_db";
private static final String USER = "app";
private static final String PASSWORD = "app";
```
Chnage USER and PASSWORD if you used different ones







