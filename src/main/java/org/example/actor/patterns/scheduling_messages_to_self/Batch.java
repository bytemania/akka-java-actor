package org.example.actor.patterns.scheduling_messages_to_self;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
final class Batch {
    private final List<Command> messages;
}
