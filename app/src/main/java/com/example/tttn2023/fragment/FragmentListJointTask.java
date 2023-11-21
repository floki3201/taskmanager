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

import com.example.tttn2023.R;
import com.example.tttn2023.UpdateDeleteActivity;
import com.example.tttn2023.adapter.RecycleViewAdapter;
import com.example.tttn2023.adapter.RecycleViewAdapterJointTask;
import com.example.tttn2023.model.FBUser;
import com.example.tttn2023.model.JointTask;
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

public class FragmentListJointTask extends Fragment implements RecycleViewAdapterJointTask.ItemListener{
    private RecyclerView recyclerView;

    RecycleViewAdapterJointTask adapter;

    private String projectId;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private GoogleSignInAccount account;
    private DatabaseReference ref;
    private String userId = "";

    public FragmentListJointTask(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_task,container,false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        adapter = new RecycleViewAdapterJointTask();
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
        getAllJointTask(userId);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllJointTask(userId);

    }
    private void getAllJointTask(String userId) {
        DatabaseReference userRef = ref.child("UserJointTask").child(userId);
        List<JointTask> userJointTaskList = new ArrayList<>();

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
                            String employeeId = (String) jsonObject.get("employeeId");
                            String projectId = (String) jsonObject.get("projectId");

                            if (!projectId.equals(FragmentListJointTask.this.projectId))
                                continue;

                            JointTask userJointTask = new JointTask(id, title,description, date, time, status, projectId, employeeId);
                            userJointTaskList.add(userJointTask);
                        }
                        adapter.setList(userJointTaskList);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                        adapter.setItemListener(FragmentListJointTask.this);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        //Item item = adapter.getItem(position);
//        JointTask userJointTask = adapter.getItem(position);
//        Intent intent = new Intent(getActivity(), UpdateDeleteActivity.class);
//        System.out.println("userTask: " + userJointTask.toMap());
//        intent.putExtra("userTask", (Parcelable) userJointTask);
//        intent.putExtra("projectId", userJointTask.getProjectId());
//        startActivity(intent);
    }
}
