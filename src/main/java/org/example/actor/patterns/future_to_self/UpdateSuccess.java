package org.example.actor.patterns.future_to_self;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class UpdateSuccess implements OperationResult {
    private final String id;
}
