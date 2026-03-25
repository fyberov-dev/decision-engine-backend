package ee.nimens.inbank.solution.backend.repository;

import ee.nimens.inbank.solution.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
