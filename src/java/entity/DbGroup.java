/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sandeepnarwal
 */
@Entity
public class DbGroup implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
	private String utype;

    @NotNull
	private String email;

	/**
	 *
	 */
	public DbGroup() { }

	/**
	 *
	 * @param email
	 * @param utype
	 * @param type
	 */
	public DbGroup(String email, String utype) {
		this.email = email;
		this.utype = utype;
	}

	/**
	 *
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
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
	public String getUtype() {
		return utype;
	}

	/**
	 *
	 * @param utype
	 */
	public void setUtype(String utype) {
		this.utype = utype;
	}
	
	

}
