package com.example.tttn2023.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tttn2023.R;
import com.example.tttn2023.UpdateDeleteJointProActivity;
import com.example.tttn2023.UpdateDeletePerProActivity;
import com.example.tttn2023.model.JointPro;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapterJointPro extends RecyclerView.Adapter<RecycleViewAdapterJointPro.HomeViewHolder> {
    @NonNull
    private List<JointPro> list;
    private ItemListener itemListener;
    public RecycleViewAdapterJointPro() {list = new ArrayList<>();}

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project,parent,false);

        return new RecycleViewAdapterJointPro.HomeViewHolder(view);
    }

    public void setItemListener(RecycleViewAdapterJointPro.ItemListener itemListener) {
        this.itemListener = (RecycleViewAdapterJointPro.ItemListener) itemListener;
    }
    public void setList(List<JointPro> list){
        this.list = list;
        notifyDataSetChanged();
    }
//    @Override
//    public RecycleViewAdapterJointPro.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterJointPro.HomeViewHolder holder, int position) {
        JointPro userJointProList = list.get(position);
        holder.title.setText(userJointProList.getTitle());
        holder.content.setText(userJointProList.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public JointPro getItem(int position) {
        return list.get(position);
    }

    public interface ItemListener {
        void onItemClick(View view, int position);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title,content;
        private Button btUpdate;
        public HomeViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tvTitle);
            content = view.findViewById(R.id.tvContent);
            btUpdate = view.findViewById(R.id.btUpdate);
            view.setOnClickListener(this);
            btUpdate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemListener!=null){
                itemListener.onItemClick(view,getAdapterPosition());
            }
            if(view.getId() == R.id.btUpdate){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Retrieve the clicked item
                    JointPro clickedItem = list.get(position);
                    // Start a new activity with the information of the clicked item
                    Intent intent = new Intent(view.getContext(), UpdateDeleteJointProActivity.class);
                    intent.putExtra("projectId", clickedItem.getId());
                    intent.putExtra("title", clickedItem.getTitle());
                    intent.putExtra("content", clickedItem.getContent());
                    intent.putExtra("ownerId", clickedItem.getOwnerId());
//                    intent.putExtra("listMember", (Serializable) clickedItem.getListMember());
                    // Add other data as needed
                    view.getContext().startActivity(intent);
//                    System.out.println("perpro"+intent);
                }
            }
        }
    }
}
