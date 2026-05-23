package presentation;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import models.FuelInventory;
import models.FuelType;
import models.GasStation;
import services.GasStationService;

public class ConsoleMenu {
    private final GasStationService service;
    private final Scanner scanner;

    public ConsoleMenu(GasStationService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readIntInRange("Enter choice: ", 1, 3);

            switch (choice) {
                case 1 -> driverMenu();
                case 2 -> adminMenu();
                case 3 -> {
                    System.out.println("\nThank you for using Nedaj. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n========== NEDAJ GAS STATION ==========");
        System.out.println("1. Driver - Search available fuel");
        System.out.println("2. Admin  - Update station inventory");
        System.out.println("3. Exit");
        System.out.println("=======================================");
    }

    private void driverMenu() {
        System.out.println("\n--- Driver: Find Fuel ---");

        System.out.print("Enter zone (e.g. Bole, Megenagna): ");
        String zone = scanner.nextLine().trim();

        FuelType fuelType = promptFuelType();
        if (fuelType == null) {
            return;
        }

        List<GasStation> results = service.searchFuel(zone, fuelType);

        if (results.isEmpty()) {
            System.out.println("\nNo stations in \"" + zone + "\" currently have " + fuelType + " available.");
            return;
        }

        System.out.println("\nStations with available " + fuelType + " in " + zone + ":");
        System.out.println("--------------------------------------------------");

        for (GasStation station : results) {
            FuelInventory fuel = station.getInventory().get(fuelType);
            System.out.printf(
                "%s | %s | Zone: %s | (%.1f, %.1f) | Line: %s | Price: %.2f ETB/L | Stock: %.0f L%n",
                station.getId(),
                station.getName(),
                station.getZone(),
                station.getX(),
                station.getY(),
                station.hasLine() ? "Yes" : "No",
                fuel.getPrice(),
                fuel.getQuantityLiters()
            );
        }
        System.out.println("--------------------------------------------------");
    }

    private void adminMenu() {
        System.out.println("\n--- Admin: Update Inventory ---");

        System.out.print("Enter station ID (e.g. ST001): ");
        String stationId = scanner.nextLine().trim();

        GasStation station = service.findStationById(stationId);
        if (station == null) {
            System.out.println("Station not found: " + stationId);
            return;
        }

        System.out.printf(
            "%nStation: %s (%s) at (%.1f, %.1f) | Line to get gas: %s%n",
            station.getName(),
            station.getZone(),
            station.getX(),
            station.getY(),
            station.hasLine() ? "Yes" : "No"
        );
        displayInventory(station);

        FuelType fuelType = promptFuelType();
        if (fuelType == null) {
            return;
        }

        if (!station.getInventory().containsKey(fuelType)) {
            System.out.println("This station does not carry " + fuelType + ".");
            return;
        }

        FuelInventory current = station.getInventory().get(fuelType);
        System.out.printf(
            "Current: %.0f L | Available: %s | Price: %.2f ETB/L%n",
            current.getQuantityLiters(),
            current.isAvailable() ? "Yes" : "No",
            current.getPrice()
        );

        System.out.print("New quantity (liters): ");
        double quantity = readPositiveDouble();

        System.out.print("Mark as available? (y/n): ");
        boolean available = scanner.nextLine().trim().equalsIgnoreCase("y");

        boolean success = service.updateInventory(stationId, fuelType, quantity, available);

        if (success) {
            System.out.println("\nInventory updated successfully.");
            displayInventory(station);
        } else {
            System.out.println("\nFailed to update inventory.");
        }
    }

    private void displayInventory(GasStation station) {
        System.out.println("\nInventory for " + station.getId() + ":");
        System.out.println("Fuel Type   | Price    | Quantity | Available");
        System.out.println("------------|----------|----------|----------");

        for (Map.Entry<FuelType, FuelInventory> entry : station.getInventory().entrySet()) {
            FuelInventory fuel = entry.getValue();
            System.out.printf(
                "%-11s | %8.2f | %8.0f | %s%n",
                entry.getKey(),
                fuel.getPrice(),
                fuel.getQuantityLiters(),
                fuel.isAvailable() ? "Yes" : "No"
            );
        }
    }

    private FuelType promptFuelType() {
        System.out.println("\nSelect fuel type:");
        FuelType[] types = FuelType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }

        int choice = readIntInRange("Enter choice: ", 1, types.length);
        return types[choice - 1];
    }

    private int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a whole number.");
            }
        }
    }

    private double readPositiveDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Quantity cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }
    }
}
