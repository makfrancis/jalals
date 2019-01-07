package bars.microservices.server.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bars.microservices.server.domain.Billing;

@Service
public class BillingServiceImpl implements BillingService {
	
	@Autowired
	BillingRepository repository;

	@Override
	public Optional<Billing> findById(int id) {
		return repository.findById(id);
	}
	
	
	@Override
	public Billing findByBillingCycleAndStartDateAndEndDate(int billingCycle, Date startDate, Date endDate) {
		return repository.findByBillingCycleAndStartDateAndEndDate(billingCycle, startDate, endDate);
	}

	@Override
	public List<Billing> findAllBillings() {
		return repository.findAll();
	}

	@Override
	public Billing updateBilling(Billing billing) {
		//Optional<Billing> theBilling = repository.findById(billing.getId());
		
		return null;
	}

	@Override
	public void deleteBilling(Billing billing) {
		repository.delete(billing);
		
	}


	@Override
	public Billing createBilling(Billing billing) {
		return repository.save(billing);
	}

}
