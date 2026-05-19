# Nedaj

## Gas Station Management CLI

A small Java console application for managing fuel station inventory and availability using a command-line interface.

> Note: The current source files in `src/` are mostly placeholder comments and need implementation.

## Project structure

- `src/models/`
  - `FuelInventory.java` — fuel stock details and availability state
  - `FuelType.java` — fuel type enumeration
  - `GasStation.java` — station details and inventory mapping
- `src/services/`
  - `GasStationService.java` — inventory data and business logic
- `src/presentation/`
  - `ConsoleMenu.java` — console user interaction and menus
  - `Main.java` — application entry point

## Build and run

This project uses standard Java source files without explicit package declarations.

1. Open a terminal in the project root.
2. Compile all Java files:

   ```powershell
   javac src/models/*.java src/services/*.java src/presentation/*.java -d bin
   ```

3. Run the application:

   ```powershell
   java -cp bin presentation.Main
   ```

## Purpose

The project is intended to provide a CLI-based gas station inventory system where drivers can search available fuel and administrators can update station stock information.

## Notes

- The current repository files are a skeleton with comments only.
- Implement the CLI logic in `Main.java` and `ConsoleMenu.java`.
- Add real station data and inventory handling in `GasStationService.java`.
