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

/**
 *
 * @author sandeepnarwal
 */
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
       
		// FAces context, used to send the message.
		FacesContext context = FacesContext.getCurrentInstance();
        
		// request object used for login
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
		try {	
			
			// login now, 
			request.login(this.email, this.passwd);
			
			// get the logged in user
			dbUser = userService.findOneByEmail(this.email);
			
			// get his user type and redirect to required pages 
			// admin or dashboard 
			String utype = userService.getGroupByUserId(dbUser.getEmail());
			if(utype.equals("admin")){
				return "admin_home";							
			}
			else{
				return "dashboard";							
			}
		
		} catch (ServletException e) {
			// else some error
			if(this.isLoggedIn()){
				return "error";
			}
			else{
				context.addMessage("loginForm:authError", 
					new FacesMessage("Authentication Failed. Consider Registration."));
				return "login";
			}
		}
    }

	/**
	 * 
	 * @throws java.io.IOException 
	 */
	public void logout() throws IOException {
		
		// on logout null the dbuser we used to add, as its scope is in session
		this.dbUser = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
			// also logout from request object
            request.logout();
			ExternalContext xContext = context.getExternalContext();
			xContext.invalidateSession();
			
			// redirect  back to login page
			xContext.redirect(xContext.getRequestContextPath() + "/faces/login.xhtml");
        } catch (ServletException e) {
        }
    }

	/**
	 *
	 * @return
	 */
	public boolean isLoggedIn(){
		
		// to check elsewhere if user is logged in
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
