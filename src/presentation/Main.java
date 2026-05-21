package presentation;

import services.GasStationService;

public class Main {
    public static void main(String[] args) {
        GasStationService service = new GasStationService();
        service.seedMockData();

        ConsoleMenu menu = new ConsoleMenu(service);
        menu.start();
    }
}
