package com.tlc.crm.category.internal.status;

import com.tlc.commons.code.ErrorCodeGroup;
import com.tlc.commons.code.ErrorCodeProvider;

public enum CategoryErrorCodes implements ErrorCodeProvider {

    UNKNOWN_REQUEST_TYPE(0x01),
    CATEGORY_NOT_FOUND(0x02),
    CATEGORY_ALREADY_EXISTS(0x03),
    CATEGORY_NAME_REQUIRED(0x04);

    private final int code;

    CategoryErrorCodes(final int localCode) {
        this.code = CategoryErrorCodeGroup.GROUP.getConvertedCode(localCode);
    }

    @Override
    public int getCode() {
        return code;
    }

    private static class CategoryErrorCodeGroup implements ErrorCodeGroup {

        private static final ErrorCodeGroup GROUP = new CategoryErrorCodeGroup();

        @Override
        public int getPrefix() {
            return 0x00_0_0000;
        }
    }
}