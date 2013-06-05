package org.soulwing.cas.apps.atlassian;

import java.security.Principal;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;

import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.HibernateTemplate;

import com.atlassian.spring.container.ContainerManager;
import com.atlassian.user.EntityException;
import com.atlassian.user.Group;
import com.atlassian.user.GroupManager;
import com.atlassian.user.User;

/**
 * A subclass of <code>ConfluenceCasAuthenticator</code> that adds the
 * group-joining capability of <code>ConfluenceGroupJoiningAuthenticator</code>.
 * 
 * @author Thomas Natt
 * @author Carl Harris
 * @author atlassian.com
 */
public class ConfluenceCasGroupJoiningAuthenticator
    extends ConfluenceCasAuthenticator {

  private static final String CONFLUENCE_USERS_GROUP = "confluence-users";

  private static final long serialVersionUID = 1L;

  private transient GroupManager groupManager;

  
  /* (non-Javadoc)
   * @see org.soulwing.cas.apps.atlassian.ConfluenceCasAuthenticator#getUser(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  public Principal getUser(HttpServletRequest request,
      HttpServletResponse response) {
    Principal user = super.getUser(request, response);
    addConfluenceUsersMembership(user.getName());
    return user;
  }

  /*
   * Adds the user to confluence-users if she isn't already a member.
   * This code was copied from ConfluenceGroupJoiningAuthenticator
   */
  private void addConfluenceUsersMembership(final String username) {
    HibernateTemplate hibernateTemplate =
        new HibernateTemplate(getSessionFactory());
    hibernateTemplate.execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
        try {
          Transaction transaction = session.beginTransaction();
          User user = getUserAccessor().getUser(username);
          Group confluenceUsers =
              getGroupManager().getGroup(CONFLUENCE_USERS_GROUP);
          if (!getGroupManager().hasMembership(confluenceUsers, user)) {
            getGroupManager().addMembership(confluenceUsers, user);
          }
          transaction.commit();
        }
        catch (EntityException e) {
          logger.error("Failed to add " + username 
              + " to " + CONFLUENCE_USERS_GROUP + ": "
              + e.getMessage(), e);
        }
        return null; 
      }
    });

  }

  /**
   * Gets a reference to Confluence's <code>groupManager</code> bean.
   * This code was copied from ConfluenceGroupJoiningAuthenticator
   */
  protected GroupManager getGroupManager() {
    if (groupManager == null) {
      groupManager =
          (GroupManager) ContainerManager.getComponent("groupManager");
    }
    return groupManager;
  }

  /**
   * Gets a reference to Confluence's <code>sessionFactory</code> bean.
   * This code was copied from ConfluenceGroupJoiningAuthenticator
   */
  protected SessionFactory getSessionFactory() {
    return (SessionFactory) ContainerManager.getComponent("sessionFactory");
  }

}
