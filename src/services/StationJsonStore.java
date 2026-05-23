package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.FuelInventory;
import models.FuelType;
import models.GasStation;

/**
 * Loads and saves gas stations to {@code data/stations.json} without external libraries.
 */
public final class StationJsonStore {

    private static final Pattern STRING_FIELD =
            Pattern.compile("\"(\\w+)\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern NUMBER_FIELD =
            Pattern.compile("\"(\\w+)\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");

    private StationJsonStore() {
    }

    public static List<GasStation> load(Path path) throws IOException {
        String content = Files.readString(path).trim();
        List<String> stationBlocks = splitTopLevelObjects(content);
        List<GasStation> stations = new ArrayList<>();

        for (String block : stationBlocks) {
            String id = requireString(block, "id");
            String name = requireString(block, "name");
            String zone = requireString(block, "zone");
            double x = requireNumber(block, "x");
            double y = requireNumber(block, "y");

            GasStation station = new GasStation(id, name, zone, x, y);
            String fuelsArray = extractArrayContent(block, "fuels");
            for (String fuelBlock : splitTopLevelObjects(fuelsArray)) {
                FuelType type = FuelType.valueOf(requireString(fuelBlock, "type"));
                double quantity = requireNumber(fuelBlock, "quantityLiters");
                double price = requireNumber(fuelBlock, "pricePerLiter");
                boolean available = parseYesNo(requireString(fuelBlock, "available"));
                station.addFuelInventory(type, new FuelInventory(price, quantity, available));
            }
            stations.add(station);
        }
        return stations;
    }

    public static void save(Path path, List<GasStation> stations) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.writeString(path, toJson(stations));
    }

    private static String toJson(List<GasStation> stations) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < stations.size(); i++) {
            GasStation station = stations.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(jsonString(station.getId())).append(",\n");
            sb.append("    \"name\": ").append(jsonString(station.getName())).append(",\n");
            sb.append("    \"zone\": ").append(jsonString(station.getZone())).append(",\n");
            sb.append("    \"x\": ").append(station.getX()).append(",\n");
            sb.append("    \"y\": ").append(station.getY()).append(",\n");
            sb.append("    \"fuels\": [\n");

            int fuelIndex = 0;
            for (Map.Entry<FuelType, FuelInventory> entry : station.getInventory().entrySet()) {
                FuelType type = entry.getKey();
                FuelInventory fuel = entry.getValue();
                sb.append("      {\n");
                sb.append("        \"type\": ").append(jsonString(type.name())).append(",\n");
                sb.append("        \"quantityLiters\": ").append(fuel.getQuantityLiters()).append(",\n");
                sb.append("        \"pricePerLiter\": ").append(fuel.getPrice()).append(",\n");
                sb.append("        \"available\": ")
                        .append(jsonString(fuel.isAvailable() ? "yes" : "no"))
                        .append("\n");
                sb.append("      }");
                if (++fuelIndex < station.getInventory().size()) {
                    sb.append(",");
                }
                sb.append("\n");
            }

            sb.append("    ]\n");
            sb.append("  }");
            if (i < stations.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]\n");
        return sb.toString();
    }

    private static String jsonString(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static boolean parseYesNo(String value) {
        return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("y");
    }

    private static String requireString(String objectJson, String key) {
        Matcher matcher = STRING_FIELD.matcher(objectJson);
        while (matcher.find()) {
            if (matcher.group(1).equals(key)) {
                return matcher.group(2);
            }
        }
        throw new IllegalArgumentException("Missing string field: " + key);
    }

    private static double requireNumber(String objectJson, String key) {
        Matcher matcher = NUMBER_FIELD.matcher(objectJson);
        while (matcher.find()) {
            if (matcher.group(1).equals(key)) {
                return Double.parseDouble(matcher.group(2));
            }
        }
        throw new IllegalArgumentException("Missing number field: " + key);
    }

    private static String extractArrayContent(String objectJson, String arrayKey) {
        String marker = "\"" + arrayKey + "\"";
        int keyIndex = objectJson.indexOf(marker);
        if (keyIndex < 0) {
            throw new IllegalArgumentException("Missing array field: " + arrayKey);
        }
        int openBracket = objectJson.indexOf('[', keyIndex);
        if (openBracket < 0) {
            throw new IllegalArgumentException("Malformed array: " + arrayKey);
        }
        int closeBracket = findMatchingBracket(objectJson, openBracket);
        return objectJson.substring(openBracket + 1, closeBracket);
    }

    private static List<String> splitTopLevelObjects(String arrayContent) {
        List<String> blocks = new ArrayList<>();
        String trimmed = arrayContent.trim();
        if (trimmed.isEmpty()) {
            return blocks;
        }

        int depth = 0;
        int start = -1;
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    blocks.add(trimmed.substring(start, i + 1));
                    start = -1;
                }
            }
        }
        return blocks;
    }

    private static int findMatchingBracket(String text, int openIndex) {
        int depth = 0;
        for (int i = openIndex; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '[') {
                depth++;
            } else if (c == ']') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        throw new IllegalArgumentException("Unclosed JSON array");
    }
}
