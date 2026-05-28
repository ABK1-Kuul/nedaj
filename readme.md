
# ⛽ Nedaj - Gas Station Management CLI

> *"Never run on empty again"*

A production-ready Java console application for real-time fuel station inventory management. Built with clean architecture principles so you can swap the CLI for a GUI, web dashboard, or mobile app without touching a single line of business logic.

## 🎯 The Problem We're Solving

Ever pulled into a gas station only to find they're out of benzene? Drove 15 minutes for diesel that's "coming tomorrow"? **Nedaj** puts real-time fuel availability in drivers' hands and gives station managers a simple inventory control system.

## 📁 Project Structure

```
nedaj/
├── src/
│   ├── models/
│   │   ├── FuelType.java          (Enum: BENZENE, REGULAR, KEROSENE)
│   │   ├── FuelInventory.java     (price, quantityLiters, isAvailable)
│   │   └── GasStation.java        (id, name, zone, x, y, hasLine, inventory Map)
│   ├── services/
│   │   ├── GasStationService.java (Main service + Strategy Pattern context)
│   │   ├── StationJsonStore.java  (JSON persistence with regex)
│   │   └── search/
│   │       ├── SearchStrategy.java (Interface)
│   │       ├── AbstractSearchStrategy.java (Common filters + distance)
│   │       ├── ZoneSearchStrategy.java (No sorting)
│   │       ├── CheapestFuelSearchStrategy.java (Sort by price ↑)
│   │       ├── NearestStationSearchStrategy.java (Sort by distance ↑)
│   │       └── SearchCriteria.java (DTO with constructor overloading)
│   └── presentation/
│       ├── ConsoleMenu.java (User interface, strategy selector)
│       └── Main.java (Entry point)
├── data/
│   └── stations.json (Persisted station data)
└── README.md
```
## 🏗️ Architecture That Scales

```
┌─────────────────────────────────────────────┐
│           PRESENTATION LAYER                │
│   (ConsoleMenu.java - today)                │
│   (Swing/JavaFX - tomorrow)                 │
│   (REST API - next month)                   │
├─────────────────────────────────────────────┤
│           SERVICE LAYER                     │
│   (GasStationService.java)                  │
│   (search/* — Strategy pattern)             │
│   • Business rules & filtering              │
│   • Stock validation                        │
│   • No UI code allowed here →               │
├─────────────────────────────────────────────┤
│           MODEL LAYER                       │
│   (FuelType, FuelInventory, GasStation)     │
│   • Data structures                         │
│   • Validation logic                        │
│   • Pure POJOs                              │
└─────────────────────────────────────────────┘
```

### Why Three Layers?
- **Swap CLI for GUI?** Just rewrite `ConsoleMenu` — service stays untouched
- **Add database?** Only touch service layer
- **Write unit tests?** Test service in isolation without keyboard input headaches

## 🚀 Quick Start

```bash
# Clone & compile
git clone https://github.com/ABK1-Kuul/nedaj.git
cd nedaj
javac src/models/*.java src/services/*.java src/services/search/*.java src/presentation/*.java -d bin

# Run it
java -cp bin presentation.Main
```

## 🎮 Features That Matter

### 👨‍🔧 Driver Experience
```
→ Select zone: Bole
→ Fuel type: BENZENE
→ Search mode: Nearest / Cheapest / Zone list
→ Results (filtered + ordered by your choice)
```
**Smart filtering** — Only stations in the zone with a queue line and in-stock fuel appear. **Search strategies** let drivers sort by zone list, nearest station (Euclidean distance), or lowest price.

### 👨‍💼 Admin Controls
```
→ Station ID: ST001
→ Update BENZENE:
   📊 Current: 850 L (AVAILABLE)
   ✏️ New quantity: 1200
   🔘 Availability: [YES/NO]
   ✅ Updated!
```

### 📦 Station data (`data/stations.json`)

Gas stations are stored in **`data/stations.json`**. The app loads this file on startup; admin inventory updates are written back automatically.

Each station record includes:

| Field | Description |
|-------|-------------|
| `id` | Station code (e.g. `ST001`) |
| `name` | Station display name |
| `zone` | Text area for zone search (`Bole`, `Megenagna`, …) |
| `x`, `y` | Grid coordinates for future proximity sorting |
| `hasLine` | `"yes"` or `"no"` — whether the station has a line to get gas |
| `fuels[]` | List of fuel lines at that station |

