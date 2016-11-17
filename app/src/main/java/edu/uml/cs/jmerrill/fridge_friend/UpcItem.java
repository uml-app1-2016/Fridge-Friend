package edu.uml.cs.jmerrill.fridge_friend;

import java.util.Calendar;

public class UpcItem {
    private String name, id;
    private int shelfLife;
    private ItemType itemType;
    private Calendar dateAdded;

    public UpcItem(String name, String id, ItemType itemType){
        this.name = name;
        this.id = id;
        this.itemType = itemType;
        this.shelfLife = itemType.getShelfLife();

        // gets current date
        this.dateAdded = Calendar.getInstance();
    }

    // temp constructor
    public UpcItem(String name, String id) {
        this.name = name;
        this.id = id;
        this.itemType = ItemType.DEFAULT;
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

    public Calendar getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(int year, int month, int day) {
        dateAdded.set(year, month, day);
    }

    /**
     * gets the expiration date as a function of the date added and the shelf life
     * @return expiration date as a {@link Calendar} object
     */
    public Calendar getExpDate() {
        Calendar expDate = dateAdded;

        expDate.add(Calendar.DATE, shelfLife);
        return expDate;
    }
}
