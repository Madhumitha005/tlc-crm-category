package com.tlc.crm.category.api.manager;

import com.tlc.crm.category.api.model.Category;
import com.tlc.crm.category.internal.category.CategoryManagerImpl;

import java.util.List;

public interface CategoryManager {

    static CategoryManager getInstance() {
        return CategoryManagerImpl.getInstance();
    }

    Category create(long orgId, String name);

    Category getById(long orgId, long categoryId);

    List<Category> list(long orgId);

    Category update(long orgId, long categoryId, String name);

    void delete(long orgId, long categoryId);
}