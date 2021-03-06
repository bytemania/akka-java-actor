package org.example.actor.patterns.adapted_response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.net.URI;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class JobCompleted implements Response {
    private final int taskId;
    private final URI resut;
}
