package bars.microservices.server.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.accenture.sef.xml.impl.BarsWriteXmlUtils;
import com.accenture.sef.xml.interfce.BarsWriteXMLUtilsInterface;

import bars.microservices.server.domain.Account;
import bars.microservices.server.domain.Billing;
import bars.microservices.server.domain.Customer;
import bars.microservices.server.domain.Record;
import bars.microservices.server.domain.Request;
import bars.microservices.server.exception.BarsException;
import bars.microservices.server.service.AccountService;
import bars.microservices.server.service.BillingService;
import bars.microservices.server.service.CustomerService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
public class BarsController {
	
	@Autowired
	BillingService billingService;
	@Autowired
	AccountService accountService;
	@Autowired
	CustomerService customerService;

	@GetMapping("/billings")
	public List<Billing> billings() {
		return billingService.findAllBillings();
	}
	
	@GetMapping("/accounts")
	public List<Account> accounts() {
		return accountService.findAllAccounts();
	}
	
	@GetMapping("/customers")
	public List<Customer> customers() {
		return customerService.findAllCustomers();
	}
	
	
	@PostMapping("/csv")
	public List<Record> displayRecords(@RequestBody String requests) throws BarsException, ParseException {
		System.out.println("Requests :" + requests);
		List<Request> requestss = new ArrayList<>();
		List<Record> records = new ArrayList<>();
		SimpleDateFormat sdf;
		String regex = "([0-9]{2})/([0-9]{2})/([0-9]{4})";
		for(String line : requests.split("\n")) {
			String[] data = line.trim().split(",");
			requestss.add(new Request(Integer.parseInt(data[0]),data[1],data[2]));
		}
		
			sdf = new SimpleDateFormat("MM/dd/yyyy");
		
			int counter = 0;
		for(Request request: requestss) {
			counter++;
			if(request.getBillingCycle() < 0 || request.getBillingCycle() > 12) {
				System.out.println(request.getBillingCycle());
				throw new BarsException(BarsException.BILLING_CYCLE_NOT_ON_RANGE + counter);
			}
			int billingCycle = request.getBillingCycle();
			if(!(request.getStartDate().matches(regex))) {
				System.out.println(request.getStartDate());
				throw new BarsException(BarsException.INVALID_START_DATE_FORMAT + counter);
			}
			Date startDate = sdf.parse(request.getStartDate());
			if(!(request.getEndDate().matches(regex))) {
				System.out.println(request.getEndDate());
				throw new BarsException(BarsException.INVALID_END_DATE_FORMAT + counter);
			}
			Date endDate = sdf.parse(request.getEndDate());
			Billing billing = billingService.findByBillingCycleAndStartDateAndEndDate(
					billingCycle, startDate, endDate);
			Account account = accountService.findById(billing.getAccountId()).orElseThrow(() -> new BarsException(BarsException.NO_RECORDS_TO_READ));
			Customer customer = customerService.findById(account.getCustomerId()).orElseThrow(() -> new BarsException(BarsException.NO_RECORDS_TO_READ));
			
			records.add(new Record(billingCycle, startDate, 
					endDate,account.getAccountName(),customer.getFirstName(),
					customer.getLastName(),billing.getAmount()));
		}
		if(records.isEmpty()) {
			throw new BarsException(BarsException.NO_RECORDS_TO_WRITE);
		}
		try {
			writeXML(records);
		} catch(Exception e) {
			throw new BarsException(BarsException.PATH_DOES_NOT_EXIST);
		}
		return records;
	}
	
