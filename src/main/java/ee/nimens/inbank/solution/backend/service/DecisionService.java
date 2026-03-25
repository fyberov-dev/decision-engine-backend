package ee.nimens.inbank.solution.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import ee.nimens.inbank.solution.backend.dto.Decision;
import ee.nimens.inbank.solution.backend.dto.RequestDecision;
import ee.nimens.inbank.solution.backend.dto.ResponseDecision;
import ee.nimens.inbank.solution.backend.exception.CustomerNotFoundException;
import ee.nimens.inbank.solution.backend.model.Customer;
import ee.nimens.inbank.solution.backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DecisionService {

    private final CustomerRepository customerRepository;

    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(2000L);
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(10000L);

    private static final int MIN_PERIOD_MONTHS = 12;
    private static final int MAX_PERIOD_MONTHS = 60;

    public ResponseDecision makeDecision(RequestDecision request) {
        String id = request.id();
        Customer customer = findCustomerById(id);

        int creditModifier = customer.getCreditModifier();
        BigDecimal amount = request.loanAmount();
        Integer period = request.loanPeriod();

        if (customer.isHasDebt()) {
            log.info("Customer with id={} is in debt", id);

            return declineRequest(amount, period);
        }

        BigDecimal creditScore = getCreditScore(creditModifier, amount, period);
        if (creditScore.compareTo(BigDecimal.ONE) >= 0) {
            BigDecimal maxAmountToApprove = MAX_AMOUNT.min(amount.multiply(creditScore));

            log.info("Customer with an ID={} APPROVED. amount={}, period={}", id, maxAmountToApprove, period);

            return approveRequest(maxAmountToApprove, period);
        }

        BigDecimal largestSumToApprove = getLargestSumToApprove(creditModifier, period);
        if (largestSumToApprove.compareTo(MIN_AMOUNT) >= 0) {
            BigDecimal maxAmountToApprove = MAX_AMOUNT.min(largestSumToApprove);

            log.info("Customer with an ID={} APPROVED. amount={}, period={}", id, maxAmountToApprove, period);

            return approveRequest(maxAmountToApprove, period);
        }

        int newPeriod = getNewPeriod(creditModifier);
        if (newPeriod <= MAX_PERIOD_MONTHS) {
            int maxPeriodToApprove = Math.max(MIN_PERIOD_MONTHS, newPeriod);
            BigDecimal maxAmountToApprove = getLargestSumToApprove(creditModifier, maxPeriodToApprove);

            log.info("Customer with an ID={} APPROVED. amount={}, period={}", id, maxAmountToApprove, maxPeriodToApprove);

            return approveRequest(maxAmountToApprove, maxPeriodToApprove);
        }

        log.info("Customer with an ID={} DECLINED. amount={}, period={}", id, amount, period);

        return declineRequest(amount, period);
    }

    private BigDecimal getCreditScore(int creditModifier, BigDecimal loanAmount, int loanPeriod) {
        return BigDecimal.valueOf(creditModifier)
                .multiply(BigDecimal.valueOf(loanPeriod))
                .divide(loanAmount, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getLargestSumToApprove(int creditModifier, int loanPeriod) {
        return BigDecimal.valueOf(creditModifier)
                .multiply(BigDecimal.valueOf(loanPeriod));
    }

    private int getNewPeriod(int creditModifier) {
        return MIN_AMOUNT.divide(BigDecimal.valueOf(creditModifier), 0, RoundingMode.UP).intValue();
    }

    private Customer findCustomerById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    private ResponseDecision approveRequest(BigDecimal amount, int period) {
        return ResponseDecision.builder()
                .decision(Decision.APPROVED)
                .amount(amount)
                .period(period)
                .build();
    }

    private ResponseDecision declineRequest(BigDecimal amount, int period) {
        return ResponseDecision.builder()
                .decision(Decision.DECLINED)
                .amount(amount)
                .period(period)
                .build();
    }

}
