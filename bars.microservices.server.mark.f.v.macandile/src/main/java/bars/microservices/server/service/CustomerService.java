package bars.microservices.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import bars.microservices.server.domain.Customer;

@Component
public interface CustomerService {
	
	Optional<Customer> findById(int id);
	public Customer createCustomer(Customer customer);
	public List<Customer> findAllCustomers();
}
