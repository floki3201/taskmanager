package com.example.tttn2023.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tttn2023.UpdateDeleteActivity;
import com.example.tttn2023.adapter.RecycleViewAdapter;
import com.example.tttn2023.model.FBUser;
import com.example.tttn2023.R;
import com.example.tttn2023.model.PersonalTask;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentListTask extends Fragment implements RecycleViewAdapter.ItemListener {
    private RecyclerView recyclerView;

    RecycleViewAdapter adapter;

    private String projectId;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private GoogleSignInAccount account;
    private DatabaseReference ref;
    private String userId = "";

    public FragmentListTask(String projectId) {
        this.projectId = projectId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_task,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        adapter = new RecycleViewAdapter();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        if(FBUser.getCurrent_user() != null) {
            user = FBUser.getCurrent_user();
            userId = user.getUid();
        }
//        else {
//            account = GGUser.getCurrent_user();
//            userId = account.getId();
//        }
        getAllTask(userId);
    }


    @Override
    public void onItemClick(View view, int position) {
        //Item item = adapter.getItem(position);
        PersonalTask userPersonalTask = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), UpdateDeleteActivity.class);
        System.out.println("userTask: " + userPersonalTask.toMap());
        intent.putExtra("userTask", (Parcelable) userPersonalTask);
        intent.putExtra("projectId", userPersonalTask.getProjectId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllTask(userId);

    }
    public void getAllTask(String userId) {
        DatabaseReference userRef = ref.child("UserTask").child(userId);
        List<PersonalTask> userPersonalTaskList = new ArrayList<>();

        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().getValue() != null) {
                    Object obj = task.getResult().getValue();
                    System.out.println("obj" + obj + " " + obj.getClass());
                    try {
                        ArrayList<Object> list = new ArrayList<>();
                        if (obj instanceof ArrayList) {
                            list = (ArrayList<Object>) obj;

                        }
                        if (obj instanceof HashMap) {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) obj;

                            ArrayList<Object> arrayList = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {

                                HashMap<String, Object> map = new HashMap<>();
                                map.put(entry.getKey(), entry.getValue());
                                arrayList.add(entry.getValue());
                            }
                            list = (ArrayList<Object>) arrayList;
                        }

                        for (Object entry : list) {
                            if(entry == null)
                                continue;
                            JSONObject jsonObject = new JSONObject((Map) entry);
                            String id = (String) jsonObject.get("id");
                            String title = (String) jsonObject.get("title");
                            String date = (String) jsonObject.get("date");
                            String time = (String) jsonObject.get("time");
                            String description = (String) jsonObject.get("description");
                            String status = (String) jsonObject.get("status");
                            String category = (String) jsonObject.get("category");
                            String projectId = (String) jsonObject.get("projectId");

                            if (!projectId.equals(FragmentListTask.this.projectId))
                                continue;

                            PersonalTask userPersonalTask = new PersonalTask(id, title, date, time, status, category, description, projectId);
                            userPersonalTaskList.add(userPersonalTask);
                        }
                        adapter.setList(userPersonalTaskList);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                        adapter.setItemListener(FragmentListTask.this);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
