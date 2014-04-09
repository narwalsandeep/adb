/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author "as2d3f"
 */
@WebService(serviceName = "Date")
public class Date {

	@WebMethod(operationName = "getCurrentDate")
	public String getCurrentDate() {
		
		return "d";
	}
}
