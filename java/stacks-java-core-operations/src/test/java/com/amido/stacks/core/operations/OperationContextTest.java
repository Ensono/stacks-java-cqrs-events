package com.amido.stacks.core.operations;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@Tag("Component")
public class OperationContextTest {

  private static final int OPERATION_CODE = 100;

  @Test
  void shouldReturnRightOperationCodeAndCorrelationId() {

    var uuid = UUID.randomUUID();

    OperationContext operationContext =
        Mockito.mock(
            OperationContext.class,
            Mockito.withSettings()
                .useConstructor(OPERATION_CODE, uuid.toString())
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));

    then(operationContext.getOperationCode()).isEqualTo(OPERATION_CODE);
    then(operationContext.getCorrelationId()).isEqualTo(uuid.toString());
  }
}
