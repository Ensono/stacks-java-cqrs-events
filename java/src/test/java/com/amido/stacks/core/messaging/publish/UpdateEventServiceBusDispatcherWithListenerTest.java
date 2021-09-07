package com.amido.stacks.core.messaging.publish;

import static org.assertj.core.api.BDDAssertions.then;

import com.amido.stacks.core.messaging.listen.DefaultEventListener;
import com.amido.stacks.menu.commands.CreateCategoryCommand;
import com.amido.stacks.menu.events.MenuCreatedEvent;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@Tag("Component")
class UpdateEventServiceBusDispatcherWithListenerTest {

  @MockBean ServiceBusSenderAsyncClient topicAsyncSender;

  @Test
  void testCreateMessage() throws JsonProcessingException {

    // Given
    UpdateEventServiceBusDispatcherWithListener d =
        new UpdateEventServiceBusDispatcherWithListener(topicAsyncSender, JsonMapper.builder().build(), new DefaultEventListener());

    // When
    CreateCategoryCommand command =
        new CreateCategoryCommand("CorrelationId", UUID.randomUUID(), "name", "description");
    MenuCreatedEvent applicationEvent = new MenuCreatedEvent(command, command.getMenuId());

    ServiceBusMessage message = d.createServiceBusMessageFromEvent(applicationEvent);

    // Then
    then(message.getMessageId()).isEqualTo(applicationEvent.getId().toString());
  }
}
