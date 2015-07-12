/*
 * Copyright (C) 2013 TinhVan Outsourcing.
 */
package com.minhtdh.common.widget;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * COMMENTS
 * @author MinhTDH
 * Jul 9, 2013
 */
public abstract class BaseAbstactApdater<T> extends BaseAdapter {

    protected List<T> items;
    
    public BaseAbstactApdater(List<? extends T> pItems) {
        items = (List<T>) pItems;
    }
    @Override
    public int getCount() {
        return (items == null? 0 : items.size());
    }
    @Override
    public T getItem(int position) {
        return (items == null ? null : ((position < 0 || position >= items.size()) ? null : items
                .get(position)));
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
}
