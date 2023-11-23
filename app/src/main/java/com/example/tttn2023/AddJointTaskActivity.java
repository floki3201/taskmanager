package com.example.tttn2023;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tttn2023.model.FBUser;
import com.example.tttn2023.model.GGUser;
import com.example.tttn2023.model.JointTask;
import com.example.tttn2023.model.PersonalTask;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddJointTaskActivity extends AppCompatActivity implements View.OnClickListener {
    public Spinner sp,sp2;
    private EditText eTitle,eTitle2,eDate, eTime;
    private Button btUpdate,btCancel, btSetAlarm;
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
    private List<Map<String, String>> listMember = new ArrayList<>();
    private JointTask userJointTaskSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_ql);

        Intent intent = getIntent();
        if (intent.hasExtra("projectId")) {
            Serializable serializable = intent.getSerializableExtra("projectId");
            if (serializable instanceof String) {
                projectId = (String) serializable;
            }
        }
        if (intent.hasExtra("memberList")) {
            Serializable serializable = intent.getSerializableExtra("memberList");
            if (serializable instanceof List<?>) {
                listMember = (List<Map<String, String>>) serializable;
            }

        }

        initView();

        btUpdate.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        eDate.setOnClickListener(this);
        eTime.setOnClickListener(this);
//        btSetAlarm.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        createNotificationChannel();

        if(FBUser.getCurrent_user() != null) {
            user = FBUser.getCurrent_user();
            userId = user.getUid();
        } else {
            account = GGUser.getCurrent_user();
            userId = account.getId();
        }
    }

    private void initView() {
        eTitle=findViewById(R.id.tvTitle);
        eTitle2=findViewById(R.id.tvTitle2);
        eDate=findViewById(R.id.tvDate);
        eTime=findViewById(R.id.tvTime);
        btUpdate=findViewById(R.id.btUpdate);
        btCancel=findViewById(R.id.btCancel);
        sp=findViewById(R.id.spCategory);
        sp2=findViewById(R.id.spCategory2);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.category)));
        // i want sp2 have value from listMember
        // Your list of strings

        List<String> members = new ArrayList<>();

        for (Map<String, String> member : listMember) {
            members.add(member.values().toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, members);
        adapter.setDropDownViewResource(R.layout.item_spinner);

// Set the ArrayAdapter on the Spinner
        sp2.setAdapter(adapter);

        //sp2.setAdapter(new ArrayAdapter<>(this,R.layout.item_spinner,listMember));


    }
    @Override
    public void onClick(View view) {
        if(view==eDate){
            final Calendar c=Calendar.getInstance();
            int year= c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(AddJointTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int y, int m, int d) {
                    String date="";
                    if(d>8){
                        if(m>8){
                            date = d +"/"+(m+1)+"/"+y;
                        }else{
                            date=d+"/0"+(m+1)+"/"+y;
                        }
                    }else{
                        if(m>8){
                            date = d +"/"+(m+1)+"/"+y;
                        }else{
                            date="0"+d+"/0"+(m+1)+"/"+y;
                        }

                    }

                    eDate.setText(date);
                }
            },year,month,day);
            dialog.show();
        }
        if (view == eTime) {
            showTimePicker();
            if (picker == null && eTime.getText().toString().isEmpty()) {
                return;
            }
//            btSetAlarm.setEnabled(true);
//            btSetAlarm.setBackground(getResources().getDrawable(R.drawable.button_bg));
        }
        if(view==btCancel){
            finish();
        }
        if(view==btUpdate){
            String title =eTitle.getText().toString();
            String description =eTitle2.getText().toString();
            String date =eDate.getText().toString();
            String time = eTime.getText().toString();
            String status = sp.getSelectedItem().toString();
            String employeeId =sp2.getSelectedItem().toString();
            if(!title.isEmpty() && !description.isEmpty() && !date.isEmpty()){
                JointTask userJointTask = new JointTask(title,description, date, time, status, projectId, employeeId);
                addJointTask(userId, userJointTask);
                userJointTaskSet = userJointTask;
                finish();
            }
        }
    }
    public void addJointTask(String userId, JointTask userJointTask) {
        DatabaseReference userRef = ref.child("UserJointTask").child(userId);
        Query lastQuery = userRef.orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            int lastKey = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Object obj = dataSnapshot.getValue();
                    try {
                        if (obj instanceof HashMap) {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) obj;
                            ArrayList<Map.Entry<String, Object>> list = new ArrayList<>(hashMap.entrySet());
                            Map.Entry<String, Object> lastEntry = list.get(list.size() - 1);

                            lastKey = Integer.parseInt(lastEntry.getKey());
                        }
                        else {
                            ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) obj;
                            lastKey = Integer.parseInt(list.get(list.size() - 1).get("id").toString());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                int newKey = lastKey + 1;
                JointTask newUserJointTask = new JointTask(String.valueOf(newKey), userJointTask.getTitle(),userJointTask.getDescription(), userJointTask.getDate(), userJointTask.getTime() , userJointTask.getStatus(), projectId, userJointTask.getEmployeeId());
                userRef.child(String.valueOf(newKey)).setValue(newUserJointTask);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showTimePicker() {

        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(), "foxandroid");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (picker.getHour() > 12) {

                    eTime.setText(
                            String.format("%02d", (picker.getHour() - 12)) + " : " + String.format("%02d", picker.getMinute()) + " PM"
                    );

                } else {

                    eTime.setText(picker.getHour() + " : " + picker.getMinute() + " AM");

                }

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

            }
        });
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
}
