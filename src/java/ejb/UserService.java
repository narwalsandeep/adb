/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.DbGroup;
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
	
	public synchronized DbUser findOneById(Long id){
		
		try{
			TypedQuery<DbUser> query = em.createNamedQuery("findOneById",DbUser.class);
			DbUser user = query.setParameter("id", id).getSingleResult();
			return user;
		} catch(NoResultException e) {
			return null;
		}
		
	}

	public synchronized int updateAlert(Long id){
		
		try{
			
			Integer lastAlerts = findOneById(id).getAlerts();
			Integer currentAlerts = (Integer)lastAlerts + 1;
			
			TypedQuery<DbUser> query = em.createNamedQuery("updateAlerts",DbUser.class);
			query.setParameter("id", id);
			query.setParameter("alerts", currentAlerts);
			int tx = query.executeUpdate();			
			return tx;
			
		} catch(NoResultException e) {
		}
		return 0;

	}

	public synchronized void resetAlerts(Long id) {

		try{
			
			Integer currentAlerts = 0;
			
			TypedQuery<DbUser> query = em.createNamedQuery("updateAlerts",DbUser.class);
			query.setParameter("id", id);
			query.setParameter("alerts", currentAlerts);
			query.executeUpdate();			
			
		} catch(NoResultException e) {

		}
	}

	public String getGroupByUserId(String email) {
		try{
			
			Integer currentAlerts = 0;
			
			TypedQuery<DbGroup> query = em.createNamedQuery("findUser",DbGroup.class);
			DbGroup user = query.setParameter("email", email).getSingleResult();
			return user.getUtype();
			
		} catch(NoResultException e) {

		}
		return null;
		
		
	}
}
