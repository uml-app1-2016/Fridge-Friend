package edu.uml.cs.jmerrill.fridge_friend;

public enum ItemType {
    DAIRY(14),
    PRODUCE(7),
    MEAT(7),
    PACKAGED(365),
    DEFAULT(21);

    private final int shelfLife;  // in days

    ItemType(int shelfLife){
        this.shelfLife = shelfLife;
    }

    public int getShelfLife() {
        return shelfLife;
    }
}
