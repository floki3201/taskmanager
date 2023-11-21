package com.example.tttn2023.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tttn2023.R;
import com.example.tttn2023.model.JointTask;
import com.example.tttn2023.model.PersonalTask;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapterJointTask extends RecyclerView.Adapter<RecycleViewAdapterJointTask.HomeViewHolder>{
    private List<JointTask> list;

    private RecycleViewAdapterJointTask.ItemListener itemListener;
    public RecycleViewAdapterJointTask() {
        list = new ArrayList<>();
    }
    public void setItemListener(RecycleViewAdapterJointTask.ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<JointTask> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public JointTask getItem(int position){
        return list.get(position);
    }
    @NonNull
    @Override
    public RecycleViewAdapterJointTask.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterJointTask.HomeViewHolder holder, int position) {
        JointTask userJointTask = list.get(position);
        holder.title.setText(userJointTask.getTitle());
        holder.title2.setText(userJointTask.getDescription());
        holder.date.setText(userJointTask.getDate());
        holder.time.setText(userJointTask.getTime());
        holder.category.setText(userJointTask.getStatus());
        holder.category2.setText(userJointTask.getEmployeeId());
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