Each fuel line includes:

| Field | Description |
|-------|-------------|
| `type` | `BENZENE`, `REGULAR`, or `KEROSENE` |
| `quantityLiters` | Stock amount in liters |
| `pricePerLiter` | Price per liter (ETB) |
| `available` | `"yes"` or `"no"` — whether that fuel type is offered at the station |

`hasLine` is per **station** (can customers queue for fuel?). `available` is per **fuel type** (is that product sold right now?).

Example station fields:

```json
{
  "id": "ST001",
  "name": "TotalEnergies Bole",
  "zone": "Bole",
  "x": 12.0,
  "y": 8.5,
  "hasLine": "yes",
  "fuels": [ ... ]
}
```

Example fuel entry:

```json
{
  "type": "BENZENE",
  "quantityLiters": 500,
  "pricePerLiter": 90.5,
  "available": "yes"
}
```

Run the app from the **project root** so `data/stations.json` resolves correctly.

## 👥 7-Day Team Plan (2 Developers)

### Daily Sync Schedule (15 min standup)
- **9:00 AM** - What merged yesterday?
- **9:10 AM** - What's blocked?
- **9:15 AM** - Review today's targets

### Day-by-Day Breakdown

| Day | Person A (Model/Service) | Person B (CLI/UX) | Done? |
|-----|--------------------------|-------------------|-------|
| **1** | Review architecture, document class relationships | Set up Git (feature branches), run app locally | ✅ |
| **2** | Add validation to `FuelInventory` (no negative stock) | Create 6 more mock stations (add CMC, Summit zones) | |
| **3** | Edge cases: null zone handling, fuel not found | Input validation loops, colored console output | |
| **4** | Service method: update fuel price (admin feature) | Driver "view all fuels at station" feature | |
| **5** | Write 10 test scenarios in README | UML class diagram + flowchart (Lucidchart/draw.io) | |
| **6** | Code review Person B's PR, refactor duplicates | Code review Person A's PR, performance check | |
| **7** | Integration testing, bug fixes | Demo script + screen recording, final push | |

### 🎯 Ownership Strategy

**Person A** (Backend focus)
- `GasStation.java` - Station data structure
- `FuelInventory.java` - Stock business rules
- `GasStationService.java` - All filtering logic
- Unit test creation

**Person B** (Frontend focus)  
- `ConsoleMenu.java` - Menu flows & UX
- `Main.java` - App entry point
- `README.md` - Documentation & diagrams
- Input sanitization

**Merge strategy:** Each creates feature branches (`feature/validation`, `feature/ui-polish`), PR to `develop`, then `main` on Day 7.

## 📊 Sample Data Flow

```
Driver chooses zone + fuel + search strategy
         ↓
ConsoleMenu builds SearchCriteria + SearchStrategy
         ↓
GasStationService.search(criteria, strategy)
         ↓
AbstractSearchStrategy.applyCommonFilters (zone, hasLine, hasFuel)
         ↓
Concrete strategy sorts (nearest / cheapest) or returns as-is (zone)
         ↓
Return list → ConsoleMenu displays
```

## OOP Concepts Demonstrated

This project demonstrates core OOP concepts using the current package structure:

| Concept | Where it appears in this project |
|-------|------|
| **Encapsulation** | `GasStation` and `FuelInventory` keep fields private and expose behavior through methods like `updateFuelStatus`, `hasFuel`, `setQuantityLiters`, and `setAvailable`. |
| **Abstraction** | `SearchStrategy` defines a clean search contract (`search(stations, criteria)`) without exposing internal filtering/sorting details to callers. |
| **Inheritance** | `AbstractSearchStrategy` provides shared filtering logic (`applyCommonFilters`) and reusable utilities (`distanceFromDriver`) for concrete strategies. |
| **Polymorphism** | `GasStationService.search(criteria, strategy)` works with any `SearchStrategy` implementation (`ZoneSearchStrategy`, `NearestStationSearchStrategy`, `CheapestFuelSearchStrategy`). |
| **Composition** | `GasStation` contains a `Map<FuelType, FuelInventory>`; `GasStationService` manages a `List<GasStation>` and uses `StationJsonStore` for persistence. |
| **Strategy Pattern** | Search behavior is selected at runtime in `ConsoleMenu` and executed through interchangeable strategy classes under `src/services/search/`. |

Current search strategy classes in `src/services/search/`:

