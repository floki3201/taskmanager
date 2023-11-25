package com.example.tttn2023;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinProjectActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText eTitle;
    private Button btUpdate,btCancel;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private GoogleSignInAccount account;
    private String userId = "";

    private String userEmail = "";
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_project);
        initView();
        btUpdate.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        createNotificationChannel();

        if(User.getCurrent_user() != null) {
            user = User.getCurrent_user();
            userId = user.getUid();
            userEmail = user.getEmail();
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
            if(!title.isEmpty()){
                boolean done = findProById(userId, title);
                finish();
            } else {
                Toast.makeText(this, "Vui lòng nhập tên dự án", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean findProById(String userId, String key) {
        DatabaseReference userRef = ref.child("UserJointPro");
        JointProject userJointPro = new JointProject();

        // check if project is exist and add user to project

        Query query = userRef.orderByChild("id").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.getChildren().)
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    JointProject userJointProject = dataSnapshot.getValue(JointProject.class);
                    if(userJointProject.getId().equalsIgnoreCase(key)){
                        userJointPro.setId(userJointProject.getId());
                        userJointPro.setTitle(userJointProject.getTitle());
                        userJointPro.setContent(userJointProject.getContent());
                        userJointPro.setOwnerId(userJointProject.getOwnerId());

                        List<Map<String, String>> listMember = userJointProject.getListMember();
                        Map<String, String> map = new HashMap<>();
                        map.put(userId, userEmail);
                        listMember.add(map);

                        userJointPro.setListMember(userJointProject.getListMember());
                        updatePerPro(userJointProject.getId(), userJointPro);

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(userJointPro.getId() != null)
            return true;
        return false;
    }

    private void updatePerPro(String projectId, JointProject newUserPerPro) {
        DatabaseReference userRef = ref.child("UserJointPro");
        Map<String, Object> userPerProValues = newUserPerPro.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(projectId, userPerProValues);
        System.out.println("childUpdates: " + childUpdates);
        userRef.updateChildren(childUpdates);
    }
}
