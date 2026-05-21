# Nedaj

## Gas Station Management CLI

Java console application for managing fuel station inventory and availability across zones.

## Project structure

| Layer | Package | Classes |
|-------|---------|---------|
| Model | `models` | `FuelType`, `FuelInventory`, `GasStation` |
| Service | `services` | `GasStationService` |
| Presentation | `presentation` | `ConsoleMenu`, `Main` |

## Build and run

```powershell
javac src/models/*.java src/services/*.java src/presentation/*.java -d bin
java -cp bin presentation.Main
```

## Usage

**Driver** — Search by zone and fuel type for stations with available stock.

**Admin** — Enter a station ID, view inventory, update quantity and availability.

Sample station IDs: `ST001`, `ST002`, `ST003`, `ST004`. Zones include `Bole` and `Megenagna`.
