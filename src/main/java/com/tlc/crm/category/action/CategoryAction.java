package com.tlc.crm.category.action;

import com.tlc.commons.code.ErrorCode;
import com.tlc.commons.json.Json;
import com.tlc.commons.json.JsonArray;
import com.tlc.commons.json.JsonObject;

import com.tlc.crm.category.api.manager.CategoryManager;
import com.tlc.crm.category.api.model.Category;
import com.tlc.crm.category.fields.CategoryFields;
import com.tlc.crm.category.internal.status.CategoryErrorCodes;

import com.tlc.crm.common.action.CrmRequest;
import com.tlc.crm.common.action.CrmResponse;
import com.tlc.crm.common.action.secure.CrmSecureAction;

import com.tlc.web.WebAction;

@WebAction(path = "/category")
public final class CategoryAction extends CrmSecureAction {

     private final CategoryManager categoryManager;

     public CategoryAction() {
          this.categoryManager = CategoryManager.getInstance();
     }

     @Override
     public void sProcess(final CrmRequest request, final CrmResponse response) {
          final JsonObject requestJson = request.getRequestJson();
          final long orgId = request.orgId();

          switch (requestJson.getString(CategoryFields.TYPE)) {
               case "create" -> create(orgId, requestJson.getJsonObject(CategoryFields.DATA), response);
               case "get" -> get(orgId, requestJson.getJsonObject(CategoryFields.DATA), response);
               case "list" -> list(orgId, response);
               case "update" -> update(orgId, requestJson.getJsonObject(CategoryFields.DATA), response);
               case "delete" -> delete(orgId, requestJson.getJsonObject(CategoryFields.DATA), response);
               default -> throw ErrorCode.get(CategoryErrorCodes.UNKNOWN_REQUEST_TYPE, "i18n.category.unknown.request.type");
          }
     }

     private void create(final long orgId, final JsonObject data, final CrmResponse response) {

          final Category category = categoryManager.create(orgId, data.getString(CategoryFields.NAME));

          response.put(CategoryFields.ID, category.id());
          response.put(CategoryFields.MESSAGE, "Category created");
     }

     private void get(final long orgId, final JsonObject data, final CrmResponse response) {

          final Category category = requireCategory(orgId, data.getLong(CategoryFields.ID));

          response.put(CategoryFields.ID, category.id());
          response.put(CategoryFields.NAME, category.getName());
          response.put(CategoryFields.CREATED_TIME, category.getCreatedTime());
          response.put(CategoryFields.MODIFIED_TIME, category.getModifiedTime());
     }

     private void list(final long orgId, final CrmResponse response) {

          final JsonArray result = Json.array();

          for (final Category category : categoryManager.list(orgId)) {
               result.put(Json.object()
                       .put(CategoryFields.NAME, category.getName())
                       .put(CategoryFields.ID, category.id()));
          }
          response.put(CategoryFields.CATEGORIES, result);
     }

     private void update(final long orgId, final JsonObject data, final CrmResponse response) {

          categoryManager.update(orgId, data.getLong(CategoryFields.ID), data.getString(CategoryFields.NAME));

          response.put(CategoryFields.MESSAGE, "Category updated successfully");
     }

     private void delete(final long orgId, final JsonObject data, final CrmResponse response) {

          categoryManager.delete(orgId, data.getLong(CategoryFields.ID));

          response.put(CategoryFields.MESSAGE, "Category deleted successfully");
     }

     private Category requireCategory(final long orgId, final long categoryId) {

          final Category category = categoryManager.getById(orgId, categoryId);

          if (category == null) {
               throw ErrorCode.get(CategoryErrorCodes.CATEGORY_NOT_FOUND, "i18n.category.not.found");
          }

          return category;
     }
}