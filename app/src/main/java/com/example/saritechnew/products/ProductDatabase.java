package com.example.saritechnew.products;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ProductDatabase extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "ProductList.db";
        private static final int DATABASE_VERSION = 2;
        private static final String TABLE_NAME = "products";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_PRICE = "price";
        private static final String COLUMN_BARCODE = "barcode";
        private static final String COLUMN_QUANTITY = "quantity";
        private static final String COLUMN_PHOTO_PATH = "photo_path";

        public ProductDatabase(@Nullable Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_PRICE + " REAL, " +
                        COLUMN_BARCODE + " TEXT, " +
                        COLUMN_QUANTITY + " INTEGER, " +
                        COLUMN_PHOTO_PATH + " TEXT" +
                        ")";
                db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // Drop the table if it exists
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
        }

        public void insertProduct(String name, double price, String barcode, int quantity, String photoPath) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_PRICE, price);
                values.put(COLUMN_BARCODE, barcode);
                values.put(COLUMN_QUANTITY, quantity);
                values.put(COLUMN_PHOTO_PATH, photoPath);
                db.insert(TABLE_NAME, null, values);
        }

        public List<Products> getAllProducts(String barcode) {
                List<Products> productList = new ArrayList<>();

                SQLiteDatabase db = getReadableDatabase();
                String selection = COLUMN_BARCODE + " = ?";
                String[] selectionArgs = { barcode };
                Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                        int idColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                        int nameColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME);
                        int priceColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_PRICE);
                        int quantityColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_QUANTITY);
                        int photoPathColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH);

                        do {
                                int id = cursor.getInt(idColumnIndex);
                                String name = cursor.getString(nameColumnIndex);
                                double price = cursor.getDouble(priceColumnIndex);
                                int quantity = cursor.getInt(quantityColumnIndex);
                                String photoPath = cursor.getString(photoPathColumnIndex);

                                Products product = new Products(id, name, price, barcode, quantity, photoPath);
                                productList.add(product);
                        } while (cursor.moveToNext());

                        cursor.close();
                }

                return productList;
        }

        public List<Products> getListProducts() {
                List<Products> productList = new ArrayList<>();

                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                        int idColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                        int nameColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME);
                        int priceColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_PRICE);
                        int barcodeColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_BARCODE);
                        int quantityColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_QUANTITY);
                        int photoPathColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH);

                        do {
                                int id = cursor.getInt(idColumnIndex);
                                String name = cursor.getString(nameColumnIndex);
                                double price = cursor.getDouble(priceColumnIndex);
                                String barcode = cursor.getString(barcodeColumnIndex);
                                int quantity = cursor.getInt(quantityColumnIndex);
                                String photoPath = cursor.getString(photoPathColumnIndex);

                                Products product = new Products(id, name, price, barcode, quantity, photoPath);
                                productList.add(product);
                        } while (cursor.moveToNext());

                        cursor.close();
                }

                return productList;
        }

        public void updateProductByBarcode(String barcode, String name, double price, int quantity) {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_PRICE, price);
                values.put(COLUMN_QUANTITY, quantity);
                db.update(TABLE_NAME, values, COLUMN_BARCODE + " = ?", new String[]{barcode});
        }

        public void deleteProduct(int productId) {
                SQLiteDatabase db = getWritableDatabase();
                String selection = COLUMN_ID + " = ?";
                String[] selectionArgs = { String.valueOf(productId) };
                db.delete(TABLE_NAME, selection, selectionArgs);
        }
}

