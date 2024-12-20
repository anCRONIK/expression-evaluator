package co.leapwise.assignments.expression_evaluator.application.mapper;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonFlattener {

    public Map<String, Object> flatten(Map<String, Object> map) {
        Map<String, Object> flatMap = new HashMap<>();
        flatten("", map, flatMap);
        return flatMap;
    }

    private void flatten(String prefix, Map<String, Object> map, Map<String, Object> flatMap) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                flatten(key, (Map<String, Object>) value, flatMap);
            } else if (value instanceof List) {
                flattenList(key, (List<Object>) value, flatMap);
            } else {
                flatMap.put(key, value);
            }
        }
    }

    private void flattenList(String prefix, List<Object> list, Map<String, Object> flatMap) {
        for (int i = 0; i < list.size(); i++) {
            String key = prefix + "[" + i + "]";
            Object value = list.get(i);
            if (value instanceof Map) {
                flatten(key, (Map<String, Object>) value, flatMap);
            } else if (value instanceof List) {
                flattenList(key, (List<Object>) value, flatMap);
            } else {
                flatMap.put(key, value);
            }
        }
    }
}