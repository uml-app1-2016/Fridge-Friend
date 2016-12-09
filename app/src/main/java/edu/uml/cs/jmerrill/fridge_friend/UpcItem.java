package edu.uml.cs.jmerrill.fridge_friend;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;

import static java.lang.System.out;

public class UpcItem implements Serializable {

    private String name, id; ///> product name and UPC ID
    private int shelfLife; ///> shelf life as assigned by item type
    private ItemType itemType; ///> Type of product (dairy, meat, etc)
    private Calendar dateAdded; ///> The date of item creation
    private byte[] thumbnail; ///> a small thumbnail image of the item

    /**
     * A constructor to determine all fields of the UpcItem
     */
    public UpcItem(String name, String id, ItemType itemType){
        this.name = name;
        this.id = id;
        this.itemType = itemType;
        this.shelfLife = itemType.getShelfLife();

        // gets current date
        dateAdded = Calendar.getInstance();
    }

    /**
     * A constructor that assigns default type to change later
     */
    public UpcItem(String name, String id) {
        this.name = name;
        this.id = id;
        this.itemType = ItemType.PACKAGED;
        this.shelfLife = itemType.getShelfLife();
    }

    public String getName() {
        return name;
    }

    public void setName(String itemName) { this.name = itemName; }

    public String getId() {
        return id;
    }

    public void setId(String number) { this.id = number; }

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

    public void setDateAdded(Calendar cal) { dateAdded = Calendar.getInstance(); }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

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

    /**
     * Loop through ItemType keyword values and match the item to the corresponding type
     * Default to PACKAGED if no keyword matches
     */
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
    }
}
