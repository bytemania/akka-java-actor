package org.example.actor.patterns.respond_sharded_actor;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class GetValue implements Command {
    private final String replyToEntityId;
}
