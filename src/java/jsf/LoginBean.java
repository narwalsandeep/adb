/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class LoginBean implements Serializable {

    private String email;
    private String passwd;

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
	public String auth() {
       
		FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        System.out.println("Username: " + this.email);
        System.out.println("Password: " + this.passwd);
        try {	
			request.login(this.email, this.passwd);
			return "/user/home.xhtml";
		} catch (ServletException e) {
            context.addMessage("loginForm:authError", new FacesMessage("Authentication Failed. Consider Registration."));
			return "/login.xhtml";
        }
    }

	/**
	 *
	 */
	public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            //this method will disassociate the principal from the session (effectively logging him/her out)
            request.logout();
            //context.addMessage(null, new FacesMessage("User is logged out"));
        } catch (ServletException e) {
            //context.addMessage(null, new FacesMessage("Logout failed."));
        }
    }

	private void FacesMessage(String authentication_Failed_Consider_Registrati) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
