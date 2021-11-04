package com.amido.stacks.core.azure.servicebus.publisher;

import static org.assertj.core.api.BDDAssertions.then;

import com.amido.stacks.core.messaging.listen.DefaultEventListener;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@Tag("Component")
@ExtendWith(MockitoExtension.class)
class UpdateEventServiceBusDispatcherWithListenerTest {

  private JsonMapper jsonMapper;

  @MockBean ServiceBusSenderAsyncClient topicAsyncSender;

  private static final int OPERATION_CODE = 100;

  private static final int EVENT_CODE = 200;

  @Test
  void testCreateMessage() throws JsonProcessingException {

    var uuid = UUID.randomUUID();

    // Given
    UpdateEventServiceBusDispatcherWithListener d =
        new UpdateEventServiceBusDispatcherWithListener(
            topicAsyncSender, JsonMapper.builder().build(), new DefaultEventListener());
    TestApplicationCommand testApplicationCommand = new TestApplicationCommand(uuid.toString());
    TestApplicationEvent testApplicationEvent =
        new TestApplicationEvent(testApplicationCommand, uuid.toString());

    // When
    ServiceBusMessage message = d.createServiceBusMessageFromEvent(testApplicationEvent);

    // Then
    then(message.getMessageId()).isEqualTo(testApplicationEvent.getId().toString());
  }
}
