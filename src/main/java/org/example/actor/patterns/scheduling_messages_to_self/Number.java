package org.example.actor.patterns.scheduling_messages_to_self;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class Number implements Command {
    private Integer value;
}
