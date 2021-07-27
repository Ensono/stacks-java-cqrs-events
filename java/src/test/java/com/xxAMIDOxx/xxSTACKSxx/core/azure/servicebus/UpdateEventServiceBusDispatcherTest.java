package com.xxAMIDOxx.xxSTACKSxx.core.azure.servicebus;

import static org.assertj.core.api.BDDAssertions.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.TopicClient;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuCreatedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@Tag("Component")
class UpdateEventServiceBusDispatcherTest {

  @MockBean TopicClient topicClient;

  @Test
  void testCreateMessage() throws JsonProcessingException {

    // Given
    UpdateEventServiceBusDispatcher d =
        new UpdateEventServiceBusDispatcher(topicClient, JsonMapper.builder().build());

    // When
    CreateCategoryCommand command =
        new CreateCategoryCommand("CorrelationId", UUID.randomUUID(), "name", "description");
    MenuCreatedEvent applicationEvent = new MenuCreatedEvent(command, command.getMenuId());

    Message message = d.createMessageFromEvent(applicationEvent);

    // Then
    then(message.getMessageId()).isEqualTo(applicationEvent.getId().toString());
  }
}
