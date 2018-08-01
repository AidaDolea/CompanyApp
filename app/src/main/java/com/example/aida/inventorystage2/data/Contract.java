package com.example.aida.inventorystage2.data;

/**
 * Created by Aida on 7/29/2018.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {
    public static final String CONTENT_AUTHORITY = "com.example.aida.inventorystage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";
    private Contract() {}
    public static final class CompanyEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY +
                        "/" + PATH_INVENTORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY +
                        "/" + PATH_INVENTORY;
        public static final String TABLE_NAME = "inventory";
        public final static String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SALARY = "salary";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_COMPANY_NAME = "company_name";
        public static final String COLUMN_COMPANY_PHONE = "company_phone";
    }
}
