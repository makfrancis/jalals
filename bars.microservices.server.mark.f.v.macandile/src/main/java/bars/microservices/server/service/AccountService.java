package bars.microservices.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import bars.microservices.server.domain.Account;

@Component
public interface AccountService {
	Optional<Account> findById(int id);
	public Account createAccount(Account account);
	public List<Account> findAllAccounts();
}
