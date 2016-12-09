package edu.uml.cs.jmerrill.fridge_friend;

/**
 * Each entry stores a shelf life and a set of keywords
 */
public enum ItemType {
    DAIRY(14, Keywords.DAIRY_KEYWORDS),
    PRODUCE(7, Keywords.PRODUCE_KEYWORDS),
    MEAT(7, Keywords.MEAT_KEYWORDS),
    BAKERY(14, Keywords.BAKERY_KEYWORDS),
    PACKAGED(365, Keywords.PACKAGED_KEYWORDS);

    private final int shelfLife;  // in days
    private final String[] keywords;

    ItemType(int shelfLife, String[] keywords){
        this.shelfLife = shelfLife;
        this.keywords = keywords;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public String[] getKeywords() {
        return keywords;
    }

    /**
     * Equivalent to values[n]
     * It is more efficient to use a switch case
     *
     * @return the type at the specified index
     */
    public static ItemType getValueAt(int val) {
        switch (val) {
            case 0:
                return DAIRY;
            case 1:
                return PRODUCE;
            case 2:
                return MEAT;
            case 3:
                return BAKERY;
            case 4:
                return PACKAGED;
            default:
                return PACKAGED;
        }
    }

    /**
     * Collections of keywords corresponding to each type
     * For use in applying types based on item name
     */
    private static class Keywords {
        private static final String[] PRODUCE_KEYWORDS =
                {"mango",
                        "coconut",
                        "peach",
                        "blueberry",
                        "cantaloupe",
                        "plum",
                        "apple",
                        "strawberry",
                        "cherry",
                        "watermelon",
                        "melon",
                        "citrus",
                        "pineapple",
                        "banana",
                        "avocado",
                        "grape",
                        "pear",
                        "nectarine",
                        "cucumber",
                        "tree",
                        "berry",
                        "prune",
                        "cabbage",
                        "turnip",
                        "spinach",
                        "radish",
                        "salad",
                        "cucumber",
                        "carrot",
                        "lettuce",
                        "tomato",
                        "eggplant",
                        "rhubarb",
                        "endive",
                        "chard",
                        "veggie",
                        "pea",
                        "fruit",
                        "seedpod",
                        "melon",
                        "cellulose",
                        "blanch",
                        "apple",
                        "onion",
                        "plant",
                        "sauce",
                        "watermelon",
                        "seed"};

        private static final String[] DAIRY_KEYWORDS =
                {"milk",
                        "butter",
                        "cheese",
                        "cream",
                        "half",
                        "yogurt",
                        "yoghurt",
                        "kefir",
                        "nog"};

        private static final String[] BAKERY_KEYWORDS =
                {"bread",
                        "cake",
                        "pie",
                        "pastry",
                        "confection",
                        "pretzel",
                        "bagel",
                        "toast"};

        private static final String[] MEAT_KEYWORDS =
                {"hamburger",
                        "steak",
                        "ham",
                        "pork",
                        "beef",
                        "mutton",
                        "lamb",
                        "sirloin",
                        "bacon",
                        "veal",
                        "sausage",
                        "tenderloin",
                        "venison",
                        "chicken",
                        "burger",
                        "fillet"};

        private static final String[] PACKAGED_KEYWORDS = {};
    }
}
