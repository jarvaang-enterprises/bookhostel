package lan.tmsystem.bookhostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

import lan.tmsystem.bookhostel.data.DataManager;
import lan.tmsystem.bookhostel.data.UserModel;

public class MainActivity extends AppCompatActivity {

    private final DataManager mDm = DataManager.getInstance();
    private LinearLayout mLogin;
    private LinearLayout mChoice;
    private ImageView mStdImg;
    private TextView mStdDisplayName;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private final FirebaseStorage mStorage = FirebaseStorage.getInstance();// Create a storage reference from our app
    private final StorageReference storageRef = mStorage.getReference();
    // Create a child reference
// imagesRef now points to "images"
    private final StorageReference mHostelImagesRef = storageRef.child("images/hostels/");

    // Child references can also take paths
// spaceRef now points to "images/space.jpg
// imagesRef still points to "images"
    private final StorageReference mUserImagesRef = storageRef.child("images/users/");

    @Override
    protected void onStart() {
        super.onStart();
        mDm.setAuth(mAuth);
        mDm.setDb(mDb);
        mDm.setStorage(mStorage);
        mDm.setHostelImagesRef(mHostelImagesRef);
        mDm.setUserImagesRef(mUserImagesRef);
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mChoice.setVisibility(View.INVISIBLE);
            mDm.setCurrentUser(currentUser);
            getUserData(mDm.getCurrentUser().getUid(), mDm.getCurrentUser());
            mLogin.setVisibility(View.VISIBLE);
        }
    }

    private void getUserData(String uid, FirebaseUser currentUser) {
        ArrayList<String> collections = new ArrayList<>();
        collections.add("users");
        collections.add("hostel_man");
        for (String mCollection : collections)
            mDb.collection(mCollection)
                    .whereEqualTo("userId", uid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!task.getResult().isEmpty())
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        if (Objects.requireNonNull(document.get("userId")).toString().equals(uid)) {
                                            mDm.setUser(new UserModel(currentUser, document));
                                            UserModel u = mDm.getUser();
                                            mDm.loggedIn = true;
                                            mStdImg.setImageURI(u.getPhotoUrl());
                                            mStdDisplayName.setText(u.getDisplayName().isEmpty() ? u.getFirstName() + " " + u.getLastName() : u.getDisplayName());
                                            break;
                                        }
                                    }
                            } else {
                                Log.e("error", "signInWithEmail:failure", task.getException());
                                mDm.loggedIn = false;
                            }
                        }
                    });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStudentLogin = findViewById(R.id.btn_student_login);
        Button btnManLogin = findViewById(R.id.btn_man_login);
        Button btnStudentRegister = findViewById(R.id.btn_student_register);
        Button btnManRegister = findViewById(R.id.btn_man_register);
        Button btnContinue = findViewById(R.id.btn_student_login_continue);
        mLogin = findViewById(R.id.login_container);
        mChoice = findViewById(R.id.choice_container);
        mStdImg = findViewById(R.id.student_img);
        mStdDisplayName = findViewById(R.id.student_login_display_name);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                Bundle type = new Bundle();
                if (!mDm.getUser().isManager()) {
                    type.putInt("loginType", 0);
                } else {
                    type.putInt("loginType", 1);
                }
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        btnStudentLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            Bundle type = new Bundle();
            type.putInt("loginType", 0);
            intent.putExtra("type", type);
            startActivity(intent);
        });

        btnStudentRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
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
            startActivity(intent);
        });

        btnManRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            Bundle type = new Bundle();
            type.putInt("loginType", 1);
            intent.putExtra("type", type);
            startActivity(intent);
        });
    }
}