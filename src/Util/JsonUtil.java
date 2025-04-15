package Util;

import Model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class JsonUtil {

    // 保存仓库数据到文件
    public static void saveWarehouse(Warehouse warehouse, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"maxCapacity\": ").append(warehouse.getMaxCapacity()).append(",\n");
            sb.append("  \"currentCapacity\": ").append(warehouse.getCurrentCapacity()).append(",\n");
            sb.append("  \"items\": [\n");
            
            List<Item> items = warehouse.getItems();
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                sb.append("    {\n");
                sb.append("      \"name\": \"").append(item.getName()).append("\",\n");
                sb.append("      \"weight\": ").append(item.getWeight()).append(",\n");
                sb.append("      \"type\": \"").append(item.getClass().getSimpleName()).append("\"");
                
                if (item instanceof Consumable) {
                    Consumable consumable = (Consumable) item;
                    sb.append(",\n      \"expirationDate\": \"").append(consumable.getExpirationDate()).append("\"");
                }
                
                if (item instanceof Gun) {
                    Gun gun = (Gun) item;
                    sb.append(",\n      \"bulletCount\": ").append(gun.getBulletCount());
                }
                
                sb.append("\n    }");
                if (i < items.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            
            sb.append("  ]\n");
            sb.append("}");
            
            writer.write(sb.toString());
        }
    }
    
    // 从文件加载仓库数据
    public static Warehouse loadWarehouse(String filePath) throws IOException {
        StringBuilder jsonStr = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStr.append(line).append("\n");
            }
        }
        
        String json = jsonStr.toString();
        
        // 解析最大容量
        String maxCapacityStr = extractValue(json, "maxCapacity");
        double maxCapacity = Double.parseDouble(maxCapacityStr);
        
        Warehouse warehouse = new Warehouse(maxCapacity);
        
        // 解析items数组
        int itemsStart = json.indexOf("\"items\"");
        int arrayStart = json.indexOf("[", itemsStart);
        int arrayEnd = findMatchingBracket(json, arrayStart);
        
        String itemsArray = json.substring(arrayStart + 1, arrayEnd);
        
        // 解析每个item对象
        int pos = 0;
        while (pos < itemsArray.length()) {
            int objectStart = itemsArray.indexOf("{", pos);
            if (objectStart == -1) break;
            
            int objectEnd = findMatchingBracket(itemsArray, objectStart);
            String itemJson = itemsArray.substring(objectStart, objectEnd + 1);
            
            try {
                Item item = parseItem(itemJson);
                if (item != null) {
                    warehouse.addItem(item);
                }
            } catch (Exception e) {
                System.err.println("Error parsing item: " + e.getMessage());
            }
            
            pos = objectEnd + 1;
        }
        
        return warehouse;
    }
    
    private static Item parseItem(String itemJson) {
        String name = extractValue(itemJson, "name").replace("\"", "");
        String weightStr = extractValue(itemJson, "weight");
        double weight = Double.parseDouble(weightStr);
        String type = extractValue(itemJson, "type").replace("\"", "");
        
        Item item = null;
        
        switch (type) {
            case "Food":
                String foodExpDateStr = extractValue(itemJson, "expirationDate").replace("\"", "");
                LocalDate foodExpDate = LocalDate.parse(foodExpDateStr);
                item = new Food(name, weight, foodExpDate);
                break;
            case "Drink":
                String drinkExpDateStr = extractValue(itemJson, "expirationDate").replace("\"", "");
                LocalDate drinkExpDate = LocalDate.parse(drinkExpDateStr);
                item = new Drink(name, weight, drinkExpDate);
                break;
            case "Bomb":
                item = new Bomb(name, weight);
                break;
            case "Gun":
                String bulletCountStr = extractValue(itemJson, "bulletCount");
                int bulletCount = Integer.parseInt(bulletCountStr);
                item = new Gun(name, weight, bulletCount);
                break;
        }
        
        return item;
    }
    
    private static String extractValue(String json, String key) {
        String keyPattern = "\"" + key + "\"";
        int keyIndex = json.indexOf(keyPattern);
        if (keyIndex == -1) return "";
        
        int colonIndex = json.indexOf(":", keyIndex);
        int valueStart = colonIndex + 1;
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }
        
        char firstChar = json.charAt(valueStart);
        
        if (firstChar == '"') {
            int valueEnd = json.indexOf("\"", valueStart + 1);
            while (valueEnd > 0 && json.charAt(valueEnd - 1) == '\\') {
                valueEnd = json.indexOf("\"", valueEnd + 1);
            }
            return json.substring(valueStart, valueEnd + 1);
        } else if (firstChar == '{') {
            int valueEnd = findMatchingBracket(json, valueStart);
            return json.substring(valueStart, valueEnd + 1);
        } else if (firstChar == '[') {
            int valueEnd = findMatchingBracket(json, valueStart);
            return json.substring(valueStart, valueEnd + 1);
        } else {
            int valueEnd = valueStart;
            while (valueEnd < json.length() && 
                   json.charAt(valueEnd) != ',' && 
                   json.charAt(valueEnd) != '}' &&
                   json.charAt(valueEnd) != ']' &&
                   !Character.isWhitespace(json.charAt(valueEnd))) {
                valueEnd++;
            }
            return json.substring(valueStart, valueEnd).trim();
        }
    }
    
    private static int findMatchingBracket(String json, int openBracketIndex) {
        char openBracket = json.charAt(openBracketIndex);
        char closeBracket;
        if (openBracket == '{') closeBracket = '}';
        else if (openBracket == '[') closeBracket = ']';
        else if (openBracket == '(') closeBracket = ')';
        else return -1;
        
        int count = 1;
        for (int i = openBracketIndex + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == openBracket) count++;
            else if (c == closeBracket) {
                count--;
                if (count == 0) return i;
            }
        }
        return -1;
    }
} 