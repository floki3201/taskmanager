package com.example.tttn2023.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tttn2023.JointTaskActivity;
import com.example.tttn2023.R;
import com.example.tttn2023.TaskActivity;
import com.example.tttn2023.adapter.RecycleViewAdapterJointPro;
import com.example.tttn2023.adapter.RecycleViewAdapterPerPro;
import com.example.tttn2023.model.FBUser;
import com.example.tttn2023.model.JointPro;
import com.example.tttn2023.model.PersonalPro;
import com.example.tttn2023.model.PersonalTask;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentListJointPro extends Fragment implements RecycleViewAdapterJointPro.ItemListener {
    private RecyclerView recyclerView;
    RecycleViewAdapterJointPro adapter;
    private SearchView svName;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private GoogleSignInAccount account;
    private DatabaseReference ref;
    private String userId = "";
    private List<Map<String, String>> listMember = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmen_list_project,container,false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        recyclerView = view.findViewById(R.id.recycleView);
        adapter = new RecycleViewAdapterJointPro();

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
        getAllJointPro(userId);

        svName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                String searchType = "title";
                findProByTitle(userId, s, searchType);
                return true;
            }
        });
    }

    private void initView(View view) {
        recyclerView=view.findViewById(R.id.recycleView);
        svName=view.findViewById(R.id.search);
    }
    public void findProByTitle(String userId, String key,String searchType ){
        DatabaseReference userRef = ref.child("UserJointPro");
        List<JointPro> userJointProList = new ArrayList<>();
        Query query = userRef.orderByChild(searchType).startAt(key).endAt(key + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userJointProList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    JointPro userJointPro = dataSnapshot.getValue(JointPro.class);
                    String ownerId = (String) userJointPro.getOwnerId();

                    if (!ownerId.equals(userId))
                        continue;
                    userJointProList.add(userJointPro);
//                    if(searchType == null){
//                        userJointProList.equals(userJointProList2);
//                        continue;
//                    }
                }
                adapter.setList(userJointProList);
                LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                adapter.setItemListener(FragmentListJointPro.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onItemClick(View view, int position) {
        JointPro jointPro = adapter.getItem(position);
        listMember = jointPro.getListMember();
        Intent intent = new Intent(getActivity(), JointTaskActivity.class);
        intent.putExtra("userJointPro", (Parcelable) jointPro);
        intent.putExtra("memberList", (Serializable) listMember);
        System.out.println("userJointPro: " + jointPro.toMap());
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
//        getAllJointPro(userId);
    }
    private void getAllJointPro(String userId) {
        DatabaseReference userRef = ref.child("UserJointPro");
        List<JointPro> userJointProList = new ArrayList<>();
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

//                            if (!ownerId.equals(userId) || )
//                                continue;

                            // Using Gson to convert JSON array to String
                            Gson gson = new Gson();
                            String listMember = jsonObject.get("listMember").toString();
                            System.out.println("listMember" + listMember + " " + listMember.getClass());
                            // Using Gson to convert JSON array as String to List<String>
                            List<Map<String, String>> memberList = gson.fromJson(listMember, new TypeToken<List<Map<String, String>>>() {}.getType());
                            System.out.println("memberList" + memberList + " " + memberList.getClass());
                            if (!ownerId.equals(userId) && !memberList.contains(new HashMap<String, String>(){{put(userId, FBUser.getCurrent_user().getEmail());}}))
                                continue;
                            if(ownerId.equals(userId)){
                                FBUser.setIsOwner(true);
                            }

                            JointPro userJointPro = new JointPro(id, title, content, ownerId, memberList);
                            System.out.println("userJointPro" + userJointPro + " " + userJointPro.toMap());
                            userJointProList.add(userJointPro);
                        }
                        adapter.setList(userJointProList);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                        adapter.setItemListener(FragmentListJointPro.this);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
