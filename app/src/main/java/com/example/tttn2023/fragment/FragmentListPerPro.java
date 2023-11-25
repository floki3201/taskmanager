package com.example.tttn2023.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tttn2023.R;
import com.example.tttn2023.TaskActivity;
import com.example.tttn2023.adapter.RecycleViewAdapterPerPro;
import com.example.tttn2023.model.User;
import com.example.tttn2023.model.PersonalProject;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;   // TypeToken

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentListPerPro extends Fragment implements RecycleViewAdapterPerPro.ItemListener {
    private RecyclerView recyclerView;

    RecycleViewAdapterPerPro adapter;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private GoogleSignInAccount account;
    private DatabaseReference ref;
    private String userId = "";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_project,container,false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        adapter = new RecycleViewAdapterPerPro();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        if(User.getCurrent_user() != null) {
            user = User.getCurrent_user();
            userId = user.getUid();
        }
//        else {
//            account = GGUser.getCurrent_user();
//            userId = account.getId();
//        }
        getAllPerPro(userId);
    }
    @Override
    public void onItemClick(View view, int position) {
        PersonalProject perPro = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        intent.putExtra("userPerPro", (Serializable) perPro);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllPerPro(userId);
    }
    private void getAllPerPro(String userId) {
        DatabaseReference userRef = ref.child("UserPerPro");
        List<PersonalProject> userPerProList = new ArrayList<>();
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().getValue() != null) {
                    Object obj = task.getResult().getValue();
                    System.out.println("obj" + obj + " " + obj.getClass());
                    try {
                        ArrayList<Object> list = new ArrayList<>();
                        if (obj instanceof HashMap) {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) obj;

                            ArrayList<Object> arrayList = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : hashMap.entrySet()) {

                                HashMap<String, Object> map = new HashMap<>();
                                map.put(entry.getKey(), entry.getValue());
                                arrayList.add(entry.getValue());
                            }
                            list = arrayList;
                        }

                        System.out.println("list" + list + " " + list.getClass());
                        for (Object entry : list) {
                            if(entry == null)
                                continue;
                            JSONObject jsonObject = new JSONObject((Map) entry);
                            String id = (String) jsonObject.get("id");
                            String title = (String) jsonObject.get("title");
                            String content = (String) jsonObject.get("content");
                            String ownerId = (String) jsonObject.get("ownerId");

                            if (!ownerId.equals(userId))
                                continue;

                            // Using Gson to convert JSON array to String
                            Gson gson = new Gson();
                            String listMember = jsonObject.get("listMember").toString();

                            // Using Gson to convert JSON array as String to List<String>
                            List<String> memberList = gson.fromJson(listMember, new TypeToken<List<String>>() {}.getType());
                            PersonalProject userPerPro = new PersonalProject(id, title, content, ownerId, memberList);
                            userPerProList.add(userPerPro);
                        }
                        adapter.setList(userPerProList);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                        adapter.setItemListener(FragmentListPerPro.this);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

//    @Override
//    public void onItemClick(View view, int position) {
//
//    }
}
