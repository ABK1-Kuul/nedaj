package presentation;

import services.GasStationService;

public class Main {
    public static void main(String[] args) {
        GasStationService service = new GasStationService();
        service.loadStations();

        ConsoleMenu menu = new ConsoleMenu(service);
        menu.start();
    }
}
