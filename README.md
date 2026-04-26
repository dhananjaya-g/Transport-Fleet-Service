# Aditri's Transport Fleet Service — README

A menu-driven Java console app that models a multi-modal transportation fleet (land/air/water), demonstrating OOP fundamentals (abstraction, inheritance, polymorphism, interfaces, encapsulation). Includes CSV save/load, reporting, and search utilities.

---

## 0) Prerequisites (all OS)
- **JDK 21** (or ensure both `javac` and `java` are the **same** major version).
- Verify your Java toolchain:
  ```bash
  java -version
  javac -version
  ```
  Both should report **21.x** (or at least the same version). If not, fix your PATH/JAVA_HOME per OS instructions below.

---

## 1) Build & Run

> Run all commands **from the project root** (the folder that contains `src/main/java`).

### Linux

#### Install JDK 21
- **Debian/Ubuntu**
  ```bash
  sudo apt update
  sudo apt install openjdk-21-jdk
  ```


#### Compile & Run
```bash
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
java -cp out app.Main
```

**Note about `sudo` on Linux:** you normally **don’t** need it for compiling/running in your home folder. But Use `sudo` in case above showing insufficient privileges 

---

### macOS

#### Install JDK 21
- **Homebrew**
  ```bash
  brew install openjdk@21
  sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk \
       /Library/Java/JavaVirtualMachines/openjdk-21.jdk
  ```

- Point your shell to JDK 21 (for this session):
  ```bash
  export JAVA_HOME=$(/usr/libexec/java_home -v 21)
  export PATH="$JAVA_HOME/bin:$PATH"
  ```

#### Compile & Run
```bash
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
java -cp out app.Main
```

---

### Windows

#### Install JDK 21
  
  Open a **new** terminal and verify:
  ```powershell
  java -version
  javac -version
  ```

#### Compile & Run (Git Bash)
```bash
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
java -cp out app.Main
```

---

## 2) Clean & Rebuild

**macOS/Linux/Git Bash**
```bash
rm -rf out && mkdir -p out
javac -d out $(find src/main/java -name "*.java")
java -cp out app.Main
```

---

## 3) Troubleshooting (quick)

- **UnsupportedClassVersionError (e.g., “class file version 65.0 vs 52.0”)**  
  Your `javac` compiled with Java **21**, but `java` is running Java **8**. Align versions:
  ```bash
  java -version
  javac -version
  ```
  Fix PATH/JAVA_HOME so both are 21, then recompile.

- **Could not find or load main class `app.Main`**  
  Run from the project root; ensure `out/app/Main.class` exists; use:
  ```bash
  java -cp out app.Main
  ```

- **Permission denied (Linux/macOS)**  
  You might have created `out/` with `sudo`. Reset ownership and rebuild:
  ```bash
  sudo chown -R "$USER":"$USER" .
  rm -rf out && mkdir -p out
  ```
---

## 4) Project Layout (for reference)
```
transport-fleet-full/
├─ README.md
├─ sample-data/
│  └─ fleet-sample.csv
└─ src/
   └─ main/java/
      ├─ app/Main.java
      ├─ fleet/FleetManager.java
      ├─ io/{CsvSerializer.java, VehicleFactory.java}
      ├─ services/{FuelConsumable.java, CargoCarrier.java, PassengerCarrier.java, Maintainable.java}
      ├─ exceptions/{InvalidOperationException.java, InsufficientFuelException.java, OverloadException.java}
      └─ vehicles/
         ├─ Vehicle.java
         ├─ land/{LandVehicle.java, vehicle/
            ├─ {Car.java, Truck.java, Bus.java}}
         ├─ air/{AirVehicle.java, vehicle/
            ├─{Airplane.java}}
         └─ water/{WaterVehicle.java, vehicle/
            ├─{CargoShip.java}}
```

---

## 5) CSV Formats (one line per vehicle)

- **Car**  
  `Car,id,model,maxSpeed,numWheels,passengerCapacity,currentPassengers,fuelLevel,currentMileage,maintenanceNeeded`

- **Truck**  
  `Truck,id,model,maxSpeed,numWheels,cargoCapacity,currentCargo,fuelLevel,currentMileage,maintenanceNeeded`

- **Bus**  
  `Bus,id,model,maxSpeed,numWheels,passengerCapacity,currentPassengers,cargoCapacity,currentCargo,fuelLevel,currentMileage,maintenanceNeeded`

- **Airplane**  
  `Airplane,id,model,maxSpeed,maxAltitude,passengerCapacity,currentPassengers,cargoCapacity,currentCargo,fuelLevel,currentMileage,maintenanceNeeded`

- **CargoShip**  
  `CargoShip,id,model,maxSpeed,hasSail,cargoCapacity,currentCargo,fuelLevel,currentMileage,maintenanceNeeded`

A sample file is provided at `sample-data/fleet-sample.csv`.

---

## 6) Collections Usage & Justification

