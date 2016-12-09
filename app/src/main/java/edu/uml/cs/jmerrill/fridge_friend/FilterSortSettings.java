package edu.uml.cs.jmerrill.fridge_friend;

import java.io.Serializable;
import java.util.logging.Filter;

/**
 * Created by Sam on 12/8/2016.
 */

public class FilterSortSettings implements Serializable {

    // member variables for settings preferences
    private boolean mShowDairy;
    private boolean mShowProduce;
    private boolean mShowMeat;
    private boolean mShowBakery;
    private boolean mShowPackaged;
    private SortBy mSortBy;

    // Default constructor, assigns default settings preferences
    FilterSortSettings() {
        mShowDairy = true;
        mShowProduce = true;
        mShowMeat = true;
        mShowBakery = true;
        mShowPackaged = true;
        mSortBy = SortBy.DateAdded;
    }

    // Custom constructor, assigns custom settings preferences
    FilterSortSettings(boolean showDairy, boolean showProduce, boolean showMeat, boolean showBakery, boolean showPackaged, SortBy sortBy) {
        mShowDairy = showDairy;
        mShowProduce = showProduce;
        mShowMeat = showMeat;
        mShowBakery = showBakery;
        mShowPackaged = showPackaged;
        mSortBy = sortBy;
    }

    public void setDairySetting(boolean input) {
        mShowDairy = input;
    }

    public boolean getDairySetting() {
        return mShowDairy;
    }

    public void setProduceSetting(boolean input) {
        mShowProduce = input;
    }

    public boolean getProduceSetting() {
        return mShowProduce;
    }

    public void setMeatSetting(boolean input) {
        mShowMeat = input;
    }

    public boolean getMeatSetting() {
        return mShowMeat;
    }

    public void setBakerySetting(boolean input) {
        mShowBakery = input;
    }

    public boolean getBakerySetting() {
        return mShowBakery;
    }

    public void setPackagedSetting(boolean input) {
        mShowPackaged = input;
    }

    public boolean getPackagedSetting() {
        return mShowPackaged;
    }

    public void setSortBySetting(SortBy input) {
        mSortBy = input;
    }

    public SortBy getSortBySetting() {
        return mSortBy;
    }
}