| Class | Role |
|-------|------|
| `SearchStrategy` | Interface for driver search behavior |
| `AbstractSearchStrategy` | Shared zone/line/fuel filtering base class |
| `ZoneSearchStrategy` | Returns filtered results without additional ordering |
| `NearestStationSearchStrategy` | Sorts filtered results by Euclidean distance |
| `CheapestFuelSearchStrategy` | Sorts filtered results by fuel price |

### Euclidean distance (nearest mode)

If a driver is at \((x_1, y_1)\) and a station is at \((x_2, y_2)\):

\[
d = \sqrt{(x_2 - x_1)^2 + (y_2 - y_1)^2}
\]

`NearestStationSearchStrategy` applies the common filters, then sorts by \(d\) ascending.



## 📚 OOP Concepts Demonstrated

### Four Pillars of OOP

| Concept | Location | Line(s) | Description |
|---------|----------|---------|-------------|
| **Encapsulation** | `FuelInventory.java` | 4-6, 14-19 | Private fields with public getters/setters |
| **Inheritance** | `ZoneSearchStrategy.java` | 7 | Extends `AbstractSearchStrategy` |
| **Polymorphism** | `CheapestFuelSearchStrategy.java` | 11-18 | `@Override` of `search()` method |
| **Abstraction** | `SearchStrategy.java` | 9-11 | Interface defining contract |

### RELATIONSHIP
```
GasStation ◄────────── contains ◄────────── FuelInventory

GasStationService ◄────── manages ◄────── GasStation

GasStationService ◄────── uses ◄────── SearchStrategy

AbstractSearchStrategy ◄────── implements ◄────── SearchStrategy

ZoneSearchStrategy ◄────── extends ◄────── AbstractSearchStrategy

CheapestFuelSearchStrategy ◄────── extends ◄────── AbstractSearchStrategy

NearestStationSearchStrategy ◄────── extends ◄────── AbstractSearchStrategy

ConsoleMenu ◄────── uses ◄────── GasStationService

StationJsonStore ◄────── used by ◄────── GasStationService

Main ◄────── creates ◄────── ConsoleMenu

Main ◄────── creates ◄────── GasStationService
```

### Strategy Pattern (Bonus)

- **Context:** `GasStationService.search()` - Line 23-25
- **Strategy Interface:** `SearchStrategy` - Line 9-11  
- **Concrete Strategies:** `ZoneSearchStrategy`, `CheapestFuelSearchStrategy`, `NearestStationSearchStrategy`
- **Client:** `ConsoleMenu` - Lines 35-57 (runtime strategy selection)

### Constructor Features

- **Overloading:** `SearchCriteria.java` - Lines 13-27 (3 constructors)
- **Chaining:** `SearchCriteria.java` - Lines 14, 18, 21 (`this()` calls)
- **Default:** `GasStationService.java` - Line 16 (chains to parameterized)

### Access Modifiers

| Modifier | Usage | Location |
|----------|-------|----------|
| `private` | Fields and utility constructor | `FuelInventory.java` line 4-6, `StationJsonStore.java` line 25 |
| `protected` | Shared strategy methods | `AbstractSearchStrategy.java` line 13, 25 |
| `public` | API methods | All service and model public methods |
| `final` | Utility class | `StationJsonStore.java` line 19 |

## 🔧 Extension Points

- **New search strategy** — e.g. highest stock: `extends AbstractSearchStrategy`, implement `search()`, plug into driver menu
- **Admin authentication** — Simple password per station ID
- **Add new zones** — Admin menu option (no hardcoding)
- **Low stock alerts** — Notify when benzene < 500L
- **Sales tracking** — Log every sale for reporting

## 🐛 Known Limitations

- Station list is edited manually in JSON (no admin UI to add new stations yet)
- No multi-threading for concurrent admin edits
- Zone list is hardcoded (Bole, Megenagna)

## 🏆 Success Criteria

By Day 7, your app should:
1. Compile with `javac` without warnings
2. Handle edge cases (null inputs, invalid IDs)
3. Pass all 10 manual test scenarios
4. Let drivers find fuel in under 10 seconds
5. Let admins update stock in under 15 seconds

## 🤝 Contributing

This is a pair programming project. Rules:
- No silent coding — screen share if remote
- Every commit message follows: `type: description` (`feat:`, `fix:`, `docs:`, `test:`)
- No merging without the other person's approval

---

*Java Project - Spring 2026*

*"The best time to check fuel availability is before you leave, not after you arrive."*
