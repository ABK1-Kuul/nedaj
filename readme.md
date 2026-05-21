# Nedaj

## Gas Station Management CLI

A Java console application for managing fuel station inventory and availability. The codebase follows a three-layer architecture (Model → Service → Presentation) so you can later swap the CLI for a GUI or web UI without rewriting business logic.

## Project structure

| Layer | Package | Classes |
|-------|---------|---------|
| **Model** | `models` | `FuelType`, `FuelInventory`, `GasStation` |
| **Service** | `services` | `GasStationService` |
| **Presentation** | `presentation` | `ConsoleMenu`, `Main` |

```
src/
├── models/
│   ├── FuelType.java
│   ├── FuelInventory.java
│   └── GasStation.java
├── services/
│   └── GasStationService.java
└── presentation/
    ├── ConsoleMenu.java
    └── Main.java
```

## Build and run

From the project root:

```powershell
javac src/models/*.java src/services/*.java src/presentation/*.java -d bin
java -cp bin presentation.Main
```

## Features

- **Driver menu** — Search stations by zone and fuel type; only stations with available stock are shown.
- **Admin menu** — Look up a station by ID, view inventory, update quantity and availability.
- **Mock data** — Four sample stations in Bole and Megenagna (see `GasStationService.seedMockData()`).

### Sample driver flow

1. Main menu → `1` (Driver)
2. Zone: `Bole`
3. Fuel: `1` (BENZENE)
4. View matching stations with price and stock

### Sample admin flow

1. Main menu → `2` (Admin)
2. Station ID: `ST001`
3. Choose fuel type and enter new quantity / availability

## 7-day team plan (2 people)

Use this split so you can work in parallel with minimal merge conflicts.

| Day | Person A | Person B | Deliverable |
|-----|----------|----------|-------------|
| **1** | Review architecture; run app; document class diagram | Same + agree on Git workflow (branch per feature) | Both can compile and run |
| **2** | Extend `GasStation` / `FuelInventory` (validation, `toString`) | Add more mock stations in `seedMockData()` | Richer test data |
| **3** | Service: edge cases (`searchFuel` empty zone, null checks) | CLI: input validation polish, clearer error messages | Stable search flow |
| **4** | Admin: optional price update in service + menu | Driver: show all fuels at a station (optional feature) | Feature complete for assignment |
| **5** | Unit-style manual test script (scenarios in README) | UML class diagram + flowchart (export from draw.io) | Documentation |
| **6** | Code review each other's PRs; refactor duplicates | Presentation demo script (2 min walkthrough) | Clean codebase |
| **7** | Final integration test; fix bugs | Submit report + push to remote | **Done** |

### Suggested ownership

- **Person A:** Model layer + `GasStationService` business rules
- **Person B:** `ConsoleMenu` + README + diagrams

Sync daily (15 min): what you merged, what's blocked.

## Architecture notes

- **Separation of concerns:** `ConsoleMenu` only reads input and prints output; `GasStationService` owns all station data and filtering.
- **`FuelType` enum** prevents invalid fuel strings.
- **`hasFuel()`** on `GasStation` centralizes “is this fuel sellable?” (available flag + quantity > 0).

## Next steps (optional enhancements)

- Persist stations to a file or database
- Admin login / station ID password
- Add zones via admin instead of hard-coded mock data
