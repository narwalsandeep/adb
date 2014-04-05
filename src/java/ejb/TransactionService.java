/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.DbGroup;
import entity.DbTransaction;
import entity.DbUser;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author "as2d3f"
 */
@Stateless
public class TransactionService {

	@PersistenceContext
    EntityManager em;

	UserService user;
	
	public final String STATUS_SUCCESS = "SUCCESS";
	public final String STATUS_WAITING_APPROVAL = "WAITING APPROVAL";
	public final String STATUS_REQUEST_REJECTED = "REQUEST REJECTED";
	
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
			tx = new DbTransaction(senderId, receiverId, amount, STATUS_SUCCESS);
		}
			
		em.persist(tx);

	}



}
