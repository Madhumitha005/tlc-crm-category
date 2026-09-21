package com.tlc.crm.category.internal.category;

import com.tlc.commons.code.ErrorCode;

import com.tlc.crm.category.api.manager.CategoryManager;
import com.tlc.crm.category.api.model.Category;
import com.tlc.crm.category.internal.status.CategoryErrorCodes;
import com.tlc.crm.category.sql.resource.OMCGCATEGORY;

import com.tlc.crm.security.sql.DataStoreProvider;

import com.tlc.sql.common.api.DataContainer;
import com.tlc.sql.common.api.Row;
import com.tlc.sql.common.api.dml.Criteria;
import com.tlc.sql.common.api.dml.OrderByClause;
import com.tlc.sql.common.api.dml.OrderByClause.OrderType;
import com.tlc.sql.common.api.dml.SelectQuery;
import com.tlc.sql.common.api.dml.Table;
import com.tlc.sql.common.api.dml.WhereClause;
import com.tlc.sql.common.api.ds.OrgDataStore;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class CategoryManagerImpl implements CategoryManager {

    private CategoryManagerImpl() {
    }

    private static final class Instance {
        private static final CategoryManagerImpl INSTANCE = new CategoryManagerImpl();
    }

    public static CategoryManagerImpl getInstance() {
        return Instance.INSTANCE;
    }

    private Table table() {
        return Table.get(OMCGCATEGORY.TABLE);
    }

    private OrgDataStore orgDataStore(final long orgId) {
        return DataStoreProvider.getOrgDataStore(orgId);
    }

    @Override
    public Category create(final long orgId, final String name) {
        validateName(name);

        final OrgDataStore orgDataStore = orgDataStore(orgId);
        final Table table = table();
        final WhereClause duplicateCheck = new WhereClause(Criteria.eq(table.getColumn(OMCGCATEGORY.NAME), name));

        if (Objects.nonNull(orgDataStore.get(table, duplicateCheck).getRows(table).findFirst().orElse(null))) {
            throw ErrorCode.get(CategoryErrorCodes.CATEGORY_ALREADY_EXISTS, "i18n.category.already.exists");
        }

        final DataContainer container = DataContainer.create();// Container to Hold the DB Change
        final Row row = new Row(table);
        final long now = Instant.now().toEpochMilli();

        row.setOrgId(orgId);
        row.set(OMCGCATEGORY.NAME, name);
        row.set(OMCGCATEGORY.CREATED_TIME, now);
        row.set(OMCGCATEGORY.MODIFIED_TIME, now);
        container.addNewRow(row);

        orgDataStore.commitChanges(container);
        return toModel(row);
    }

    @Override
    public Category getById(final long orgId, final long categoryId) {
        final Table table = table();
        final Row row = orgDataStore(orgId)
                .get(table, new WhereClause(Criteria.eq(table.getPKColumn(), categoryId)))
                .getRows(table).findFirst().orElse(null);

        return Objects.isNull(row) ? null : toModel(row);
    }

    @Override
    public List<Category> list(final long orgId) {
        final Table table = table();
        final SelectQuery selectQuery = SelectQuery.get(table);

        selectQuery.addSelectClause(table);
        selectQuery.addOrderByClause(new OrderByClause(table.getColumn(OMCGCATEGORY.NAME), OrderType.ASCENDING));

        final DataContainer container = orgDataStore(orgId).get(selectQuery);

        return container.getRows(table).map(this::toModel).toList();
    }

    @Override
    public Category update(final long orgId, final long categoryId, final String name) {
        validateName(name);

        final OrgDataStore orgDataStore = orgDataStore(orgId);
        final Table table = table();
        final Row row = requireRow(orgDataStore, table, categoryId);

        final DataContainer container = DataContainer.create();

        row.set(OMCGCATEGORY.NAME, name);
        row.set(OMCGCATEGORY.MODIFIED_TIME, Instant.now().toEpochMilli());
        container.updateRow(row);

        orgDataStore.commitChanges(container);
        return toModel(row);
    }

    @Override
    public void delete(final long orgId, final long categoryId) {
        final OrgDataStore orgDataStore = orgDataStore(orgId);
        final Table table = table();

        requireRow(orgDataStore, table, categoryId);
        orgDataStore.delete(table, categoryId);
    }

    private Row requireRow(final OrgDataStore orgDataStore, final Table table, final long categoryId) {
        final Row row = orgDataStore.get(table, new WhereClause(Criteria.eq(table.getPKColumn(), categoryId)))
                .getRows(table).findFirst().orElse(null);

        if (Objects.isNull(row)) {
            throw ErrorCode.get(CategoryErrorCodes.CATEGORY_NOT_FOUND, "i18n.category.not.found");
        }
        return row;
    }

    private void validateName(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw ErrorCode.get(CategoryErrorCodes.CATEGORY_NAME_REQUIRED, "i18n.category.name.required");
        }
    }

    private Category toModel(final Row row) {
        final Category category = new Category();

        category.setId(row.getPKValue());
        category.setOrgId(row.getOrgId());
        category.setName(row.get(OMCGCATEGORY.NAME));
        category.setCreatedTime(row.get(OMCGCATEGORY.CREATED_TIME));
        category.setModifiedTime(row.get(OMCGCATEGORY.MODIFIED_TIME));
        return category;
    }
}