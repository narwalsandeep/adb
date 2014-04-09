/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.Singleton;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author "as2d3f"
 */
@Singleton
@WebService(serviceName = "Date")
public class Date {

	private final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	/**
	 * Web service operation
	 * @return 
	 */
	@WebMethod(operationName = "getCurrentDate")
	public String getCurrentDate() {
		
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
		return timeStamp;
	}
}
