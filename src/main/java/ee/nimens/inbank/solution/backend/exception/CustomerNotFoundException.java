package ee.nimens.inbank.solution.backend.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String id) {
        super(String.format("Customer with id %s does not exist", id));
    }

}
