package lan.tmsystem.bookhostel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import lan.tmsystem.bookhostel.data.DataManager;

public class LoginActivity extends AppCompatActivity {

    private DataManager mDm;
    private ProgressBar mPBar;
    private TextView mTextErrorLogin;
    private EditText mTextPassword;
    private EditText mTextEmail;
    private boolean mIsStudent;

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("user", false);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDm = DataManager.getInstance();

        mTextEmail = findViewById(R.id.txtEmail);
        mTextPassword = findViewById(R.id.txtPassword);
        mTextErrorLogin = findViewById(R.id.txtErrorLogin);
        Button login = findViewById(R.id.btnLogin);
        ImageButton back = findViewById(R.id.btnBack);
        mPBar = findViewById(R.id.loadingBar);

        getLoginType();

        back.setOnClickListener(v -> onBackPressed());

        login.setOnClickListener(v -> {
            mPBar.setVisibility(View.VISIBLE);
            authUser(mTextEmail.getText().toString(), mTextPassword.getText().toString(), mTextErrorLogin);
        });
    }

    private void getLoginType() {
        Intent i = getIntent();
        switch (i.getExtras().getBundle("type").getInt("type")) {
            case 0:
                mIsStudent = true;
                break;
            case 1:
                mIsStudent = false;
                break;
            default:
                break;
        }
    }

    private void authUser(String email, String passwd, TextView errors) {
        String error = "";
        if (email.isEmpty()) {
            error += "Email Address required";
            errors.setText(error);
        } else if (passwd.isEmpty()) {
            error += "Password required";
            errors.setText(error);
        } else {
            mDm.mAuth.signInWithEmailAndPassword(email, passwd)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("success", "signInWithEmail:success");
                            mDm.setCurrentUser(mDm.mAuth.getCurrentUser());
                            mDm.loggedIn = true;
                            updateUI(mDm.getCurrentUser(), false, "");
                        } else {
                            Log.e("error", "signInWithEmail:failure", task.getException());
                            mDm.loggedIn = false;
                            updateUI(null, true, Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser currentUser, boolean error, String message) {
        mPBar.setVisibility(View.INVISIBLE);
        if (currentUser != null && !error) {
            Intent data = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(data);
        } else {
            if(message.toLowerCase().contains("unable to resolve host")) mTextErrorLogin.setText(R.string.connection_lost);
            if(message.toLowerCase().contains("no user record")) mTextErrorLogin.setText(R.string.no_user);
//            Toast.makeText(LoginActivity.this, "An error occurred, Please try again\n"+message, Toast.LENGTH_LONG).show();
        }
    }
}