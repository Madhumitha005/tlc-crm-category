package com.tlc.crm.category.initializer;

import com.tlc.commons.service.initializer.ServiceInitializer;
import com.tlc.commons.util.Configurations;

public final class CategoryServiceInitializer implements ServiceInitializer {

    private CategoryServiceInitializer() {
    }

    // Singleton instance holder
    private static final class Instance {

        private static final CategoryServiceInitializer INSTANCE = new CategoryServiceInitializer();
    }

    // Returns the singleton instance of CategoryServiceInitializer
    public static CategoryServiceInitializer getInstance() {
        return Instance.INSTANCE;
    }

    // Performs the pre-initialization activities of the Category module
    public void preInitialize(final Configurations configurations) {

    }

    // Performs the post-initialization activities of the Category module
    @Override
    public void postInitialize(final Configurations configurations) {
    }

    // Stops the Category module and releases its resources
    @Override
    public void stop() {

    }
 }