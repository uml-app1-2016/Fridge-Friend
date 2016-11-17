package edu.uml.cs.jmerrill.fridge_friend;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class UpcItemLoader extends AsyncTaskLoader<UpcItem> {

    private static String LOG_TAG = UpcItemLoader.class.getSimpleName();
    private String id;

    public UpcItemLoader(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public UpcItem loadInBackground() {
        if (id == null) {
            Log.e(LOG_TAG, "id is null");
            return null;
        }

        UpcItem upcItem = NetworkUtils.fetchUpcItem(id);
        return upcItem;
    }
}
