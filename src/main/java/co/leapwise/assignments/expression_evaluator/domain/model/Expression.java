package co.leapwise.assignments.expression_evaluator.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "expressions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Expression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private UUID identifier;
    private String name;
    private String expression;
}