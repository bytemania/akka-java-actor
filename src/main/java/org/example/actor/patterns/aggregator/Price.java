package org.example.actor.patterns.aggregator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@RequiredArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
public class Price {
    private final String hotel;
    private final BigDecimal price;
}
