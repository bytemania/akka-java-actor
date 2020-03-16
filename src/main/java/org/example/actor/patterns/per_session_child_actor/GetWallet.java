package org.example.actor.patterns.per_session_child_actor;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class GetWallet implements Command {
    private final String whoseWallet;
    private final ActorRef<Item> replyTo;
}
