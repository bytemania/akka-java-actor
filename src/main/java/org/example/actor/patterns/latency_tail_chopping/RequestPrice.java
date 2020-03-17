package org.example.actor.patterns.latency_tail_chopping;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class RequestPrice implements PriceRequester.Command {
    private ActorRef<PriceRequester.Command> replyTo;
}
