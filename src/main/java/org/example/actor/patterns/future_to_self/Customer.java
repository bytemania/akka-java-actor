package org.example.actor.patterns.future_to_self;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class Customer {
    private final String id;
    private final long version;
    private final String name;
    private final String address;
}
