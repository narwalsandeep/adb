/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.DbTransaction;
import entity.DbUser;
import java.util.List;
import javax.annotation.security.RolesAllowed;
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
@RolesAllowed({"users","admin"})
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
	@RolesAllowed({"users","admin"})
	public synchronized List<DbTransaction> findAll() {
		
		// find all users 
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
	@RolesAllowed({"users"})
	public void doTransaction(Long senderId, Long receiverId,  Double amount, String txType) {

		DbTransaction tx;
		
		// we can have only two type of request, SEND or REQUEST
		// this is comming for both, I just swap the receiver and sender
		// and rest is taken care by the STATUS.
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

	/**
	 *
	 * @param userId
	 * @return
	 */
	@RolesAllowed({"users"})
	public List<DbTransaction> findAllSentByUserId(Long userId) {
		
		// find all SENT payment by users give the User Id
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("findAllSentByUserId",DbTransaction.class);
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
	@RolesAllowed({"users"})
	public List<DbTransaction> findAllRequestByUserId(Long userId) {

		// find all RESQUEST payment request by user, give the user ID
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("findAllRequestByUserId",DbTransaction.class);
			List<DbTransaction> tx = query.setParameter("receiverId", userId).getResultList();
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
	@RolesAllowed({"users"})
	public List<DbTransaction> findAllPendingRequestByUserId(Long userId) {
		
		// find all pending request by the user
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("findAllPendingRequestByUserId",DbTransaction.class);
			List<DbTransaction> tx = query.setParameter("senderId", userId).getResultList();
			return tx;
		} catch(NoResultException e) {
			return null;
		}
	}

	/**
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	@RolesAllowed({"users"})
	public int deductTotalAmount(Long userId, Double amount) {

		//deduct the total amount, 
		// this is used when you send the money or approve the request by other user.
		try{
			
			Double lastAmount = userService.findOneById(userId).getAmount();
			Double currentAmount = (double)lastAmount - (double)amount;
			
			TypedQuery<DbUser> query = em.createNamedQuery("updateAmount",DbUser.class);
			
			// define two parameter for update sql
			query.setParameter("id", userId);
			query.setParameter("amount", currentAmount);
			int tx = query.executeUpdate();
			
			return tx;
		} catch(NoResultException e) {
			//return 0;
			//System.out.println(e.getMessage());
		}
		return 0;
		
	}

	/**
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	@RolesAllowed({"users"})
	public int addTotalAmount(Long userId, Double amount) {
		
		// add total amount
		// used when you send or approve, the receiver amount is added here.
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

	/**
	 *
	 * @param tx
	 */
	@RolesAllowed({"users"})
	public void approvePayment(DbTransaction tx) {
		
		// approve the payment, simple status change is happening here.
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

	/**
	 *
	 * @param id
	 */
	@RolesAllowed({"users"})
	public void rejectPayment(Long id) {
		
		// reject the payment, simple reject of payment is happing here
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("rejectPayment",DbTransaction.class);
			query.setParameter("id", id);
			query.executeUpdate();			
		} catch(NoResultException e) {
		}
	}

	
	private String getCurrentDate() {
		
		// get the current date from webservice
		wsclient.Date port = service.getDatePort();
		return port.getCurrentDate();
	}

	/**
	 *
	 * @return
	 */
	@RolesAllowed({"admin"})
	public List<DbTransaction> findAllTransactions() {
		
		// find all transaction irrespective of user and the status.
		try{
			TypedQuery<DbTransaction> query = em.createNamedQuery("findAllTransactions",DbTransaction.class);
			List<DbTransaction> tx = query.getResultList();
			return tx;
		} catch(NoResultException e) {
			return null;
		}
	}



}
