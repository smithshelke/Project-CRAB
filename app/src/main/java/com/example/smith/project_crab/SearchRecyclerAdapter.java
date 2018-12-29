package com.example.smith.project_crab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private Context context;
    private AutocompletePredictionBufferResponse list;
    private OnItemClickListener listener;

    interface OnItemClickListener{
        void onClick(String primaryText);
    }

    public void setItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public SearchRecyclerAdapter(Context context, AutocompletePredictionBufferResponse list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getPrimaryText(null));
        holder.body.setText(list.get(position).getSecondaryText(null));
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(position).getPrimaryText(null).toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.getCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, body;
        public LinearLayout itemLayout;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            itemLayout = itemView.findViewById(R.id.search_item_layout);
        }
    }
}
