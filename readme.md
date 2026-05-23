
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
git clone https://github.com/yourusername/nedaj.git
cd nedaj
javac src/models/*.java src/services/*.java src/presentation/*.java -d bin

# Run it
java -cp bin presentation.Main
```

## 🎮 Features That Matter

### 👨‍🔧 Driver Experience
```
→ Select zone: Bole
→ Fuel type: BENZENE
→ Results:
   📍 Bole Total Station | 95 ETB/L | 1,200 L available
   📍 Bole Shell       | 97 ETB/L | 850 L available
```
**Smart filtering** — Only stations with actual stock appear. No more wasted trips.

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
Driver chooses "Bole" + "BENZENE"
         ↓
ConsoleMenu calls service.searchFuel("Bole", BENZENE)
         ↓
GasStationService loops through stations
         ↓
Station.hasFuel(BENZENE) checks:
   • Fuel exists in inventory?
   • Available flag = true?
   • Quantity > 0?
         ↓
Return filtered list → ConsoleMenu displays
```

## Search logic: zones vs proximity

The driver search flow today filters by **text zone** (e.g. `Bole`, `Megenagna`) and fuel type, then lists matching stations. Each station also has grid coordinates \((x, y)\) in `data/stations.json` for proximity sorting (not wired in the CLI yet).

### Euclidean distance

If a driver is at \((x_1, y_1)\) and a gas station is at \((x_2, y_2)\), the straight-line distance is:

\[
d = \sqrt{(x_2 - x_1)^2 + (y_2 - y_1)^2}
\]

After filtering to stations that have the requested fuel in stock, the service (or presentation layer) can **sort results by \(d\) in ascending order** so the nearest stations appear first.

### Intended behavior (not implemented yet)

1. Driver enters their position \((x_1, y_1)\) (and fuel type as today).
2. Collect all stations with available stock for that fuel.
3. For each station with coordinates \((x_2, y_2)\), compute \(d\) using the formula above.
4. Return or display the list sorted by \(d\) from smallest to largest.

Zone-based search can remain as a simpler mode; grid coordinates are an alternative when you want **proximity ordering** rather than only a named area match.

## 🔧 Extension Points

- **Proximity search** — Prompt for driver \((x, y)\) in the CLI and sort results by Euclidean distance (coordinates are already in JSON; see [Search logic: zones vs proximity](#search-logic-zones-vs-proximity))
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
