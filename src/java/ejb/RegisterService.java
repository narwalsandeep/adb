/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.DbGroup;
import entity.DbUser;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.ws.WebServiceRef;
import wsclient.Date_Service;

/**
 *
 * @author sandeepnarwal
 */
@Stateless
public class RegisterService {
	@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/A/Date.wsdl")
	private Date_Service service;
	
    @PersistenceContext
    EntityManager em;

	Double initialAmount = 1000.0;

	/**
	 *
	 */
	public RegisterService() { }

	/**
	 *
	 * @param email
	 * @param passwd
	 * @param name
	 * @param currency
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public void doRegister(String email, String passwd, String name, String currency) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		DbUser user;
		DbGroup group;
		
		String newPwd = _hash(passwd);
		user = new DbUser(email, newPwd, name, currency, initialAmount,getCurrentDate());
		String utype = "users";
		group = new DbGroup(email, utype);
		
		em.persist(user);
		em.persist(group);
    }
	
	/**
	 *
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public String _hash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{

		// hash a str
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(str.getBytes("UTF-8"));
		byte[] digest = md.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		return bigInt.toString(16);
		
	}

	private String getCurrentDate() {
		wsclient.Date port = service.getDatePort();
		return port.getCurrentDate();
	}
}
