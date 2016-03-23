package com.martin.androidlame.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @Description: RecyclerView Adapter 基类
 * @author: Created by Martin on 15-8-12.
 */
public abstract class RecyclerArrayAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private ArrayList<M> items = new ArrayList<>();

    public RecyclerArrayAdapter() {
        setHasStableIds(true);
    }

    public void add(M object) {
        final int position = items.size();
        items.add(object);
        notifyItemInserted(position);
    }

    public void add(int index, M object) {
        items.add(index, object);
        notifyItemInserted(index);
    }

    public void addAll(Collection<? extends M> collection) {
        if (collection != null) {
            items.clear();
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    /**
     * 向后插入
     *
     * @param collection
     */
    public void appendAll(Collection<? extends M> collection) {
        if (collection != null) {
            final int start = items.size();
            items.addAll(collection);
            notifyItemRangeInserted(start, items.size());
        }
    }

    public void addAll(M... items) {
        addAll(Arrays.asList(items));
    }


    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(M object) {
        final int position = items.indexOf(object);
        items.remove(position);
        notifyItemRemoved(position);
    }

    public M getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<M> getAll() {
        return items;
    }
}
