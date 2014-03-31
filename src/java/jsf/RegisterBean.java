/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import ejb.RegisterService;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class RegisterBean {

    @EJB
    RegisterService register;
    
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
        register.doRegister(email, passwd, name, currency);
        return "success";
    }

	/**
	 *
	 * @return
	 */
	public RegisterService getRegister() {
		return register;
	}

	/**
	 *
	 * @param register
	 */
	public void setRegister(RegisterService register) {
		this.register = register;
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
    
}
