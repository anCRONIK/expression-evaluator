package co.leapwise.assignments.expression_evaluator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaRepositories
public class ExpressionEvaluatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpressionEvaluatorApplication.class, args);
	}

}
