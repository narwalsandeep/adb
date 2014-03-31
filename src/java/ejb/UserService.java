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
import javax.persistence.PersistenceContext;

/**
 *
 * @author "as2d3f"
 */
@Stateless
public class UserService {

	@PersistenceContext
    EntityManager em;

	public synchronized List<DbUser> findAll() {
        List<DbUser> user = em.createNamedQuery("findAll").getResultList();
        return user;
    }

	public synchronized List<DbUser> findOneByEmail() {
        List<DbUser> user = em.createNamedQuery("findOneByEmail").getResultList();
        return user;
    }
	
}
