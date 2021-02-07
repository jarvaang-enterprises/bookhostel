package lan.tmsystem.bookhostel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lan.tmsystem.bookhostel.data.DataManager;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_RESPONSE = 999;
    private final DataManager mDm = DataManager.getInstance();
    private LinearLayout mLogin;
    private LinearLayout mChoice;
    private ImageView mStdImg;
    private TextView mStdDisplayName;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            mDm.setCurrentUser(currentUser);
            mDm.loggedIn = true;
            mChoice.setVisibility(View.INVISIBLE);
            mStdImg.setImageURI(mDm.getUser().getPhotoUrl());
            mStdDisplayName.setText(mDm.getUser().getName());
            mLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStudentLogin = findViewById(R.id.btn_student_login);
        Button btnManLogin = findViewById(R.id.btn_man_login);
        Button btnStudentRegister = findViewById(R.id.btn_student_register);
        Button btnManRegister = findViewById(R.id.btn_man_register);
        mLogin = findViewById(R.id.login_container);
        mChoice = findViewById(R.id.choice_container);
        mStdImg = findViewById(R.id.student_img);
        mStdDisplayName = findViewById(R.id.student_login_display_name);

        btnStudentLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            Bundle type = new Bundle();
            type.putInt("loginType", 0);
            intent.putExtra("type", type);
            startActivity(intent);
        });

        btnManLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            Bundle type = new Bundle();
            type.putInt("loginType", 1);
            intent.putExtra("type", type);
            startActivity( intent);
        });
    }
}