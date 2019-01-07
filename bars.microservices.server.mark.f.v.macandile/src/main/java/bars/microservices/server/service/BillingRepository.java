package bars.microservices.server.service;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bars.microservices.server.domain.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {
	Billing findByAccountId(int id);
	Billing findByBillingCycleAndStartDateAndEndDate(int billingCycle, Date startDate, Date endDate);

}
