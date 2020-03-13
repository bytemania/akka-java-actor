package org.example.actor.patterns.adapted_response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class WrappedBackendResponse implements Command {
    private final Response response;
}
