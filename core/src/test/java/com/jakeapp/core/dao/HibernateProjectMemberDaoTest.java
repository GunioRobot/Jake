package com.jakeapp.core.dao;

import static org.junit.Assert.*;

import java.io.File;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jakeapp.core.dao.exceptions.NoSuchProjectException;
import com.jakeapp.core.dao.exceptions.NoSuchProjectMemberException;
import com.jakeapp.core.domain.Project;
import com.jakeapp.core.domain.ProjectMember;
import com.jakeapp.core.domain.XMPPUserId;

/**
 * Unit test for the HibernateProjectMemberDao
 * @author Simon
 *
 */
public class HibernateProjectMemberDaoTest {
	private static final Logger log = Logger.getLogger(HibernateProjectMemberDaoTest.class);
    private static IProjectMemberDao projectMemberDao;
    private static final String contextXML = "jake_core_test_context.xml";
    
    private static final Project project = new Project("project", new UUID(2, 3), null, new File("test"));
    private static final ProjectMember member1 = new ProjectMember(new XMPPUserId(new UUID(1, 1), "foo", "bar", "", ""), null);
    private static final ProjectMember member2 = new ProjectMember(new XMPPUserId(new UUID(2, 2), "foo2", "bar2", "", ""), null);


    @BeforeClass
    public static void setupClass() {
    	try {
			log.debug("starting setupClass");
			ApplicationContext context = new ClassPathXmlApplicationContext(contextXML);
			projectMemberDao = (IProjectMemberDao) context.getBean("projectMemberDao");
			log.debug("setup done");
		} catch (Exception e) {
			log.debug("exception cought!");
			e.printStackTrace();
			fail();
		}
    }

    @Test
    public void persist_persist()
    {
    	try {
			log.info("Test: persist_persist...");
			projectMemberDao.persist(project, member1);
		} catch (RuntimeException e) {
			log.debug("failed to persist entity...");
			e.printStackTrace();
			fail();
		}
    }
    
    @Test
    public void persist_persistRead() {
    	log.info("Test: persist_persistRead...");
    	projectMemberDao.persist(project, member1);
    	try {
    		assertNotNull(projectMemberDao.getAll(project));
			assertTrue(projectMemberDao.getAll(project).contains(member1));
		} catch (NoSuchProjectException e) {
			log.debug("exception caught");		
			e.printStackTrace();
			fail();
		}
    }
    
    @Test
    public void makeTransient_persistReadMakeTransientRead() {
    	log.info("Test: makeTransient_persistReadMakeTransientRead...");
    	projectMemberDao.persist(project, member1);
    	try {
    		assertNotNull(projectMemberDao.getAll(project));
			assertTrue(projectMemberDao.getAll(project).contains(member1));
			
			projectMemberDao.makeTransient(project, member1);
			
    		assertNotNull(projectMemberDao.getAll(project));
			assertFalse(projectMemberDao.getAll(project).contains(member1));
			
		} catch (Exception e) {
			log.debug("exception caught");		
			e.printStackTrace();
			fail();
		}
    }
    
    @Test(expected=NoSuchProjectMemberException.class)
    public void makeTransient_throwNoSuchProjectException() throws NoSuchProjectMemberException{
    	log.info("Test: makeTransient_throwNoSuchProjectException...");
    	projectMemberDao.makeTransient(project, member2);
    }
}