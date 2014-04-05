/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author "as2d3f"
 */
public class Utils {
	
	public Utils(){
		
	}
	
	public void addContextMessage(String to,String msg){
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(to, new FacesMessage(msg));
        
	}
	
	public FacesContext getContext(){
		return FacesContext.getCurrentInstance();
        
	}
	
	public Object getRequest(){
		
		return this.getContext().getExternalContext().getRequest();

	}
	
}
