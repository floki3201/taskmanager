package com.example.tttn2023.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tttn2023.R;
import com.example.tttn2023.model.PersonalTask;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter  extends RecyclerView.Adapter<RecycleViewAdapter.HomeViewHolder>{
    private List<PersonalTask> list;

    private ItemListener itemListener;
    public RecycleViewAdapter() {
        list = new ArrayList<>();
    }
    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<PersonalTask> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public PersonalTask getItem(int position){
        return list.get(position);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        PersonalTask userPersonalTask = list.get(position);
        holder.title.setText(userPersonalTask.getTitle());
        holder.title2.setText(userPersonalTask.getDescription());
        holder.date.setText(userPersonalTask.getDate());
        holder.time.setText(userPersonalTask.getTime());
        holder.category.setText(userPersonalTask.getStatus());
        holder.category2.setText(userPersonalTask.getCategory());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title,category,title2,date,time, category2;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tvTitle);
            title2 = view.findViewById(R.id.tvTitle2);
            date = view.findViewById(R.id.tvDate);
            time = view.findViewById(R.id.tvTime);
            category = view.findViewById(R.id.tvCategory);
            category2 = view.findViewById(R.id.tvCategory2);
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
        void onItemClick(View view,int position);
    }
}
