package com.xxAMIDOxx.xxSTACKSxx.core.cqrs.handler;

import com.xxAMIDOxx.xxSTACKSxx.core.cqrs.command.ApplicationCommand;
import java.util.Optional;
import java.util.UUID;

public interface CommandHandler<T extends ApplicationCommand> {
  Optional<UUID> handle(T command);
}
