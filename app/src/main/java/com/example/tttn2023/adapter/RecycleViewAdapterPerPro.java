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
import com.example.tttn2023.UpdateDeletePerProActivity;
import com.example.tttn2023.model.PersonalProject;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapterPerPro extends RecyclerView.Adapter<RecycleViewAdapterPerPro.HomeViewHolder>{
    @NonNull
    private List<PersonalProject> list;
    private ItemListener itemListener;
    public RecycleViewAdapterPerPro() {list = new ArrayList<>();};

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = (ItemListener) itemListener;
    }
    public void setList(List<PersonalProject> list){
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
        PersonalProject userPerProList = list.get(position);
        holder.title.setText(userPerProList.getTitle());
        holder.content.setText(userPerProList.getContent());
        holder.projectId.setText(userPerProList.getId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public PersonalProject getItem(int position) {
        return list.get(position);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title,content,projectId;
        private Button btUpdate;
        public HomeViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tvTitle);
            content = view.findViewById(R.id.tvContent);
            projectId = view.findViewById(R.id.tvProjectId);

            btUpdate = view.findViewById(R.id.btUpdate);
            view.setOnClickListener(this);
            btUpdate.setOnClickListener(this);
//            itemView.findViewById(R.id.btUpdate).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                }
//            });
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
                    PersonalProject clickedItem = list.get(position);
                    // Start a new activity with the information of the clicked item
                    Intent intent = new Intent(view.getContext(), UpdateDeletePerProActivity.class);
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
