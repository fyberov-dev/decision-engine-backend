package ee.nimens.inbank.solution.backend.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ResponseDecision(
        Decision decision,
        BigDecimal amount,
        Integer period
) {
}
