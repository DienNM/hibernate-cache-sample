package com.dee.hibernate.cache;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;

import com.dee.hibernate.cache.model.CategoryReadOnlyModel;
import com.dee.hibernate.cache.model.CategoryReadWriteModel;

/**
 * @author dien.nguyen
 **/

public class SecondLevelCacheTest extends TestCase {
    
    public void testUpdateReadOnly_case01() {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();

        CategoryReadOnlyModel category01 = new CategoryReadOnlyModel();
        category01.setName("Category 01");
        session.persist(category01);
        tx.commit();
        session.close();

        session = SessionUtil.getSession();
        try {
            tx = session.beginTransaction();
            CategoryReadOnlyModel category02 = (CategoryReadOnlyModel) session.byId(CategoryReadOnlyModel.class).load(category01.getId());
            category02.setName("Update Name of Category 01");
            session.flush();
            tx.commit();
            fail("Cannot update: This is a readonly object");
        } catch (UnsupportedOperationException e) {
            tx.rollback();
            Assert.assertEquals("Can't write to a readonly object", e.getMessage());
        } finally {
            session.close();
        }
    }
    
    public void testUpdateReadWrite() {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();

        CategoryReadWriteModel category01 = new CategoryReadWriteModel();
        category01.setName("Category 01");
        session.persist(category01);
        tx.commit();
        session.close();

        session = SessionUtil.getSession();
        try {
            tx = session.beginTransaction();
            CategoryReadWriteModel category02 = (CategoryReadWriteModel) session.byId(CategoryReadWriteModel.class).load(category01.getId());
            category02.setName("Update Name of Category 01");
            session.flush();
            tx.commit();
        } catch (UnsupportedOperationException e) {
            tx.rollback();
            fail("Error, should be updated successfully");
        } finally {
            session.close();
        }
    }
}
