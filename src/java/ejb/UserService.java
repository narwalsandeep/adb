/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.DbUser;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author "as2d3f"
 */
@Stateless
public class UserService{

	@PersistenceContext
    EntityManager em;

	public synchronized List<DbUser> findAll() {
        List<DbUser> user = em.createNamedQuery("findAll").getResultList();
        return user;
    }

	/**
	 *
	 * @param email
	 * @return
	 */
	public synchronized DbUser findOneByEmail(String email) {
		
		TypedQuery<DbUser> query = em.createNamedQuery("findOneByEmail",DbUser.class);
        DbUser user = query.setParameter("email", email).getSingleResult();
		
		return user;
	}
	
}
