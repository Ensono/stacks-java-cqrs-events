package com.amido.stacks.core.operations;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;

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
