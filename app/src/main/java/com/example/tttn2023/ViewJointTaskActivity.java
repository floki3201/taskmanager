package com.example.tttn2023;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

//import static com.example.tttn2023.UserInfoActivity.PICK_IMAGE_REQUEST;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tttn2023.model.GGUser;
import com.example.tttn2023.model.JointTask;
import com.example.tttn2023.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewJointTaskActivity extends AppCompatActivity implements View.OnClickListener {
//    public Spinner sp,sp2;
    private EditText tvFile;
    private TextView eTitle,eTitle2,eDate, eTime;
    private TextView sp,sp2;
    private Button btUpdate,btBack,btFile;
    private JointTask userJointTask;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseUser user;
    private GoogleSignInAccount account;
    private String userId = "";
    private String projectId = "";
    private String ownerID = "";
    private String employeeId = "";
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog progressDialog;
    private List<Map<String, String>> listMember = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_nv);
        Intent intent = getIntent();
        userJointTask = intent.getParcelableExtra("userTask");
        projectId = (String) intent.getSerializableExtra("projectId");
        ownerID = (String) intent.getSerializableExtra("ownerId");
        if (intent.hasExtra("listMember")) {
            Serializable serializable = intent.getSerializableExtra("listMember");
            if (serializable instanceof List<?>) {
                listMember = (List<Map<String, String>>) serializable;
            }

        }
        initView();
        btUpdate.setOnClickListener(this);
        btBack.setOnClickListener(this);
        btFile.setOnClickListener(this);
        System.out.println(projectId);
        System.out.println(userJointTask.toMap());
        eTitle.setText(userJointTask.getTitle());
        eTitle2.setText(userJointTask.getDescription());
        eDate.setText(userJointTask.getDate());
        eTime.setText(userJointTask.getTime());
        sp.setText(userJointTask.getStatus());
        sp2.setText(userJointTask.getEmployeeId());
        tvFile.setText(userJointTask.getLinkFile());
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
        sp=findViewById(R.id.spCategory);
        sp2=findViewById(R.id.spCategory2);
        eTitle=findViewById(R.id.tvTitle);
        eTitle2=findViewById(R.id.tvTitle2);
        eDate=findViewById(R.id.tvDate);
        eTime=findViewById(R.id.tvTime);
        btUpdate=findViewById(R.id.btUpdate);
        btBack=findViewById(R.id.btCancel);
        btFile=findViewById(R.id.btSendFile);
        tvFile=findViewById(R.id.tvFile);
    }
    @Override
    public void onClick(View view) {
        if(view == btFile){
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST);
        }
        if(view==btBack){
            finish();
        }
        if(view == btUpdate){
            String title =eTitle.getText().toString();
            String description =eTitle2.getText().toString();
            String date = eDate.getText().toString();
            String status =sp.getText().toString();
            String employee =sp2.getText().toString();
            String time = eTime.getText().toString();
            String linkFile = tvFile.getText().toString();

            if(!title.isEmpty() && !description.isEmpty() && !date.isEmpty()){
                JointTask newUserJointTask = new JointTask(userJointTask.getId(),title,description, date, time, status, projectId, employee, linkFile);
                System.out.println("project ID" + newUserJointTask.getProjectId());
                updateTask(ownerID, newUserJointTask);
                finish();
            }
        }
    }
    public void updateTask(String ownerID, JointTask userJointTask) {
        DatabaseReference userRef = ref.child("UserJointTask").child(ownerID);
        Map<String, Object> userTaskValues = userJointTask.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(userJointTask.getId(), userTaskValues);
        System.out.println("childUpdates: " + childUpdates);
        userRef.updateChildren(childUpdates);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected photo
            Uri uri = data.getData();
            showLoadingDialog();

            //edUserPhoto.setText(uri.toString());

            // Upload the photo to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child( projectId+"-"+userJointTask.getId() + "-" + user.getUid());

            UploadTask uploadTask = imageRef.putFile(uri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // how to show loading screen here? help me copilot :(

                // Get the download URL for the uploaded file
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        Log.d("TAG", "Upload file 0: " + downloadUri);
                        dismissLoadingDialog();
                        tvFile.setText(downloadUri.toString());
                    } else {
                        Log.w("TAG", "Upload file failed.", task.getException());
                    }
                }
            });

        }
    }

    private void showLoadingDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Set to false if you want to force the user to wait until the loading is complete
        progressDialog.show();
    }

    private void dismissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
