/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

/**
 *
 * @author "as2d3f"
 */
@Entity
@NamedQueries({
    @NamedQuery(name="findAll",
                query="SELECT user FROM DbUser user"),
    @NamedQuery(name="findOneByEmail",
                query="SELECT user FROM DbUser user WHERE user.email = :email"),
    @NamedQuery(name="findOneById",
                query="SELECT user FROM DbUser user WHERE user.id = :id"),
    @NamedQuery(name="updateAmount",
                query="update DbUser set amount=:amount WHERE id=:id"),
    @NamedQuery(name="updateAlerts",
                query="update DbUser set alerts=:alerts WHERE id=:id"),
}) 
public class DbUser implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @NotNull
    private String email;
    
	@NotNull
	private String passwd;
	
	private String currency;
	
	private Double amount;
	
	private String dated;
	
	private Integer alerts;
	
	/**
	 *
	 */
	public DbUser() {}

	/**
	 *
	 * @param email
	 * @param passwd
	 * @param name
	 * @param currency
	 * @param amount
	 * @param dated
	 * @param alert
	 */
	public DbUser(String email, String passwd, String name, String currency, Double amount, String dated, Integer alert) {
		
		this.email		= email;
        this.passwd		= passwd;
        this.name		= name;
        this.currency	= currency;
		this.amount		= amount;
		this.dated		= dated;
		this.alerts		= alert;
 
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + Objects.hashCode(this.id);
		hash = 71 * hash + Objects.hashCode(this.name);
		hash = 71 * hash + Objects.hashCode(this.email);
		hash = 71 * hash + Objects.hashCode(this.passwd);
		hash = 71 * hash + Objects.hashCode(this.currency);
		hash = 71 * hash + Objects.hashCode(this.amount);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DbUser other = (DbUser) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.email, other.email)) {
			return false;
		}
		if (!Objects.equals(this.passwd, other.passwd)) {
			return false;
		}
		if (!Objects.equals(this.currency, other.currency)) {
			return false;
		}
		if (!Objects.equals(this.amount, other.amount)) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 *
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 *
	 * @return
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 *
	 * @param passwd
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 *
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 *
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 *
	 * @return
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 *
	 * @param amount
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 *
	 * @return
	 */
	public String getDated() {
		return dated;
	}

	/**
	 *
	 * @param dated
	 */
	public void setDated(String dated) {
		this.dated = dated;
	}

	/**
	 *
	 * @return
	 */
	public Integer getAlerts() {
		return alerts;
	}

	/**
	 *
	 * @param alerts
	 */
	public void setAlerts(Integer alerts) {
		this.alerts = alerts;
	}

	
}
