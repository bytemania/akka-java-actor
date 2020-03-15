package org.example.actor.patterns.request_response_ask_outside;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class InvalidRequest implements Reply {
    private final String reason;
}
