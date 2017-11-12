package com.library.app.category.services.impl;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CategoryServicesUTest {
    private CategoryServices categoryServices;
    private CategoryRepository categoryRepository;
    private Validator validator;


    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        categoryRepository = mock(CategoryRepository.class);
        categoryServices = new CategoryServicesImpl();
        ((CategoryServicesImpl) categoryServices).validator = validator;
        ((CategoryServicesImpl) categoryServices).categoryRepository = categoryRepository;
    }

    @Test
    public void addCategoryWithNullName() {
        addCategoryWithInvalidName(null);
    }

    @Test
    public void addCategoryWithShortName() {
        addCategoryWithInvalidName("A");
    }

    @Test
    public void addCategoryWithLongName() {
        addCategoryWithInvalidName("This is a long name that will cause an exception to be thrown");
    }

    @Test(expected = CategoryExistentException.class)
    public void addCategoryWithExistentName() {
        when(categoryRepository.alreadyExists(java())).thenReturn(true);

        categoryServices.add(java());
    }

    @Test
    public void addValidCategory() {
        when(categoryRepository.alreadyExists(java())).thenReturn(false);
        when(categoryRepository.add(java())).thenReturn(categoryWithId(java(), 1L));

        final Category categoryAdded = categoryServices.add(java());
        assertThat(categoryAdded.getId(), is(equalTo(1L)));
    }

    @Test
    public void updateWithNullName() {
        updateCategoryWithInvalidName(null);
    }

    @Test
    public void updateCategoryWithShortName() {
        updateCategoryWithInvalidName("A");
    }

    @Test
    public void updateCategoryWithLongName() {
        updateCategoryWithInvalidName("This is a long name that will cause an exception to be thrown");
    }

    @Test(expected = CategoryExistentException.class)
    public void updateCategoryWithExistentName() {
        when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(true);

        categoryServices.update(categoryWithId(java(), 1L));
    }

    @Test(expected = CategoryNotFoundException.class)
    public void updateCategoryNotFound() {
        when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
        when(categoryRepository.existsById(1L)).thenReturn(false);

        categoryServices.update(categoryWithId(java(), 1L));
    }

    private void addCategoryWithInvalidName(final String name) {
        try {
            categoryServices.add(new Category(name));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

    private void updateCategoryWithInvalidName(final String name) {
        try {
            categoryServices.update(new Category(name));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }
}
