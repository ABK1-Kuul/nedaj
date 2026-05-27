
# ⛽ Nedaj - Gas Station Management CLI

> *"Never run on empty again"*

A production-ready Java console application for real-time fuel station inventory management. Built with clean architecture principles so you can swap the CLI for a GUI, web dashboard, or mobile app without touching a single line of business logic.

## 🎯 The Problem We're Solving

Ever pulled into a gas station only to find they're out of benzene? Drove 15 minutes for diesel that's "coming tomorrow"? **Nedaj** puts real-time fuel availability in drivers' hands and gives station managers a simple inventory control system.

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

## Search strategies (OOP: interface + inheritance)

Driver search uses the **Strategy pattern** under `src/services/search/`:

| Class | Role |
|-------|------|
| `SearchStrategy` | Interface — `search(stations, criteria)` |
| `AbstractSearchStrategy` | Abstract base — shared zone / line / fuel filters |
| `ZoneSearchStrategy` | `extends` base — list matches in zone |
| `NearestStationSearchStrategy` | `extends` base — sort by distance from driver \((x, y)\) |
| `CheapestFuelSearchStrategy` | `extends` base — sort by lowest price per liter |

`GasStationService.search(criteria, strategy)` accepts any `SearchStrategy`, so new strategies can be added without changing the service loop.

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
