/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import ejb.TransactionService;
import ejb.UserService;
import entity.DbTransaction;
import entity.DbUser;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author sandeepnarwal
 */
@Named
@RequestScoped
public class TransactionBean  implements Serializable {
	
    @EJB
    private TransactionService tx;
    
	@EJB
	private UserService userService;
	
	@Inject
	private LoginBean loginBean;
	
    private String senderEmail;
	private String receiverEmail;
    private Double amount;
	
	private final String TYPE_SEND = "SEND";
	private final String TYPE_REQUEST = "REQUEST";

	/**
	 *
	 * @return
	 */
	public String doSend(){
		
		FacesContext context = FacesContext.getCurrentInstance();
        Long senderId = loginBean.getLoggedInUser().getId();
		DbUser receiver = userService.findOneByEmail(receiverEmail);
		if(receiver != null){
			tx.doTransaction(senderId,receiver.getId(),amount,TYPE_SEND);
			context.addMessage("txSendForm:sendSuccess", new FacesMessage(amount+ " GBP transferred successfully."));
		}
		else{
			context.addMessage("txSendForm:sendError", new FacesMessage("ERROR: Sender Email not found."));
		}
		return "/user/transaction/send";
	
	}

	/**
	 *
	 * @return
	 */
	public String doRequest(){
		FacesContext context = FacesContext.getCurrentInstance();
        Long receiverId = loginBean.getLoggedInUser().getId();
		DbUser sender = userService.findOneByEmail(senderEmail);
		if(sender != null){
			tx.doTransaction(sender.getId(),receiverId,amount,TYPE_REQUEST);
			context.addMessage("txRequestForm:requestSuccess", new FacesMessage(amount+ " request sent successfully."));
		}
		else{
			context.addMessage("txRequestForm:requestError", new FacesMessage("ERROR: Email from whom you are requesting not found."));
		}
		return "/user/transaction/request";
	}
	
	/**
	 *
	 * @return
	 */
	public List<DbTransaction> findAllSentTransactionsByUser(){
		
		Long userId = loginBean.getLoggedInUser().getId();
		return tx.findAllSentTransactionsByUser(userId);
		
	}

	/**
	 *
	 * @return
	 */
	public List<DbTransaction> findAllRequestTransactionsByUser(){
		
		Long userId = loginBean.getLoggedInUser().getId();
		return tx.findAllRequestTransactionsByUser(userId);
		
	}

	
	private void FacesMessage(String authentication_Failed_Consider_Registration) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
