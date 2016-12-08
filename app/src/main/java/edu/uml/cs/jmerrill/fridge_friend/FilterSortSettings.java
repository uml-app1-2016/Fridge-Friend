package edu.uml.cs.jmerrill.fridge_friend;

/**
 * Created by Sam on 12/8/2016.
 */

public class FilterSortSettings {

    private boolean mShowDairy;
    private boolean mShowProduce;
    private boolean mShowBakery;
    private boolean mShowPackaged;
    private SortBy mSortBy;



    FilterSortSettings(boolean showDairy, boolean showProduce, boolean showBakery, boolean showPackaged, SortBy sortBy) {
        mShowDairy = showDairy;
        mShowProduce = showProduce;
        mShowBakery = showBakery;
        mShowPackaged = showPackaged;
        mSortBy = sortBy;
    }

    public boolean getDairySetting() {
        return mShowDairy;
    }

    public boolean getProduceSetting() {
        return mShowProduce;
    }

    public boolean getBakerySetting() {
        return mShowBakery;
    }

    public boolean getPackagedSetting() {
        return mShowPackaged;
    }

    public SortBy getSortBySetting() {
        return mSortBy;
    }
}
