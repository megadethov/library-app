package com.library.app.category.services.impl;

import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class CategoryServicesUTest {
    private CategoryServices categoryServices;
    private Validator validator;


    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        categoryServices = new CategoryServicesImpl();
        ((CategoryServicesImpl) categoryServices).validator = validator;
    }

    @Test
    public void addCategoryWithNullName() {
        addCategoryWithInvalidName(null);
    }

    @Test
    public void addCategoryWithShortName() {
        addCategoryWithInvalidName("A");
    }

    private void addCategoryWithInvalidName(final String name) {
        try {
            categoryServices.add(new Category(name));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }
}
