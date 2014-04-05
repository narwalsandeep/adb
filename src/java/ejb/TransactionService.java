/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.DbTransaction;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author "as2d3f"
 */
@Stateless
public class TransactionService {

	@PersistenceContext
    EntityManager em;

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
			tx = new DbTransaction(senderId, receiverId, amount, STATUS_SUCCESS);
		} else {
			tx = new DbTransaction(senderId, receiverId, amount, STATUS_REQUEST_AWAITING_APPROVAL);
		}
			
		em.persist(tx);

	}

	/**
	 *
	 * @param userId
	 * @return
	 */
	public List<DbTransaction> findAllSentTransactionsByUser(Long userId) {
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("findAllSentBySenderId",DbTransaction.class);
			List<DbTransaction> tx = query.setParameter("senderId", userId).getResultList();
			return tx;
		} catch(NoResultException e) {
			return null;
		}
	}

	/**
	 *
	 * @param userId
	 * @return
	 */
	public List<DbTransaction> findAllRequestTransactionsByUser(Long userId) {
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("findAllRequestByReceiverId",DbTransaction.class);
			List<DbTransaction> tx = query.setParameter("receiverId", userId).getResultList();
			return tx;
		} catch(NoResultException e) {
			return null;
		}
	}



}
