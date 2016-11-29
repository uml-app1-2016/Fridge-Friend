package edu.uml.cs.jmerrill.fridge_friend;
//tutorial https://www.tutorialspoint.com/android/android_sqlite_database.htm
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Products.db";
    public static final String PRODUCTS_TABLE_NAME = "products";
    public static final String PRODUCTS_COLUMN_ID = "id";
    public static final String PRODUCTS_COLUMN_NAME = "name";
    public static final String PRODUCTS_COLUMN_TYPE = "type";
    public static final String PRODUCTS_COLUMN_SHELFLIFE = "shelflife";
    public static final String PRODUCTS_COLUMN_UPC = "upc";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        
        db.execSQL(
                "create table products " +
                        "(id integer primary key, name text,type enum,shelflife integer, upc text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertProduct (UpcItem upc) {

        String name = upc.getName();
        ItemType type = upc.getItemType();
        int shelflife = upc.getShelfLife();
        String upccode = upc.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("type", String.valueOf(type));
        contentValues.put("shelflife", shelflife);
        contentValues.put("upc", upccode);

        db.insert("products", null, contentValues);
        return true;
    }

    public Cursor getData(UpcItem id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from products where upc="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PRODUCTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateProduct (UpcItem upc) {

        String name = upc.getName();
        ItemType type = upc.getItemType();
        int shelflife = upc.getShelfLife();
        String upccode = upc.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("type", String.valueOf(type));
        contentValues.put("shelflife", shelflife);
        contentValues.put("upc", upccode);
        db.update("products", contentValues, "upc = ? ", new String[] { upccode } );
        return true;
    }

    public Integer deleteProduct (UpcItem upc) {
        String upccode = upc.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products",
                "id = ? ",
                new String[] { upccode });
    }

    public ArrayList<String> getAllProducts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from products", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
