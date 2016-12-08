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
        //call to init database
        db.execSQL(
                "create table products " +
                        "(id integer primary key, name text,type enum,shelflife integer, upc text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete table and rebuild
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    public void DeleteAll(SQLiteDatabase db){

        db.execSQL("DROP TABLE IF EXISTS products");

    }

    public boolean insertProduct (UpcItem upc) {
        //grab from passed upc
        String name = upc.getName();
        ItemType type = upc.getItemType();
        int shelflife = upc.getShelfLife();
        String upccode = upc.getId();


        //pass to db
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
        //get all data for passed uoc in db
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from products where upc="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        //count rows
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PRODUCTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateProduct (UpcItem upc) {
        //get data from passed upc
        String name = upc.getName();
        ItemType type = upc.getItemType();
        int shelflife = upc.getShelfLife();
        String upccode = upc.getId();

        //send to database
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

        //use upc id to locate and remove product from db
        String upccode = upc.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products",
                "id = ? ",
                new String[] { upccode });
    }



    public ArrayList<UpcItem> getAllProducts() {

        if(numberOfRows() < 1) return null;
        //to print full db in activity
        ArrayList<UpcItem> array_list = new ArrayList<UpcItem>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from products", null );
        res.moveToFirst();
        UpcItem temp = null;

        while(res.isAfterLast() == false){
            temp.setName(res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)));
           temp.setId(res.getString(res.getColumnIndex(PRODUCTS_COLUMN_ID)));
            temp.setItemType(ItemType.getValueAt(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_TYPE))));

            array_list.add(temp);
            res.moveToNext();
        }
        return array_list;
    }
}
