package edu.uml.cs.jmerrill.fridge_friend;

public enum ItemType {
    DAIRY(14),
    PRODUCE(7),
    MEAT(7),
    PACKAGED(365);

    private final int expTime;  // in days

    ItemType(int expTime){
        this.expTime = expTime;
    }

    public int getExpTime() {
        return expTime;
    }
}
