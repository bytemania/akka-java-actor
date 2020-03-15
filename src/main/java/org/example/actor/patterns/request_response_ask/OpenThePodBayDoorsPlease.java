package org.example.actor.patterns.request_response_ask;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class OpenThePodBayDoorsPlease implements HalCommand {
    private final ActorRef<HalResponse> respondTo;
}
