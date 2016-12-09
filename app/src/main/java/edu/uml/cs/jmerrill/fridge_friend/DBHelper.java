package edu.uml.cs.jmerrill.fridge_friend;
//tutorial https://www.tutorialspoint.com/android/android_sqlite_database.htm
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Products.db";
    public static final String PRODUCTS_TABLE_NAME = "products";
    public static final String PRODUCTS_COLUMN_ID = "id";
    public static final String PRODUCTS_COLUMN_NAME = "name";
    public static final String PRODUCTS_COLUMN_TYPE = "type";
    public static final String PRODUCTS_COLUMN_SHELFLIFE = "shelflife";
    public static final String PRODUCTS_COLUMN_UPC = "upc";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //call to init database
        db.execSQL(
                "create table products " +
                        "(id integer primary key, name text,type enum,shelflife integer, upc text, thumbnail BLOB)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete table and rebuild
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    public void DeleteAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS products");

    }

    public boolean insertProduct (UpcItem upc) {
        //grab from passed upc
        String name = upc.getName();
        ItemType type = upc.getItemType();
        int shelflife = upc.getShelfLife();
        String upccode = upc.getId();
        byte[] thumbnail = upc.getThumbnail();


        //pass to db
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("type", String.valueOf(type));
        contentValues.put("shelflife", shelflife);
        contentValues.put("upc", upccode);
        // passing thumbnail as a BLOB type
        contentValues.put("thumbnail", thumbnail);

        db.insert("products", null, contentValues);
        return true;
    }

    public Cursor getData(UpcItem id) {
        //get all data for passed upc in db
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
        byte[] thumbnail = upc.getThumbnail();

        //send to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("type", String.valueOf(type));
        contentValues.put("shelflife", shelflife);
        contentValues.put("upc", upccode);
        contentValues.put("thumbnail", thumbnail);
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
        ArrayList<UpcItem> array_list = new ArrayList<UpcItem>();
        if(numberOfRows() < 1) return array_list;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from products", null );
        res.moveToFirst();
        UpcItem temp = null;

        while(res.isAfterLast() == false){
//            temp.setName(res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)));

            String strtemp1 = res.getString(res.getColumnIndex(PRODUCTS_COLUMN_ID));
            String strtemp2 = res.getString(0);

            temp.setId(strtemp1);
            temp.setName(res.getString(1));
            temp.setItemType(ItemType.getValueAt(res.getInt(2)));
            // retrieve the thumbnail from the blob entry in the db
            temp.setThumbnail(res.getBlob(0));

            array_list.add(temp);
            res.moveToNext();
        }
        return array_list;
    }
}
