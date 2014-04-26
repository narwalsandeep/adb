/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

<<<<<<< HEAD
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
=======
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.Singleton;
import javax.jws.WebService;
import javax.jws.WebMethod;
>>>>>>> b2

/**
 *
 * @author "as2d3f"
 */
<<<<<<< HEAD
@WebService(serviceName = "Date")
public class Date {

	/**
	 *
	 * @return
=======
@Singleton
@WebService(serviceName = "Date")
public class Date {

	private final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	/**
	 * Web service operation
	 * @return 
>>>>>>> b2
	 */
	@WebMethod(operationName = "getCurrentDate")
	public String getCurrentDate() {
		
<<<<<<< HEAD
		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
		return date;
=======
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
		return timeStamp;
>>>>>>> b2
	}
}
