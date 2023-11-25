package com.example.tttn2023.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tttn2023.UpdateDeleteActivity;
import com.example.tttn2023.adapter.RecycleViewAdapter;
import com.example.tttn2023.model.User;
import com.example.tttn2023.R;
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

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentSearch extends Fragment implements RecycleViewAdapter.ItemListener, View.OnClickListener{

    private RecyclerView recyclerView;
    private Button btSearch;
    private SearchView svName, svDes;
    private Spinner spCategory;
    private RecycleViewAdapter adapter;
//    private SQLiteHelper db;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    private FirebaseUser user;
    private GoogleSignInAccount account;
    private List<PersonalTask> userPersonalTaskList;
    private String userId = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        adapter=new RecycleViewAdapter();
        userPersonalTaskList = new ArrayList<>();
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


        getAllTask(userId);

        svName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String searchType = "title";
                findTaskByTitle(userId, s, searchType);
                return true;
            }
        });
        svDes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String searchType = "description";
                if(User.getCurrent_user() != null) {
                    user = User.getCurrent_user();
                    findTaskByTitle(user.getUid(), s, searchType);
                }
//                else {
//                    account = GGUser.getCurrent_user();
//                    findTaskByTitle(account.getId(), s, searchType);
//                }
                return true;
            }
        });

        btSearch.setOnClickListener(this);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s=spCategory.getItemAtPosition(position).toString();
                String searchType = "status";

                if(s.equalsIgnoreCase("Tất cả tình trạng")) {
//                    getAllTask(userId);
                } else {
                    findTaskByTitle(userId, s, searchType);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void initView(View view) {
        recyclerView=view.findViewById(R.id.recycleView);
        btSearch=view.findViewById(R.id.btSearch);
        svName=view.findViewById(R.id.search);
        svDes = view.findViewById(R.id.search2);
        spCategory=view.findViewById(R.id.spCategory);

        String[] arr = getResources().getStringArray(R.array.category);
        String[] arr1=new String[arr.length+1];
        arr1[0]="Tất cả tình trạng";
        for(int i=0;i<arr.length;i++){
            arr1[i+1]=arr[i];
        }
        spCategory.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.item_spinner,arr1));

    }

    @Override
    public void onClick(View view) {
        if(view==btSearch){
        }
    }
    public void getAllTask(String userId) {
        DatabaseReference userRef = ref.child("UserTask").child(userId);
        List<PersonalTask> userTaskList = new ArrayList<>();
        System.out.println("userId: "+ userId);

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
                            PersonalTask userTask = new PersonalTask(id, title, date, time, status, category, description, projectId);
                            userTaskList.add(userTask);
                        }
                        adapter.setList(userTaskList);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                        adapter.setItemListener(FragmentSearch.this);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void findTaskByTitle(String userId, String key, String searchType) {
        DatabaseReference userRef = ref.child("UserTask").child(userId);
        List<PersonalTask> userPersonalTaskList = new ArrayList<>();
        Query query = userRef.orderByChild(searchType).startAt(key).endAt(key + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userPersonalTaskList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PersonalTask userPersonalTask = dataSnapshot.getValue(PersonalTask.class);
                    userPersonalTaskList.add(userPersonalTask);
                }
                adapter.setList(userPersonalTaskList);
                LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                adapter.setItemListener(FragmentSearch.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onItemClick(View view, int position) {
        PersonalTask userPersonalTask = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), UpdateDeleteActivity.class);
        intent.putExtra("userTask", (Serializable) userPersonalTask);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllTask(userId);
    }
}
