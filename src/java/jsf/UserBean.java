/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package jsf;

import ejb.UserService;
import entity.DbUser;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
*
* @author "as2d3f"
*/
@Named
@RequestScoped
public class UserBean {

	@EJB
    private UserService userService;
    
	@Inject
	LoginBean loginBean;
	
    private String email;
    private String name;
	private String amount;
		
	/**
	 *
	 * @return
	 */
	public List<DbUser> findAll(){
		return userService.findAll();
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public DbUser findOneById(Long id){
		return userService.findOneById(id);
	}

	/**
	 *
	 * @return
	 */
	public Integer getAlerts(){
		return userService.findOneById(loginBean.getLoggedInUser().getId()).getAlerts();
	}
	
	/**
	 *
	 */
	public void resetAlerts(){
		userService.resetAlerts(loginBean.getLoggedInUser().getId());
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
	public String getAmount() {
		return amount;
	}

	/**
	 *
	 * @param amount
	 */
	public void setAmount(String amount) {
		this.amount = amount;
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

}