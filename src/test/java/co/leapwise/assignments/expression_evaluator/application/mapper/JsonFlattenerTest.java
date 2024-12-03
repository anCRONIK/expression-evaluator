package co.leapwise.assignments.expression_evaluator.application.mapper;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JsonFlattenerTest {

    @Test
    public void flatten_EmptyMap() {
        Map<String, Object> emptyMap = new HashMap<>();

        JsonFlattener flattener = new JsonFlattener();
        Map<String, Object> flatMap = flattener.flatten(emptyMap);

        assertThat(flatMap).isEmpty();
    }

    @Test
    public void flatten_NullValue() {
        Map<String, Object> mapWithNull = new HashMap<>();
        mapWithNull.put("customer", null);

        JsonFlattener flattener = new JsonFlattener();
        Map<String, Object> flatMap = flattener.flatten(mapWithNull);

        assertThat(flatMap).containsEntry("customer", null);
    }

    @Test
    public void flatten_NullMap() {
        JsonFlattener flattener = new JsonFlattener();
        assertThatThrownBy(() -> flattener.flatten(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void flatten_SingleLevelMap() {
        Map<String, Object> singleLevelMap = new HashMap<>();
        singleLevelMap.put("firstName", "John");
        singleLevelMap.put("salary", 100);

        JsonFlattener flattener = new JsonFlattener();
        Map<String, Object> flatMap = flattener.flatten(singleLevelMap);

        assertThat(flatMap).containsEntry("firstName", "John");
        assertThat(flatMap).containsEntry("salary", 100);
    }

    @Test
    public void flatten_NestedMap() {
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("customer", Map.of(
                "firstName", "John",
                "salary", 100,
                "address", Map.of(
                        "city", "Washington",
                        "zipCode", 12345
                )
        ));

        JsonFlattener flattener = new JsonFlattener();
        Map<String, Object> flatMap = flattener.flatten(nestedMap);

        assertThat(flatMap).containsEntry("customer.firstName", "John");
        assertThat(flatMap).containsEntry("customer.salary", 100);
        assertThat(flatMap).containsEntry("customer.address.city", "Washington");
        assertThat(flatMap).containsEntry("customer.address.zipCode", 12345);
    }
}