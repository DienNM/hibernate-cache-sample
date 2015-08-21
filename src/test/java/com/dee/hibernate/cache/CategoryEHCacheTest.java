package com.dee.hibernate.cache;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.Assert;

import com.dee.hibernate.cache.model.CategoryModel;
import com.dee.hibernate.cache.model.ItemModel;

/**
 * @author dien.nguyen
 **/

public class CategoryEHCacheTest extends TestCase{
    
    public void testUsingEHCache() {
        CategoryModel root = new CategoryModel();
        root.setName("root");
        
        Session session = SessionUtil.getSession();
        session.getTransaction().begin();
        session.save(root);
        
        for(int i = 0; i < 500; i++) {
            CategoryModel category = new CategoryModel();
            category.setName("category" + i);
            session.save(category);
            root.getChildren().add(category);
            category.setParent(root);
            
            for(int j = 0; j < 100; j++) {
                ItemModel item = new ItemModel();
                item.setName("item" + j);
                item.setCategory(category);
            }
        }
        session.getTransaction().commit();
        session.close();
        
        session = SessionUtil.getSession();
        
        CategoryModel pRoot = (CategoryModel) session.get(CategoryModel.class, root.getId());
        Assert.assertNotNull(pRoot);
        Assert.assertTrue(!pRoot.getChildren().isEmpty());
        Assert.assertNotNull(pRoot.getChildren().get(0).getName());
        Assert.assertEquals("root", pRoot.getChildren().get(0).getParent().getName());
        
        session.close();
    }
    
}
