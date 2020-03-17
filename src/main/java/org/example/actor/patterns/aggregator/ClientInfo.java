package org.example.actor.patterns.aggregator;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString
class ClientInfo {
    private final String hotel;
    private final BigDecimal quote;
}
