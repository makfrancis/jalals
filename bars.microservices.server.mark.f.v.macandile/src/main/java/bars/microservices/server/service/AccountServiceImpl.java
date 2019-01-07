package bars.microservices.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bars.microservices.server.domain.Account;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	AccountRepository repository;

	@Override
	public Optional<Account> findById(int id) {
		return repository.findById(id);
	}

	@Override
	public Account createAccount(Account account) {
		return repository.save(account);
	}

	@Override
	public List<Account> findAllAccounts() {
		return repository.findAll();
	}

}
