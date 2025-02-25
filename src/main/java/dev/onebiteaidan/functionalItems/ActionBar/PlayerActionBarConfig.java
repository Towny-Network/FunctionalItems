package dev.onebiteaidan.functionalItems.ActionBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayerActionBarConfig {
    private final Map<String, Boolean> componentStates = new LinkedHashMap<>(); // Stores component order & enabled state

    public void addComponent(String key) {
        componentStates.putIfAbsent(key, true); // Default to enabled
    }

    public void setEnabled(String key, boolean enabled) {
        if (componentStates.containsKey(key)) {
            componentStates.put(key, enabled);
        }
    }

    public boolean isEnabled(String key) {
        return componentStates.getOrDefault(key, false);
    }

    public void reorderComponent(String movingKey, int newPos) {
        String[] keys = componentStates.keySet().toArray(new String[0]);
        // Remove and reinsert in new position
        componentStates.remove(movingKey);
        Map<String, Boolean> reordered = new LinkedHashMap<>();
        int index = 0;
        for (String key : keys) {
            if (index == newPos) reordered.put(movingKey, componentStates.get(movingKey));
            reordered.put(key, componentStates.get(key));
            index++;
        }
        componentStates.clear();
        componentStates.putAll(reordered);
    }

    public void reorderComponent(int oldPos, int newPos) {
        if (oldPos < 0 || newPos < 0 || oldPos >= componentStates.size() || newPos >= componentStates.size()) return;

        String[] keys = componentStates.keySet().toArray(new String[0]);
        String movingKey = keys[oldPos];

        // Remove and reinsert in new position
        componentStates.remove(movingKey);
        Map<String, Boolean> reordered = new LinkedHashMap<>();
        int index = 0;
        for (String key : keys) {
            if (index == newPos) reordered.put(movingKey, componentStates.get(movingKey));
            reordered.put(key, componentStates.get(key));
            index++;
        }
        componentStates.clear();
        componentStates.putAll(reordered);
    }

    public String getComponentAtIndex(int index) {
        return componentStates.keySet().toArray(new String[0])[index];
    }

    public Map<String, Boolean> getComponentStates() {
        return componentStates;
    }

    public List<String> getEnabledComponents() {
        List<String> keys = new ArrayList<>();
        for (String key : componentStates.keySet()) {
            if (componentStates.get(key)) {
                keys.add(key);
            }
        }
        return keys;
    }

    public String buildActionBar() {
        return componentStates.entrySet().stream()
                .filter(Map.Entry::getValue) // Only include enabled components
                .map(Map.Entry::getKey)
                .reduce((a, b) -> a + " | " + b).orElse("");
    }
}
