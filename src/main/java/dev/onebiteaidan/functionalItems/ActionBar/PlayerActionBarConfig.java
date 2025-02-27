package dev.onebiteaidan.functionalItems.ActionBar;

import java.util.*;

public class PlayerActionBarConfig {
    private final LinkedHashMap<String, Boolean> componentStates = new LinkedHashMap<>(); // Stores component order & enabled state

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

    public LinkedHashMap<String, Boolean> getComponentStates() {
        return componentStates;
    }

    public LinkedList<String> getEnabledComponents() {
        return new LinkedList<>(componentStates.entrySet().stream()
                .filter(Map.Entry::getValue) // Keep only entries where value is true
                .map(Map.Entry::getKey)     // Extract the keys
                .toList());
    }

    public void reorderKeys(List<String> keys) {
        LinkedHashMap<String, Boolean> componentStatesReordered = new LinkedHashMap<>();

        for (String key : keys) {
            componentStatesReordered.put(key, componentStates.get(key));
        }
        this.componentStates.clear();

        for (Map.Entry<String, Boolean> entry : componentStatesReordered.sequencedEntrySet()) {
            this.componentStates.put(entry.getKey(), entry.getValue());
        }
    }
}