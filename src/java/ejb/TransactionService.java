/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.DbTransaction;
import entity.DbUser;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import jsf.LoginBean;

/**
 *
 * @author "as2d3f"
 */
@Stateless
public class TransactionService {

	@PersistenceContext
    EntityManager em;

	@EJB
	UserService user;
	
	/**
	 *
	 */
	public final String STATUS_SUCCESS = "SUCCESS";

	/**
	 *
	 */
	public final String STATUS_REQUEST_AWAITING_APPROVAL = "REQUEST AWAITING APPROVAL";

	/**
	 *
	 */
	public final String STATUS_REQUEST_REJECTED = "REQUEST REJECTED";
	
	/**
	 *
	 * @return
	 */
	public synchronized List<DbTransaction> findAll() {
        List<DbTransaction> tx = em.createNamedQuery("findAllByUser").getResultList();
        return tx;
    }

	/**
	 *
	 * @param senderId
	 * @param receiverId
	 * @param txType
	 * @param amount
	 */
	public void doTransaction(Long senderId, Long receiverId,  Double amount, String txType) {

		DbTransaction tx;
		
		if("SEND".equals(txType)) {
			deductTotalAmount(senderId,amount);
			addTotalAmount(receiverId,amount);
			tx = new DbTransaction(senderId, receiverId, amount, STATUS_SUCCESS);
		} else {
			tx = new DbTransaction(senderId, receiverId, amount, STATUS_REQUEST_AWAITING_APPROVAL);
		}
			
		em.persist(tx);

	}

	public List<DbTransaction> findAllSentByUserId(Long userId) {
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("findAllSentByUserId",DbTransaction.class);
			List<DbTransaction> tx = query.setParameter("senderId", userId).getResultList();
			return tx;
		} catch(NoResultException e) {
			return null;
		}
	}

	public List<DbTransaction> findAllRequestByUserId(Long userId) {
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("findAllRequestByUserId",DbTransaction.class);
			List<DbTransaction> tx = query.setParameter("receiverId", userId).getResultList();
			return tx;
		} catch(NoResultException e) {
			return null;
		}
	}

	public List<DbTransaction> findAllPendingRequestByUserId(Long userId) {
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("findAllPendingRequestByUserId",DbTransaction.class);
			List<DbTransaction> tx = query.setParameter("senderId", userId).getResultList();
			return tx;
		} catch(NoResultException e) {
			return null;
		}
	}

	private int deductTotalAmount(Long senderId, Double amount) {

		try{
			
			Double lastAmount = user.findOneById(senderId).getAmount();
			Double currentAmount = (double)lastAmount - (double)amount;
			
			TypedQuery<DbUser> query = em.createNamedQuery("deductTotalAmount",DbUser.class);
			query.setParameter("id", senderId);
			query.setParameter("amount", currentAmount);
			int tx = query.executeUpdate();
			
			return tx;
		} catch(NoResultException e) {
			//return 0;
			System.out.println(e.getMessage());
		}
		return 0;
		
		
	}

	private void addTotalAmount(Long receiverId, Double amount) {
	}

	public void approvePayment(Long id) {
		
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("approvePayment",DbTransaction.class);
			query.setParameter("id", id);
			query.executeUpdate();			
		} catch(NoResultException e) {
		}
	}

	public void rejectPayment(Long id) {
		
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("rejectPayment",DbTransaction.class);
			query.setParameter("id", id);
			query.executeUpdate();			
		} catch(NoResultException e) {
		}
	}



}
