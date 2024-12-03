package co.leapwise.assignments.expression_evaluator.infrastructure.repository;

import co.leapwise.assignments.expression_evaluator.domain.model.Expression;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExpressionRepository extends CrudRepository<Expression, Long> {

    Optional<Expression> findByIdentifier(UUID identifier);

}