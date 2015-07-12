package com.minhtdh.common.widget;

import android.content.Context;

import java.util.Comparator;
import java.util.List;

public class AlphabetAdapter extends AbstractAlphabeAdapter<String> {
    
    public AlphabetAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }
    
    public String getTitleForPosition(int position) {
        return getItem(position);
    }
    
    @Override
    protected Comparator<? super String> getComparator() {
        return mCollator;
    }
}