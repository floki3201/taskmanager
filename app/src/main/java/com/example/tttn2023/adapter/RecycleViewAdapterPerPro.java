package com.example.tttn2023.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tttn2023.R;
import com.example.tttn2023.model.PerPro;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapterPerPro extends RecyclerView.Adapter<RecycleViewAdapterPerPro.HomeViewHolder>{
    @NonNull
    private List<PerPro> list;
    private ItemListener itemListener;
    public RecycleViewAdapterPerPro() {list = new ArrayList<>();};

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = (ItemListener) itemListener;
    }
    public void setList(List<PerPro> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project,parent,false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        PerPro userPerProList = list.get(position);
        holder.title.setText(userPerProList.getTitle());
        holder.content.setText(userPerProList.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public PerPro getItem(int position) {
        return list.get(position);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title,content;
        public HomeViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tvTitle);
            content = view.findViewById(R.id.tvContent);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemListener!=null){
                itemListener.onItemClick(view,getAdapterPosition());
            }
        }
    }
    public interface ItemListener{
        void onItemClick(View view, int position);
    }

    private class ItemListenner implements ItemListener {
        @Override
        public void onItemClick(View view, int adapterPosition) {
            System.out.println("Hello");
        }
    }
}
