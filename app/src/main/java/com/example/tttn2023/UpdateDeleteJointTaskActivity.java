package com.example.tttn2023;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tttn2023.model.GGUser;
import com.example.tttn2023.model.JointTask;
import com.example.tttn2023.model.PersonalTask;
import com.example.tttn2023.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateDeleteJointTaskActivity extends AppCompatActivity implements View.OnClickListener{
    public Spinner sp,sp2;
    private EditText eTitle,eTitle2,eDate, eTime;
    private TextView tvFile;
    private Button btUpdate,btBack,btRemove, btFile;
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
    private List<Map<String, String>> listMember = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_jt);
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
        btRemove.setOnClickListener(this);
        eDate.setOnClickListener(this);
        eTime.setOnClickListener(this);
        btFile.setOnClickListener(this);
        System.out.println(projectId);
        System.out.println(userJointTask.toMap());
        eTitle.setText(userJointTask.getTitle());
        eTitle2.setText(userJointTask.getDescription());
        eDate.setText(userJointTask.getDate());
        eTime.setText(userJointTask.getTime());
//        tvFile.setText(userJointTask.getLinkFile());
        if(userJointTask.getLinkFile().isEmpty()){
            tvFile.setText("Nhân viên chưa gửi file");
            btFile.setVisibility(View.GONE);
        }else{
            tvFile.setText(userJointTask.getLinkFile());
            btFile.setVisibility(View.VISIBLE);
        }
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
        int p=0;
        for(int i=0; i<sp.getCount();i++){
            if(sp.getItemAtPosition(i).toString().equalsIgnoreCase(userJointTask.getStatus())){
                p=i;
                break;
            }
        }
        int p2=0;
        for(int i=0; i<sp2.getCount();i++){
            employeeId = sp2.getItemAtPosition(i).toString();
            if(employeeId.equalsIgnoreCase(userJointTask.getEmployeeId())){
                p2=i;
                break;
            }
        }
        sp.setSelection(p);
        sp2.setSelection(p2);
    }
    private void initView() {
        sp=findViewById(R.id.spCategory);
        sp2=findViewById(R.id.spCategory2);
        eTitle=findViewById(R.id.tvTitle);
        eTitle2=findViewById(R.id.tvTitle2);
        eDate=findViewById(R.id.tvDate);
        eTime=findViewById(R.id.tvTime);
        btUpdate=findViewById(R.id.btUpdate);
        btBack=findViewById(R.id.btBack);
        btRemove=findViewById(R.id.btRemove);
        btFile=findViewById(R.id.btFile);
        tvFile=findViewById(R.id.tvFile);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.category)));
        List<String> members = new ArrayList<>();
        for (Map<String, String> member : listMember) {
            members.add(member.values().toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, members);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        sp2.setAdapter(adapter);

    }
    @Override
    public void onClick(View view) {
        if(view==eDate){
            final Calendar c=Calendar.getInstance();
            int year= c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(UpdateDeleteJointTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
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
            if (picker == null || eTime.getText().toString().isEmpty()) {
                return;
            }
        }
        if(view==btBack){
            finish();
        }
        if(view==btUpdate){
            String title =eTitle.getText().toString();
            String description =eTitle2.getText().toString();
            String date = eDate.getText().toString();
            String status =sp.getSelectedItem().toString();
            String employee =sp2.getSelectedItem().toString();
            String time = eTime.getText().toString();
            String linkFile = tvFile.getText().toString();

            if(!title.isEmpty() && !description.isEmpty() && !date.isEmpty()){
                JointTask newUserJointTask = new JointTask(userJointTask.getId(),title,description, date, time, status, projectId, employee, linkFile);
                System.out.println("project ID" + newUserJointTask.getProjectId());
                updateTask(userId, newUserJointTask);
                finish();
            }
        }
        if(view==btFile){
            if(!tvFile.getText().toString().isEmpty() ){
                copyToClipboard(tvFile.getText().toString());
                System.out.println(tvFile.getText().toString());
                openBrowser(tvFile.getText().toString());
            }
            else{
                Toast.makeText(this, "Chưa có file nào", Toast.LENGTH_SHORT).show();
            }
        }
        if(view==btRemove){
            //int id= userTask.getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thông báo xóa");
            builder.setMessage("Bạn có chắc muốn xóa "+ userJointTask.getTitle()+" không?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ref.child("UserJointTask").child(userId).child(userJointTask.getId()).removeValue();
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
    public void updateTask(String userId, JointTask userJointTask) {
        DatabaseReference userRef = ref.child("UserJointTask").child(userId);
        Map<String, Object> userTaskValues = userJointTask.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(userJointTask.getId(), userTaskValues);
        System.out.println("childUpdates: " + childUpdates);
        userRef.updateChildren(childUpdates);
    }

    private void showTimePicker() {
        calendar = Calendar.getInstance();
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(), "taskmanagement");

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
                userJointTask.setTime(eTime.getText().toString());

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
    private void copyToClipboard(String textToCopy) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }
    private void openBrowser(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            // Use createChooser to give the user the option to choose a browser
            Intent chooser = Intent.createChooser(intent, "Open with");

            // Check if there's an app available to handle the intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                // If no app is found, show a message
                Toast.makeText(this, "No app found to open the link", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle exceptions, e.g., malformed URL
            Toast.makeText(this, "Error opening the link", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
