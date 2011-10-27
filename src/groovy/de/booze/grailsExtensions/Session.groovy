/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.booze.grailsExtensions;

import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateAccessor
import org.apache.log4j.Logger;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.hibernate.HibernateException;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.hibernate.*;
import org.hibernate.cfg.*

/**
 * pretty much a clone of what the hibernate session in view filter does
 * User: ab
 * Date: Mar 8, 2009
 * Time: 9:37:19 AM
 */
public class Session extends HibernateAccessor {
  private final Logger log = Logger.getLogger(Session.class);

  public void startSession() {
    def sessionFactory = new Configuration().configure().buildSessionFactory()
    org.hibernate.classic.Session session = sessionFactory.openSession();
    setFlushMode(FLUSH_NEVER);

    //Bind the Session to the current thread/transaction
    TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    //Activate transaction synchronization for the current thread.
    TransactionSynchronizationManager.initSynchronization();
  }

  public void endSession() throws HibernateException {
    SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
    log.debug("Flushing single Hibernate Session");

    try {
      flushIfNecessary(sessionHolder.getSession(), false);
    }
    catch (HibernateException ex) {
      throw convertHibernateAccessException(ex);
    }

    sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
    log.debug("Closing single Hibernate Session");
    SessionFactoryUtils.releaseSession(sessionHolder.getSession(), sessionFactory);
  }
}