/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import ejb.UserService;
import entity.DbUser;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String email;
    private String passwd;
	
	DbUser dbUser;
	
	@EJB
	UserService userService;
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
			this.dbUser = userService.findOneByEmail(this.email);
			
			return "dashboard";
		
		} catch (ServletException e) {
			if(this.isLoggedIn()){
				return "/error";
			}
			else{
				context.addMessage("loginForm:authError", 
					new FacesMessage("Authentication Failed. Consider Registration."));
				return "/login";
			}
		}
    }

	/**
	 * 
	 * @throws java.io.IOException 
	 */
	public void logout() throws IOException {
		this.dbUser = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
			ExternalContext xContext = context.getExternalContext();
			xContext.invalidateSession();
			xContext.redirect(xContext.getRequestContextPath() + "/faces/login.xhtml");
			//return "/login";
            //context.addMessage(null, new FacesMessage("User is logged out"));
        } catch (ServletException e) {
			//return "/error-exception";
			//context.addMessage(null, new FacesMessage("Logout failed."));
        }
    }

	/**
	 *
	 * @return
	 */
	public boolean isLoggedIn(){
		if(this.dbUser == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 *
	 * @return
	 */
	public DbUser getLoggedInUser(){
		return this.dbUser;
	}

}
