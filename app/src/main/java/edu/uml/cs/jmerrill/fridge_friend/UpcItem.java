package edu.uml.cs.jmerrill.fridge_friend;

public class UpcItem {
    private String name, id;
    private int expTime;
    private ItemType itemType;

    public UpcItem(String name, String id, int expTime, ItemType itemType){
        this.name = name;
        this.id = id;
        this. itemType = itemType;
        this.expTime = itemType.getExpTime();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getExpTime() {
        return expTime;
    }

    public void setExpTime(int expTime) {
        this.expTime = expTime;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
