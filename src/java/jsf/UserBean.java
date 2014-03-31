/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import ejb.UserService;
import entity.DbUser;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author "as2d3f"
 */
@Named
@RequestScoped
public class UserBean {
	
	@EJB
    private UserService user;
    
    private String email;
    private String name;
	private String amount;

	public List<DbUser> findAll(){
		return user.findAll();
	}
	
	public List<DbUser> fineOneByEmail(){	
		return user.findOneByEmail();
	}
	
	public UserService getUser() {
		return user;
	}

	public void setUser(UserService user) {
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	
}
