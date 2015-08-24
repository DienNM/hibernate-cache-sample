package com.dee.hibernate.cache.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author dien.nguyen
 **/

@Entity(name = "category_cache")
public class CategoryModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_relation", joinColumns = {@JoinColumn(name = "parent")}, 
        inverseJoinColumns = {@JoinColumn(name = "child")})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<CategoryModel> children = new ArrayList<CategoryModel>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryModel parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryModel> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryModel> children) {
        this.children = children;
    }

    public CategoryModel getParent() {
        return parent;
    }

    public void setParent(CategoryModel parent) {
        this.parent = parent;
    }
    
    
    
}
