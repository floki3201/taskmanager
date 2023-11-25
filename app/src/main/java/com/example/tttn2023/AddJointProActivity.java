package com.example.tttn2023;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tttn2023.model.User;
import com.example.tttn2023.model.GGUser;
import com.example.tttn2023.model.JointProject;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddJointProActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText eTitle,eContent;
    private Button btUpdate,btCancel;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private GoogleSignInAccount account;
    private String userId = "";
    private JointProject userJointProSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_ql);
        initView();
        btUpdate.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        createNotificationChannel();

        if(User.getCurrent_user() != null) {
            user = User.getCurrent_user();
            userId = user.getUid();
        } else {
            account = GGUser.getCurrent_user();
            userId = account.getId();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Manager Reminder Channel";
            String description = "Cấp quyền để nhận thông báo cho ứng dụng";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("taskmanagement", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    private void initView() {
        eTitle=findViewById(R.id.eTitle);
        eContent=findViewById(R.id.eContent);
        btUpdate=findViewById(R.id.btUpdate);
        btCancel=findViewById(R.id.btCancel);
    }

    @Override
    public void onClick(View view) {
        if(view==btCancel){
            finish();
        }
        if(view==btUpdate){
            String title =eTitle.getText().toString();
            String content =eContent.getText().toString();

            if(!title.isEmpty() && !content.isEmpty() ){
                String newId = ref.push().getKey();
                JointProject userJointPro = new JointProject(newId, title, content);
                addJointPro(userId, userJointPro);
                userJointProSet = userJointPro;
                finish();
            }
        }
    }

    private void addJointPro(String userId, JointProject userJointPro) {
        DatabaseReference userRef = ref.child("UserJointPro").child(userJointPro.getId());
        Query lastQuery = userRef.orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            int lastKey = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                    Object obj = dataSnapshot.getValue();
//                    try {
//                        if (obj instanceof HashMap) {
//                            HashMap<String, Object> hashMap = (HashMap<String, Object>) obj;
//                            ArrayList<Map.Entry<String, Object>> list = new ArrayList<>(hashMap.entrySet());
//                            Map.Entry<String, Object> lastEntry = list.get(list.size() - 1);
//
//                            lastKey = Integer.parseInt(lastEntry.getKey());
//                        }
//                        else {
//                            ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) obj;
//                            lastKey = Integer.parseInt(list.get(list.size() - 1).get("id").toString());
//                        }
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                int newKey = lastKey + 1;
                List<Map<String, String>> memberList = new ArrayList<>();
                Map<String, String> map = new HashMap<>();
                map.put(userId, user.getEmail());
                memberList.add(map);
                JointProject newUserPerPro = new JointProject(userJointPro.getId(),userJointPro.getTitle(), userJointPro.getContent(), userId, memberList);
                userRef.setValue(newUserPerPro);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
