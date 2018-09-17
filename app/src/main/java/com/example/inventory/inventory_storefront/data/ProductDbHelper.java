package com.example.inventory.inventory_storefront.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for Inventory app. Manages database creation and version management.
 */
public class ProductDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "ProductFinal.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link ProductDbHelper}.
     *
     * @param context of the app
     */
    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the product table
        String SQL_CREATE_PRODUCTSFINAL_TABLE = "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + " ("
                + ProductContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductContract.ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ProductContract.ProductEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + ProductContract.ProductEntry.COLUMN_PRICE + " DOUBLE NOT NULL, "
                + ProductContract.ProductEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + ProductContract.ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PRODUCTSFINAL_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}