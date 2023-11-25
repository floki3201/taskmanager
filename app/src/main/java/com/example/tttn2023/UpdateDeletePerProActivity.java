package com.example.tttn2023;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tttn2023.model.User;
import com.example.tttn2023.model.PersonalProject;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateDeletePerProActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText eTitle,eTitle2;
    private Button btUpdate,btBack,btRemove;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private GoogleSignInAccount account;
    private String userId = "";
    private String projectId = "";
    private String title, content, ownerId;
    private PersonalProject perPro;
    private List<String> memberList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_jp);
        initView();
        btUpdate.setOnClickListener(this);
        btBack.setOnClickListener(this);
        btRemove.setOnClickListener(this);
        Intent intent = getIntent();
//        perPro= intent.getParcelableExtra("perPro");
        projectId = (String) intent.getSerializableExtra("projectId");
        title = (String) intent.getSerializableExtra("title");
        content = (String) intent.getSerializableExtra("content");
        ownerId = (String) intent.getSerializableExtra("ownerId");
        memberList = new ArrayList<>();
        memberList.add(ownerId);

        System.out.println(projectId);


        eTitle.setText(title);
        eTitle2.setText(content);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        createNotificationChannel();

        if(User.getCurrent_user() != null) {
            user = User.getCurrent_user();
            userId = user.getUid();
        }
//        else {
//            account = GGUser.getCurrent_user();
//            userId = account.getId();
//        }


    }

    private void createNotificationChannel() {
    }

    private void initView() {
        btUpdate=findViewById(R.id.btUpdate);
        btBack=findViewById(R.id.btBack);
        btRemove=findViewById(R.id.btRemove);
        eTitle=findViewById(R.id.tvTitle);
        eTitle2=findViewById(R.id.tvTitle2);
    }

    @Override
    public void onClick(View view) {
        if(view==btBack){
            finish();
        }
        if(view==btUpdate){
            String title =eTitle.getText().toString();
            String content =eTitle2.getText().toString();
            if(!title.isEmpty() && !content.isEmpty()){
                PersonalProject newUserPerPro = new PersonalProject(projectId,title, content,ownerId,memberList);
//                System.out.println("project ID" + newUserPerPro.getProjectId());
                updatePerPro(projectId, newUserPerPro);
                finish();
            }

        }
        if(view==btRemove){
            //int id= userTask.getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thông báo xóa");
            builder.setMessage("Bạn có chắc muốn xóa "+title+" không?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ref.child("UserPerPro").child(projectId).removeValue();
                    finish();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void updatePerPro(String projectId, PersonalProject newUserPerPro) {
        DatabaseReference userRef = ref.child("UserPerPro");
        Map<String, Object> userPerProValues = newUserPerPro.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(projectId, userPerProValues);
        System.out.println("childUpdates: " + childUpdates);
        userRef.updateChildren(childUpdates);
    }
}
