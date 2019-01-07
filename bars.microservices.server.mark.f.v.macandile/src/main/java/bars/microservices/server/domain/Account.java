package bars.microservices.server.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Account {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_id")
	private Integer id;
	private String accountName;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	@Column(length = 1)
	private char isActive;
	private String lastEdited;
	private int customerId;	
}
