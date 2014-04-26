/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import entity.DbGroup;
import entity.DbUser;
import java.util.List;
import javax.annotation.security.RolesAllowed;
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
	@RolesAllowed({"admin"})
	public synchronized List<DbUser> findAll() {
		
		// get the list of all users,
		// NOTE this also get admin.
        List<DbUser> user = em.createNamedQuery("findAll").getResultList();
        return user;
    }

	/**
	 *
	 * @param email
	 * @return
	 */
	public synchronized DbUser findOneByEmail(String email) {
		
		// find a user provide his email
		try{
			TypedQuery<DbUser> query = em.createNamedQuery("findOneByEmail",DbUser.class);
			DbUser user = query.setParameter("email", email).getSingleResult();
			return user;
		} catch(NoResultException e) {
			return null;
		}
	
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	public synchronized DbUser findOneById(Long id){
		
		// find a user provided his ID
		try{
			TypedQuery<DbUser> query = em.createNamedQuery("findOneById",DbUser.class);
			DbUser user = query.setParameter("id", id).getSingleResult();
			return user;
		} catch(NoResultException e) {
			return null;
		}
		
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@RolesAllowed({"users"})
	public synchronized int updateAlert(Long id){
		
		// update alert/
		// this is used when someone request a payment
		// the number of alerts are incremented
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

	/**
	 *
	 * @param id
	 */
	@RolesAllowed({"users"})
	public synchronized void resetAlerts(Long id) {

		// when user click on alert in dashboard, reset it.
		// means he has seen the alerts
		try{
			
			Integer currentAlerts = 0;
			
			TypedQuery<DbUser> query = em.createNamedQuery("updateAlerts",DbUser.class);
			query.setParameter("id", id);
			query.setParameter("alerts", currentAlerts);
			query.executeUpdate();			
			
		} catch(NoResultException e) {

		}
	}

	/**
	 *
	 * @param email
	 * @return
	 */
	public String getGroupByUserId(String email) {
		
		// the the group name of the user, given the user iD
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
