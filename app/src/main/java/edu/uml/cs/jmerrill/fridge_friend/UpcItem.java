package edu.uml.cs.jmerrill.fridge_friend;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;

import static java.lang.System.out;

public class UpcItem implements Serializable {

    private String name, id;
    private int shelfLife;
    private ItemType itemType;
    private Calendar dateAdded;
    private byte[] thumbnail;

    public UpcItem(String name, String id, ItemType itemType){
        this.name = name;
        this.id = id;
        this.itemType = itemType;
        this.shelfLife = itemType.getShelfLife();

        // gets current date
        dateAdded = Calendar.getInstance();
    }

    // temp constructor
    public UpcItem(String name, String id) {
        this.name = name;
        this.id = id;
        this.itemType = ItemType.PACKAGED;
        this.shelfLife = itemType.getShelfLife();
    }

    public String getName() {
        return name;
    }

    public void setName(String itemname) { this.name = name; }

    public String getId() {
        return id;
    }

    public void setId(String number) { this.id = id; }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) { this.itemType = itemType; }

    public Calendar getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(int year, int month, int day) {
        dateAdded.set(year, month, day);
    }

    public void setDateAdded(Calendar cal) { dateAdded = Calendar.getInstance(); }

    /**
     * gets the expiration date as a function of the date added and the shelf life
     * @return expiration date as a {@link Calendar} object
     */
    public Calendar getExpDate() {
        setDateAdded(Calendar.getInstance());
        if (itemType != null) {
            setShelfLife(itemType.getShelfLife());
        } else {
            setShelfLife(365);
        }
        Calendar expDate = dateAdded;
        if (expDate == null) {
            Log.d("UpcItem", "expDate is Null!");
        }

        Log.d("UpcItem", "shelf life: " + shelfLife);
        expDate.add(Calendar.DATE, shelfLife);
        return expDate;
    }

    public void applyItemType() {
        for (ItemType type : ItemType.values()) {
            for (String keyword : type.getKeywords()) {
                if(name.toLowerCase().contains(keyword)) {
                    itemType = type;
                    return;
                } else {
                    itemType = ItemType.PACKAGED;
                }
            }
        }
        Log.d("UpcItem", "Item type: " + itemType);
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
    
}