	@PostMapping("/txt")
	public List<Record> displayTxtRecords(@RequestBody String requests) throws BarsException, ParseException {
		List<Request> requestss = new ArrayList<>();
		List<Record> records = new ArrayList<>();
		SimpleDateFormat sdf;
		String regex = "([0-9]{8})";
		for(String line : requests.trim().split("\n")) {
			requestss.add(new Request(Integer.parseInt(line.substring(0,2).trim()),line.substring(2,10).trim(),line.substring(10,18).trim()));
		}
		
		sdf = new SimpleDateFormat("MMddyyyy");
		int counter = 0;
		for(Request request: requestss) {
			counter++;
			if(request.getBillingCycle() < 0 || request.getBillingCycle() > 12) {
				System.out.println(request.getBillingCycle());
				throw new BarsException(BarsException.BILLING_CYCLE_NOT_ON_RANGE + counter);
			}
			System.out.println("Start :" + request.getStartDate() + "\t" + (request.getStartDate().matches(regex)));
			System.out.println("End :" + request.getEndDate() + "\t" + (request.getEndDate().matches(regex)));
			int billingCycle = request.getBillingCycle();
			if(!(request.getStartDate().matches(regex))) {
				System.out.println(request.getStartDate());
				throw new BarsException(BarsException.INVALID_START_DATE_FORMAT + counter);
			}
			else if(!(request.getEndDate().matches(regex))) {
				System.out.println(request.getEndDate());
				throw new BarsException(BarsException.INVALID_END_DATE_FORMAT + counter);
			}
			Date startDate = sdf.parse(request.getStartDate());
			Date endDate = sdf.parse(request.getEndDate());
			Billing billing = billingService.findByBillingCycleAndStartDateAndEndDate(
					billingCycle, startDate, endDate);
			if(billing==null)
				continue;
			Account account = accountService.findById(billing.getAccountId()).orElseThrow(() -> new BarsException(BarsException.NO_RECORDS_TO_READ));
			Customer customer = customerService.findById(account.getCustomerId()).orElseThrow(() -> new BarsException(BarsException.NO_RECORDS_TO_READ));
			
			records.add(new Record(billingCycle, startDate, 
					endDate,account.getAccountName(),customer.getFirstName(),
					customer.getLastName(),billing.getAmount()));
		}
		if(records.isEmpty())
			throw new BarsException(BarsException.NO_RECORDS_TO_WRITE);
		try {
			writeXML(records);
		} catch(Exception e) {
			throw new BarsException(BarsException.PATH_DOES_NOT_EXIST);
		}
		return records;
	}
	
	public void writeXML (List<Record> records) throws BarsException, IOException {
		BarsWriteXMLUtilsInterface x = new BarsWriteXmlUtils();
		Document doc = x.createXMLDocument();
		Element rootElement = x.createDocumentElement(doc, "BARS");
		int counter = 1;
		for(Record record : records) {
			Element recordElement = x.createChildElement(doc, rootElement, "record");
			x.createElementAttribute(doc, recordElement, "id", Integer.toString(counter));
			x.createElementTextNode(doc, recordElement, "billing-cycle", Integer.toString(record.getBillingCycle()));
			x.createElementTextNode(doc, recordElement, "start-date", new SimpleDateFormat("yyyy-MM-dd").format(record.getStartDate()));
			x.createElementTextNode(doc, recordElement, "end-date", new SimpleDateFormat("yyyy-MM-dd").format(record.getEndDate()));
			x.createElementTextNode(doc, recordElement, "first-name", record.getFirstName());
			x.createElementTextNode(doc, recordElement, "last-name", record.getLastName());
			x.createElementTextNode(doc, recordElement, "amount", Double.toString(record.getAmount()));
			counter++;
		}	
		System.out.println("before create");
		File file = new File("C:" + System.getProperty("file.separator") + "BARS" + System.getProperty("file.separator") + "Report" + System.getProperty("file.separator")+"BARS_Report-"+new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date())+".xml");
		if(file.createNewFile()==false) {
			System.out.println("False siya");
			throw new BarsException(BarsException.PATH_DOES_NOT_EXIST);
		}
		x.transformToXML(doc, "C:" + System.getProperty("file.separator") + "BARS" + System.getProperty("file.separator") + "Report" + System.getProperty("file.separator")+"BARS_Report-"+new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date())+".xml");
		
	}
}