- **`ArrayList<Vehicle>` (in `FleetManager`)**
    - Dynamic storage for the fleet (easy append, iteration, removal via `removeIf`/iterator).
    - Good iteration performance for listing, sorting via streams, and batch operations.

- **`HashSet<String>` / Sets of models (in `FleetManager`)**
    - A `HashSet` (or a de-dup strategy) is used to demonstrate **duplicate handling** of IDs/models.
    - `TreeSet<String>` (`modelsAlphabetical`) keeps **distinct model names** in **sorted** order (case-insensitive) for quick display and reporting. `TreeSet` provides natural ordering with O(log n) insertion.

- **Comparators / Streams**
    - `sortByModel()`, `sortByMaxSpeed()`, `sortByEfficiency()` return **sorted views** using Comparators with streams, keeping the underlying `ArrayList` order untouched (functional style, easy to print).



---

## 7) Why these collections in `addVehicle(...)` and how they help

### 1) `List<Vehicle> fleet = new ArrayList<>()`
- **What it stores:** Every `Vehicle` object which are currently in the system.
- **Why an `ArrayList`:**
    - Dynamic resizing, cache-friendly iteration, easy to print/list.
    - Simple to scan when checking for a duplicate ID during add.
- **How it helps in `addVehicle`:**
    - We scan `fleet` once to ensure **ID uniqueness** (case-insensitive in our code).
    - On success, we append the new `Vehicle` to `fleet`.
- **Typical operations & costs:**
    - Duplicate check: `O(n)` (single pass).
    - Append: amortized `O(1)`.
    - Listing/reporting: `O(n)` iteration.

### 2) `Set<String> distinctModels = new HashSet<>()`
- **What it stores:** A **unique** set of all model names present in the fleet (e.g., `Swift`, `A320`, `Volvo`).
- **Why a `HashSet`:**
    - Fast membership and deduplication: average `O(1)` add/contains.
    - Lets us quickly know “have we seen this model name before?” and report the **count of distinct models**.
- **How it helps in `addVehicle`:**
    - After the vehicle passes the ID check, we do `distinctModels.add(v.getModel())`.
    - This keeps a constantly up-to-date **unique** pool of models for summary metrics.

### 3) `TreeSet<String> modelsAlphabetical = new TreeSet<>(String.CASE_INSENSITIVE_ORDER)`
- **What it stores:** All model names again, but **sorted** (case-insensitive).
- **Why a `TreeSet`:**
    - Natural, always-sorted order with `O(log n)` insertion.
    - Perfect for an alphabetical display in reports without re-sorting every time.
- **How it helps in `addVehicle`:**
    - We also do `modelsAlphabetical.add(v.getModel())`, so the moment a vehicle is added,  
      the alphabetical list of models is fresh and ready to print in summaries.
- **Why keep both `HashSet` and `TreeSet`:**
    - `HashSet` gives **fast dedup & counts**; `TreeSet` gives **sorted presentation**.
    - Using both avoids re-sorting on every report and keeps both **speed** and **readability**.

### How it flows during `addVehicle(...)`
1. **Null/ID check:** reject null vehicles; scan `fleet` to reject a **duplicate ID** (case-insensitive).
2. **Model sets update:**
    - `distinctModels.add(model)` → ensures the **unique** model pool is up to date.
    - `modelsAlphabetical.add(model)` → ensures the **sorted** model view is up to date.
3. **Append to fleet:** `fleet.add(v)` → the canonical source of all vehicles.

### Removal & consistency
- On removal, we delete from `fleet` and **rebuild** the two model sets (`rebuildModelSets()`):
    - Clear both sets.
    - Re-add each remaining vehicle’s model.
- This guarantees both sets always reflect the current truth of `fleet`, even if multiple vehicles
  of the same model were removed.

### Summary of responsibilities
- `fleet (ArrayList)` → **stores the vehicles** themselves (iteration, save/load, printing).
- `distinctModels (HashSet)` → **tracks uniqueness** of models for counts/metrics.
- `modelsAlphabetical (TreeSet)` → **keeps models sorted** for instant, readable reports.


## 8) OOP Highlights

### Encapsulation 
- **Idea:** Data hiding (Keep fields private) and expose safe methods
- **Instance:** In `Vehicle` and concrete classes (`Car`, `Truck`, …):
  ```java
  private final String id;
  private final String model;
  private final double maxSpeed;
  private double currentMileage;

  protected void addMileage(double distance) { this.currentMileage += distance; }
  ```
---



## 9) Quick CLI Walkthrough

When you run `java -cp out app.Main`, you’ll see a menu like:
```
=== Welcome to Aditri's Transport fleet Service 2 ===
1. Add Vehicle
2. Remove Vehicle
3. Generate Report
4. Save Fleet (CSV)
5. Load Fleet (CSV)
6. List All Vehicles
7. Sort by (Efficiency/Model/MaxSpeed)
8. Exit
Choose option:
```


