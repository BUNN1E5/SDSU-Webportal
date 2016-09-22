package com.twobitdata.sdsuportalunffl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class gradeAdapter extends RecyclerView.Adapter<gradeAdapter.ItemViewHolder> {

    private LayoutInflater inflater;
    List<ListItem> data = Collections.emptyList();

    public gradeAdapter(Context context, List<ListItem> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void setData( List<ListItem> data){
        this.data = data;
    }
    
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ListItem item = data.get(position);
        holder.mainText.setText(item.mainText);
        holder.subText.setText(item.subText);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    
    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView mainText, subText;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mainText = (TextView) itemView.findViewById(R.id.main_text);
            subText = (TextView) itemView.findViewById(R.id.sub_text);

        }
    }
}
