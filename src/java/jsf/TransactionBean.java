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
public class TransactionBean implements Serializable {

    @EJB
    private TransactionService txService;
    
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
		
		Double pendingMoney = userService.findOneById(senderId).getAmount();
		
		if(pendingMoney < amount){
			context.addMessage("txSendForm:sendError", new FacesMessage("ERROR: Not enough money to send. Only "+ pendingMoney+" left."));
			return "/user/transaction/send";
		}
		
		DbUser receiver = userService.findOneByEmail(receiverEmail);
		if(receiver != null){
			
			txService.doTransaction(senderId,receiver.getId(),amount,TYPE_SEND);
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
			txService.doTransaction(sender.getId(),receiverId,amount,TYPE_REQUEST);
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
		return txService.findAllSentByUserId(userId);

	}

	/**
	*
	* @return
	*/
	public List<DbTransaction> findAllRequestTransactionsByUser(){

		Long userId = loginBean.getLoggedInUser().getId();
		return txService.findAllRequestByUserId(userId);

	}

	public List<DbTransaction> findAllPendingRequestsByUser(){
	
		Long userId = loginBean.getLoggedInUser().getId();
		return txService.findAllPendingRequestByUserId(userId);
	
	}
	
	public List<DbTransaction> findAllTransactions(){

		return txService.findAllTransactions();
		
	}
	
	public Double getCurrentAmount(Long id){
		return getUserService().findOneById(id).getAmount();
	}

	public void approvePayment(DbTransaction transactionRecord){
	
		FacesContext context = FacesContext.getCurrentInstance();

		Double pendingMoney = userService.findOneById(transactionRecord.getSenderId()).getAmount();
		
		if(pendingMoney < transactionRecord.getAmount()){
			context.addMessage("error", new FacesMessage("ERROR: Not enough money to send. Only "+ pendingMoney+" left."));
		}
		else{
			txService.approvePayment(transactionRecord);
		}
	}
	
	public TransactionService getTxService() {
		return txService;
	}

	public void setTxService(TransactionService txService) {
		this.txService = txService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}