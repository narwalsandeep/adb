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

		// get the pending money in account
		Double pendingMoney = userService.findOneById(senderId).getAmount();
		
		// if pending is lesss the sending money
		if(pendingMoney < amount){
			context.addMessage("txSendForm:sendError", new FacesMessage("ERROR: Not enough money to send. Only "+ pendingMoney+" left."));
			return "/user/transaction/send";
		}
		
		// check if receiver exists
		DbUser receiver = userService.findOneByEmail(receiverEmail);
		if(receiver != null){
			
			// now do the transaction
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
		
		// request is sent only if user exists
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

	/**
	 *
	 * @return
	 */
	public List<DbTransaction> findAllPendingRequestsByUser(){
	
		Long userId = loginBean.getLoggedInUser().getId();
		return txService.findAllPendingRequestByUserId(userId);
	
	}
	
	/**
	 *
	 * @return
	 */
	public List<DbTransaction> findAllTransactions(){

		return txService.findAllTransactions();
		
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	public Double getCurrentAmount(Long id){
		return getUserService().findOneById(id).getAmount();
	}

	/**
	 *
	 * @param transactionRecord
	 */
	public void approvePayment(DbTransaction transactionRecord){
	
		FacesContext context = FacesContext.getCurrentInstance();

		// to approve payment, balance much be greater
		Double pendingMoney = userService.findOneById(transactionRecord.getSenderId()).getAmount();
		
		// check if enough balance else error
		if(pendingMoney < transactionRecord.getAmount()){
			context.addMessage("error", new FacesMessage("ERROR: Not enough money to send. Only "+ pendingMoney+" left."));
		}
		else{
			txService.approvePayment(transactionRecord);
		}
	}
	
	/**
	 *
	 * @return
	 */
	public TransactionService getTxService() {
		return txService;
	}

	/**
	 *
	 * @param txService
	 */
	public void setTxService(TransactionService txService) {
		this.txService = txService;
	}

	/**
	 *
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 *
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 *
	 * @return
	 */
	public LoginBean getLoginBean() {
		return loginBean;
	}

	/**
	 *
	 * @param loginBean
	 */
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	/**
	 *
	 * @return
	 */
	public String getSenderEmail() {
		return senderEmail;
	}

	/**
	 *
	 * @param senderEmail
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	/**
	 *
	 * @return
	 */
	public String getReceiverEmail() {
		return receiverEmail;
	}

	/**
	 *
	 * @param receiverEmail
	 */
	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
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

}