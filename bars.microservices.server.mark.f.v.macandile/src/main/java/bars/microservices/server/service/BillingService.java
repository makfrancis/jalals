package bars.microservices.server.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import bars.microservices.server.domain.Billing;

@Component
public interface BillingService {
	
	public Optional<Billing> findById(int id);
	public Billing findByBillingCycleAndStartDateAndEndDate(int billingCycle, Date startDate, Date endDate);
	public List<Billing> findAllBillings();
	public Billing updateBilling(Billing billing);
	public void deleteBilling(Billing billing);
	public Billing createBilling(Billing billing);
}
