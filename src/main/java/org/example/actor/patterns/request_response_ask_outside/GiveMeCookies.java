package org.example.actor.patterns.request_response_ask_outside;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class GiveMeCookies implements Command {
    private final int count;
    private final ActorRef<Reply> replyTo;
}
