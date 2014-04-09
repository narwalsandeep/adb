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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.xml.ws.WebServiceRef;
import wsclient.Date_Service;

/**
 *
 * @author "as2d3f"
 */
@Stateless
public class TransactionService {
	
	@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/A/Date.wsdl")
	private Date_Service service;

	@PersistenceContext
    EntityManager em;

	@EJB
	UserService userService;
	
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
			tx = new DbTransaction(senderId, receiverId, amount, STATUS_SUCCESS,getCurrentDate());
		// else its a  REQUEST
		} else {
			userService.updateAlert(senderId);
			tx = new DbTransaction(senderId, receiverId, amount, STATUS_REQUEST_AWAITING_APPROVAL,getCurrentDate());
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

	public int deductTotalAmount(Long userId, Double amount) {

		try{
			
			Double lastAmount = userService.findOneById(userId).getAmount();
			Double currentAmount = (double)lastAmount - (double)amount;
			
			TypedQuery<DbUser> query = em.createNamedQuery("updateAmount",DbUser.class);
			query.setParameter("id", userId);
			query.setParameter("amount", currentAmount);
			int tx = query.executeUpdate();
			
			return tx;
		} catch(NoResultException e) {
			//return 0;
			System.out.println(e.getMessage());
		}
		return 0;
		
	}

	public int addTotalAmount(Long userId, Double amount) {
		try{
			
			Double lastAmount = userService.findOneById(userId).getAmount();
			Double currentAmount = (double)lastAmount + (double)amount;
			
			TypedQuery<DbUser> query = em.createNamedQuery("updateAmount",DbUser.class);
			query.setParameter("id", userId);
			query.setParameter("amount", currentAmount);
			int tx = query.executeUpdate();
			
			return tx;
		} catch(NoResultException e) {
			//return 0;
			System.out.println(e.getMessage());
		}
		return 0;
		
	}

	public void approvePayment(DbTransaction tx) {
		
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("approvePayment",DbTransaction.class);
			query.setParameter("id", tx.getId());
			query.executeUpdate();			
			
			// reset Total Balance for both Users because we are approving
			deductTotalAmount(tx.getSenderId(),tx.getAmount());
			addTotalAmount(tx.getReceiverId(),tx.getAmount());

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

	
	private String getCurrentDate() {
		wsclient.Date port = service.getDatePort();
		return port.getCurrentDate();
	}



}
