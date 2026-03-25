package ee.nimens.inbank.solution.backend.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RequestDecision(
        @Schema(requiredMode = REQUIRED)
        String id,

        @Schema(requiredMode = REQUIRED)
        @Min(value = 2000, message = "Minimum value should be 2000$")
        @Max(value = 10000, message = "Maximum value should be 10000$")
        BigDecimal loanAmount,

        @Schema(requiredMode = REQUIRED)
        @Min(value = 12, message = "Minimum period is 12 months")
        @Max(value = 60, message = "Maximum period is 60 months")
        Integer loanPeriod
) {
}
