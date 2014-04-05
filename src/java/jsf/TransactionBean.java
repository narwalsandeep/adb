/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import ejb.TransactionService;
import ejb.UserService;
import entity.DbUser;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class TransactionBean {
	
    @EJB
    private TransactionService tx;
    
	@EJB
	private UserService userService;
	
	@Inject
	private LoginBean loginBean;
	
    private String senderEmail;
	private String receiverEmail;
    private Double amount;
	private String txType;
	
	private final String TYPE_SEND = "SEND";
	private final String TYPE_REQUEST = "REQUEST";

	public String doSend(){
		
		FacesContext context = FacesContext.getCurrentInstance();
        Long senderId = loginBean.getLoggedInUser().getId();
		DbUser receiver = userService.findOneByEmail(receiverEmail);
		if(receiver != null){
			tx.doTransaction(senderId,receiver.getId(),amount,TYPE_SEND);
			context.addMessage("txSendForm:sendSuccess", new FacesMessage(amount+ " GBP transferred successfully."));
		}
		else{
			context.addMessage("txSendForm:sendError", new FacesMessage("Sender Email not found."));
		}
		return "/user/transaction/send";
	
	}

	public String doRequest(){
		//tx.doTransaction(senderEmail,receiverEmail,amount,TYPE_REQUEST);
		return "/transaction/success";
	}

	public TransactionService getTx() {
		return tx;
	}

	public void setTx(TransactionService tx) {
		this.tx = tx;
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

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}
	
	private void FacesMessage(String authentication_Failed_Consider_Registration) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
