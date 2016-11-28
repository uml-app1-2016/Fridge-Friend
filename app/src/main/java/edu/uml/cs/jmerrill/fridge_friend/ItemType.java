package edu.uml.cs.jmerrill.fridge_friend;

public enum ItemType {
    DAIRY(14, ItemType.DAIRY_KEYWORDS),
    PRODUCE(7, ItemType.PRODUCE_KEYWORDS),
    MEAT(7, ItemType.MEAT_KEYWORDS),
    BAKERY(14, ItemType.BAKERY_KEYWORDS),
    PACKAGED(365, null),
    DEFAULT(21, null);

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
}
