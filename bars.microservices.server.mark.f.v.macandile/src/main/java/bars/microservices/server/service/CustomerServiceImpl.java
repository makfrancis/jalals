package bars.microservices.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bars.microservices.server.domain.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerRepository repository;

	@Override
	public Optional<Customer> findById(int id) {
		return repository.findById(id);
	}

	@Override
	public Customer createCustomer(Customer customer) {
		return repository.save(customer);
	}

	@Override
	public List<Customer> findAllCustomers() {
		return repository.findAll();
	}

}