---

## 8) Project Guide

This tutorial shows how to use the **console (CLI)** for the Transport Fleet project and walks you through a **hands-on demo** with expected outputs.

> **Prerequisite:** You’ve already compiled the project and can run the app. From the project root:
> ```bash
> java -cp out app.Main
> ```

---

### 1) Start the app

When the app launches, you’ll see a menu like:

```
=== Welcome to Aditri's Transport fleet Service 2 ===
1. Add Vehicle
2. Remove Vehicle
3. Generate Report
4. Save Fleet (CSV)
5. Load Fleet (CSV)
6. List All Vehicles
7. Sort by (Efficiency/Model/MaxSpeed)
8. Exit
Choose option:
===================================================

```
---
### 2) Core CLI actions

### 2.1 Add Vehicle (Option 1)
Create a vehicle by type. You’ll be prompted for common fields (`id`, `model`, `maxSpeed`) and type-specific fields.
```
Choose option: 1
Type [Car/Truck/Bus/Airplane/CargoShip]: Car
id: C100
model: Swift
maxSpeed km/h: 160
numWheels: 4
passengerCapacity: 5
Added: Car[id=C100, model=Swift, ...]
```
### 2.2 Remove Vehicle (Option 2)
Remove by `id`:
```
Choose option: 2
id to remove: C100
Removed: C100
```

### 2.3 Generate Report (Option 3)
```
Choose option: 3
=== Fleet Summary ===
Total vehicles: 5
Distinct models: 5 -> [A320, Evergreen, Swift, Tata407, Volvo]
Fastest: A320 (850.0 km/h)
Slowest: Evergreen (60.0 km/h)
Average efficiency: 8.40 km/l
=====================
```
### 2.4 Save Fleet (Option 4)
```
Choose option: 4
CSV path: /tmp/fleet.csv
Saved to /tmp/fleet.csv

```

### 2.5 Load Fleet (Option 5)
```
Choose option: 5
CSV path: /tmp/fleet.csv
Loaded 5 vehicles.
```

### 2. 6 List All Vehicles (Option 6)
Print a readable list of all vehicles with key fields:
```
Choose: 6
Car[C1] model=Swift maxSpeed=150.0 km/h mileage=0.0 km eff=15.00 km/L 
Truck[T1] model=Tata407 maxSpeed=110.0 km/h mileage=0.0 km eff=8.00 km/L 
Bus[B1] model=Volvo maxSpeed=90.0 km/h mileage=0.0 km eff=10.00 km/L 
Airplane[A1] model=A320 maxSpeed=850.0 km/h mileage=0.0 km eff=5.00 km/L
CargoShip[S1] model=Evergreen maxSpeed=60.0 km/h mileage=0.0 km eff=4.00 km/L
```

### 2.7 Sort view (Option 7)
Sort the in-memory list by `calculateFuelEfficiency()` (descending):
```
Choose option: 7
Sort by (model/speed/eff) : model
Airplane[A1] model=A320 maxSpeed=850.0 km/h mileage=0.0 km eff=5.00 km/L
CargoShip[S1] model=Evergreen maxSpeed=60.0 km/h mileage=0.0 km eff=4.00 km/L
Car[C1] model=Swift maxSpeed=150.0 km/h mileage=0.0 km eff=15.00 km/L
Truck[T1] model=Tata407 maxSpeed=110.0 km/h mileage=0.0 km eff=8.00 km/L
Bus[B1] model=Volvo maxSpeed=90.0 km/h mileage=0.0 km eff=10.00 km/L


Choose option: 7
Sort by (model/speed/eff) : speed
Airplane[A1] model=A320 maxSpeed=850.0 km/h mileage=0.0 km eff=5.00 km/L
Car[C1] model=Swift maxSpeed=150.0 km/h mileage=0.0 km eff=15.00 km/L
Truck[T1] model=Tata407 maxSpeed=110.0 km/h mileage=0.0 km eff=8.00 km/L
Bus[B1] model=Volvo maxSpeed=90.0 km/h mileage=0.0 km eff=10.00 km/L
CargoShip[S1] model=Evergreen maxSpeed=60.0 km/h mileage=0.0 km eff=4.00 km/L


Choose option: 7
Sort by (model/speed/eff) : eff
Car[C1] model=Swift maxSpeed=150.0 km/h mileage=0.0 km eff=15.00 km/L
Bus[B1] model=Volvo maxSpeed=90.0 km/h mileage=0.0 km eff=10.00 km/L
Truck[T1] model=Tata407 maxSpeed=110.0 km/h mileage=0.0 km eff=8.00 km/L
Airplane[A1] model=A320 maxSpeed=850.0 km/h mileage=0.0 km eff=5.00 km/L
CargoShip[S1] model=Evergreen maxSpeed=60.0 km/h mileage=0.0 km eff=4.00 km/L
```

### 2.8 Exit
Quit the program.

---
