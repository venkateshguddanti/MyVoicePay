package com.hm.org.voicepay.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hm.org.voicepay.BR;
import com.hm.org.voicepay.ui.holders.BindingViewHolder;
import com.hm.org.voicepay.ui.listener.RecyclerViewClickInterface;

import java.util.ArrayList;

/**
 * Created by Venkatesh.Guddanti on 2/27/2018.
 */

public class RecyclerAdapter<T> extends RecyclerView.Adapter<BindingViewHolder> {

    private Context mContext;
    private ArrayList<T> items;
    private int layoutID;
    private RecyclerViewClickInterface<T> mClickInterface;

    public RecyclerAdapter(Context mContext,ArrayList<T> items,int layoutID,RecyclerViewClickInterface<T> mClickInterface)
    {
        this.mContext = mContext;
        this.items = items;
        this.layoutID = layoutID;
        this.mClickInterface = mClickInterface;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layoutID,parent,false);
        return new BindingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {

        final T item = items.get(position);
        holder.getBindings().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mClickInterface.onClickItem(item);
            }
        });
        holder.getBindings().setVariable(BR.types,item);
        holder.getBindings().executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}