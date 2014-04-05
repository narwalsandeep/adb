/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.DbUser;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

	/**
	 *
	 * @return
	 */
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
		
		try{
			TypedQuery<DbUser> query = em.createNamedQuery("findOneByEmail",DbUser.class);
			DbUser user = query.setParameter("email", email).getSingleResult();
			return user;
		} catch(NoResultException e) {
			return null;
		}
	
	}

	
}
