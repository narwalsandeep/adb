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

@Entity
@NamedQueries({
    @NamedQuery(name="findAllSentByUserId",
                query="SELECT tx FROM DbTransaction tx WHERE tx.status='SUCCESS' and tx.senderId=:senderId"),
    @NamedQuery(name="findAllRequestByUserId",
                query="SELECT tx FROM DbTransaction tx WHERE tx.receiverId = :receiverId"),
    @NamedQuery(name="findAllPendingRequestByUserId",
                query="SELECT tx FROM DbTransaction tx WHERE tx.status='REQUEST AWAITING APPROVAL' and tx.senderId = :senderId"),    
	@NamedQuery(name="approvePayment",
                query="update DbTransaction set status='SUCCESS' WHERE id=:id"),
	@NamedQuery(name="rejectPayment",
                query="update DbTransaction set status='REJECT' WHERE id=:id"),

}) 

public class DbTransaction implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
	private Long senderId;
    
    @NotNull
    private Long receiverId;
    
	private Double amount;

    @NotNull
	private String status;

	/**
	 *
	 */
	public DbTransaction() {
	}

	/**
	 *
	 * @param senderId
	 * @param receiverId
	 * @param amount
	 * @param STATUS_SUCCESS
	 */
	public DbTransaction(Long senderId, Long receiverId, Double amount, String STATUS_SUCCESS) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.amount = amount;
		this.status = STATUS_SUCCESS;
	}
	
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 13 * hash + Objects.hashCode(this.id);
		hash = 13 * hash + Objects.hashCode(this.senderId);
		hash = 13 * hash + Objects.hashCode(this.receiverId);
		hash = 13 * hash + Objects.hashCode(this.amount);
		hash = 13 * hash + Objects.hashCode(this.status);
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
		final DbTransaction other = (DbTransaction) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.senderId, other.senderId)) {
			return false;
		}
		if (!Objects.equals(this.receiverId, other.receiverId)) {
			return false;
		}
		if (!Objects.equals(this.amount, other.amount)) {
			return false;
		}
		if (!Objects.equals(this.status, other.status)) {
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
	public Long getSenderId() {
		return senderId;
	}

	/**
	 *
	 * @param senderId
	 */
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	/**
	 *
	 * @return
	 */
	public Long getReceiverId() {
		return receiverId;
	}

	/**
	 *
	 * @param receiverId
	 */
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
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
	public String getStatus() {
		return status;
	}

	/**
	 *
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
