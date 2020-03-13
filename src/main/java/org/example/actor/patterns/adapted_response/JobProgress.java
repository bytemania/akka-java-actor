package org.example.actor.patterns.adapted_response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class JobProgress implements Response {
    private final int taskId;
    private final double progress;
}
