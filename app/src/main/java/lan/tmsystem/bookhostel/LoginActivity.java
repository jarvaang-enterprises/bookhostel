package lan.tmsystem.bookhostel;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import lan.tmsystem.bookhostel.data.DataManager;
import lan.tmsystem.bookhostel.data.UserModel;

public class LoginActivity extends AppCompatActivity {

    private DataManager mDm;
    private ProgressBar mPBar;
    private TextView mTextErrorLogin;
    private EditText mTextPassword;
    private EditText mTextEmail;
    private boolean mIsStudent;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mDb;
    private String mCollection;

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
        mDb = FirebaseFirestore.getInstance();

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
        switch (i.getExtras().getBundle("type").getInt("loginType")) {
            case 0:
                mCollection = "users";
                mIsStudent = true;
                break;
            case 1:
                mCollection = "hostel_man";
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
            mDm.getAuth().signInWithEmailAndPassword(email, passwd)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("success", "signInWithEmail:success");
                            mDm.setCurrentUser(mDm.getAuth().getCurrentUser());
                            mDm.loggedIn = true;
                            getUserData(mDm.getAuth().getCurrentUser().getUid(), mDm.getCurrentUser());
                        } else {
                            Log.e("error", "signInWithEmail:failure", task.getException());
                            mDm.loggedIn = false;
                            updateUI(null, true, Objects.requireNonNull(task.getException()).getMessage(), null);
                        }
                    });
        }
    }

    private void getUserData(String uid, FirebaseUser currentUser) {
        mDb.collection(mCollection)
                .whereEqualTo("userId", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                if(Objects.requireNonNull(document.get("userId")).toString().equals(uid)){
                                    mDm.setUser(new UserModel(currentUser, document));
                                    updateUI(mDm.getCurrentUser(), false, "", document);
                                }
                            }
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser, boolean error, String message, QueryDocumentSnapshot document) {
        mPBar.setVisibility(View.INVISIBLE);
        if (currentUser != null && !error && document != null) {
            Intent data = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(data);
        } else {
            if(message.toLowerCase().contains("unable to resolve host")) mTextErrorLogin.setText(R.string.connection_lost);
            if(message.toLowerCase().contains("no user record")) mTextErrorLogin.setText(R.string.no_user);
//            Toast.makeText(LoginActivity.this, "An error occurred, Please try again\n"+message, Toast.LENGTH_LONG).show();
        }
    }
}