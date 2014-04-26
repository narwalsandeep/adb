/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import ejb.RegisterService;
import ejb.UserService;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author sandeepnarwal
 */
@Named
@RequestScoped
public class RegisterBean {

    @EJB
    RegisterService registerService;
    
	@EJB
	UserService userService;
	
    String email;
    String passwd;
    String name;
    String currency;

	/**
	 *
	 */
	public RegisterBean() { }

	/**
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public String register() throws NoSuchAlgorithmException, UnsupportedEncodingException {
       
		FacesContext context = FacesContext.getCurrentInstance();
        
		// validate the email, if it does not exists and register
		if(validateEmail(email)){
			registerService.doRegister(email, passwd, name, currency);
			return "success";
		}
		else{
			// else error
			context.addMessage("registerForm:registerError", 
				new FacesMessage("Username/Email already Exists."));
			return "register";
		}
    }

	/**
	 *
	 * @return
	 */
	public RegisterService getRegisterService() {
		return registerService;
	}

	/**
	 *
	 * @param registerService
	 */
	public void setRegisterService(RegisterService registerService) {
		this.registerService = registerService;
	}

	/**
	 *
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 *
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 *
	 * @return
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 *
	 * @param passwd
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 *
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	private boolean validateEmail(String email) {
		
		// if returned null, return true else false
		if(userService.findOneByEmail(email) == null){
			return true;
		}
		return false;
	}
	    
}
