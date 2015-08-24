package com.dee.hibernate.cache;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.Assert;

import com.dee.hibernate.cache.model.CategoryModel;

/**
 * @author dien.nguyen
 **/

public class FirstLevelCacheTest extends TestCase{
    
    CategoryModel root;
    List<CategoryModel> categories = new ArrayList<CategoryModel>();
    
    @Override
    protected void setUp() throws Exception {
        root = new CategoryModel();
        root.setName("root");
        
        Session session = SessionUtil.getSession();
        session.getTransaction().begin();
        session.save(root);
        
        for(int i = 0; i < 10; i++) {
            CategoryModel category = new CategoryModel();
            category.setName("category" + i);
            session.save(category);
            root.getChildren().add(category);
            category.setParent(root);
            categories.add(category);
        }
        
        session.getTransaction().commit();
        session.close();
    }
    
    @Override
    protected void tearDown() throws Exception {
        Session session = SessionUtil.getSession();
        session.getTransaction().begin();
        session.delete(root);
        for(CategoryModel ctg : categories) {
            session.delete(ctg);
        }
        session.getTransaction().commit();
        session.close();
    }
    
    public void testFirstLevelCache_case1() {
        Session session = SessionUtil.getSession();
        
        CategoryModel pRoot;
        try {
            pRoot = (CategoryModel) session.get(CategoryModel.class, root.getId());
        } finally {
            session.close();
        }
        
        session = SessionUtil.getSession();
        try {
            CategoryModel pRoot1 = (CategoryModel) session.get(CategoryModel.class, root.getId());
            CategoryModel pRoot2 = (CategoryModel) session.get(CategoryModel.class, root.getId());
            
            // Apply First Level Cache: Transaction Scope
            Assert.assertTrue(pRoot1 == pRoot2);
            
            // Hibernate returned different Java instances in different sessions
            Assert.assertFalse(pRoot == pRoot2);
            Assert.assertFalse(pRoot == pRoot1);
        } finally {
            session.close();
        }
    }
    
}
