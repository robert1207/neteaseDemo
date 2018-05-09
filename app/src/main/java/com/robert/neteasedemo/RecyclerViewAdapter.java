package com.robert.neteasedemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class RecyclerViewAdapter extends Adapter<ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_HEADER = 2;
    private Context context;
    private List data;
    private View header;


    public RecyclerViewAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //增加header的布局资源
    public void setHeader(View v) {
        header = v;
    }


    /**
     * 返回这个recyclerlist一共有多少列，其中包括自己加的header和footer
     * @return
     */
    @Override
    public int getItemCount() {
        return data.size() + 1+1;
    }


    /**
     * 根据所在cecyclerlist位置来判断是，header，item，footer
     */
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    /**
     * 根据不同item类型设置对应的布局资源，在这里自定义加来header和footer
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent,
                    false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent,
                    false);
            return new FootViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            return new HeadViewHolder(header);
        }
        return null;
    }


    /**
     * 从data列表拿到数据并设置到每个item上来显示
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        position -= 1;//跳过headview，实际的列表数据应该是从0开始

        if (viewType == TYPE_ITEM) {
            ItemViewHolder ivh = (ItemViewHolder) holder;

            String str = (String)data.get(position);
            ivh.tv.setText(str);

            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        }
    }


    //列表项res资源绑定到对应类型的对象，并记录在viewholder
    static class ItemViewHolder extends ViewHolder {
        TextView tv;
        public ItemViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.item_title);
        }
    }

    //列表项res资源绑定到对应类型的对象，并记录在viewholder
    static class FootViewHolder extends ViewHolder {
        public FootViewHolder(View view) {
            super(view);
        }
    }

    //列表项res资源绑定到对应类型的对象，并记录在viewholder
    static class HeadViewHolder extends ViewHolder {
        public HeadViewHolder(View view) {
            super(view);
        }
    }
}