package org.example.actor.patterns.future_to_self;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class Update implements Command {
    private final Customer customer;
    private final ActorRef<OperationResult> replyTo;
}
