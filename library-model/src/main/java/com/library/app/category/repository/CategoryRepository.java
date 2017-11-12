package com.library.app.category.repository;

import javax.persistence.EntityManager;

import com.library.app.category.model.Category;

public class CategoryRepository {

    EntityManager em;

    public Category add(final Category category) {
		em.persist(category);
        return category;
    }

    public Category findById(final Long id) {
        if (id == null) {
            return null;
        }
        return em.find(Category.class, id);
    }

    public void update(final Category category) {
        em.merge(category);
    }
}