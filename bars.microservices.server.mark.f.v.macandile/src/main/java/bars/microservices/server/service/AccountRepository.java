package bars.microservices.server.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bars.microservices.server.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	Account findByCustomerId(int id);
}
