package lan.tmsystem.bookhostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lan.tmsystem.bookhostel.data.DataManager;
import lan.tmsystem.bookhostel.data.UserModel;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /// Desmond Diana
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPhoneNumber;
    private EditText mCity;
    private Spinner mCountry;
    private EditText mTextEmail;
    private final DataManager mDm = DataManager.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText mPassword;
    private ConstraintLayout mLoadingOverLay;
    private boolean mIsStudent;
    private EditText mTextHostelName;
    private EditText mTextHostelPrice;
    private EditText mTextHostelSinglesAv;
    private EditText mTextHostelDoublesAv;
    private String mCollection;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDb = FirebaseFirestore.getInstance();

        getRegisterType();

        mFirstName = findViewById(R.id.text_first_name);
        mLastName = findViewById(R.id.text_last_name);
        mPhoneNumber = findViewById(R.id.text_phone_number);
        mCity = findViewById(R.id.text_city);
        mCountry = findViewById(R.id.spinner_address);
        mTextEmail = findViewById(R.id.text_email);
        Button btnRegister = findViewById(R.id.btn_register);
        mPassword = findViewById(R.id.text_password);
        mLoadingOverLay = findViewById(R.id.loading);
        mTextHostelName = findViewById(R.id.text_hostel_name);
        TextView textHostelLabel = findViewById(R.id.text_hostel_label);
        mTextHostelPrice = findViewById(R.id.txtPrice);
        TextView textHostelPriceLabel = findViewById(R.id.text_price_label);
        mTextHostelSinglesAv = findViewById(R.id.txt_singles_available_number);
        TextView textHostelSinglesAvLabel = findViewById(R.id.txt_singles_av);
        mTextHostelDoublesAv = findViewById(R.id.txt_doubles_available_num);
        TextView textHostelDoublesAvLabel = findViewById(R.id.txt_doubles_av);
        ImageButton btnBack = findViewById(R.id.btnRegisterBack);

        mCountry.setOnItemSelectedListener(this);
        ArrayList<String> countries = new ArrayList<>();
        countries.add("Uganda");
        countries.add("Kenya");
        countries.add("Tanzania");
        countries.add("Rwanda");
        countries.add("South Sudan");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountry.setAdapter(dataAdapter);

        if (!mIsStudent) {
            textHostelLabel.setVisibility(View.VISIBLE);
            mTextHostelName.setVisibility(View.VISIBLE);
            textHostelPriceLabel.setVisibility(View.VISIBLE);
            mTextHostelPrice.setVisibility(View.VISIBLE);
            textHostelDoublesAvLabel.setVisibility(View.VISIBLE);
            mTextHostelDoublesAv.setVisibility(View.VISIBLE);
            textHostelSinglesAvLabel.setVisibility(View.VISIBLE);
            mTextHostelSinglesAv.setVisibility(View.VISIBLE);
        }

        btnRegister.setOnClickListener(v -> {
            mLoadingOverLay.setVisibility(View.VISIBLE);
            String email = mTextEmail.getText().toString();
            String passwd = mPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, passwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mDm.setAuth(mAuth);
                                FirebaseUser user = mAuth.getCurrentUser();
                                assert user != null;
                                saveUserDetails(user);
                            } else {
                                Log.w("RegisterFail:", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null, null);
                            }
                        }
                    });
        });
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void saveUserDetails(@NotNull FirebaseUser user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", user.getUid());
        userData.put("firstName", mFirstName.getText().toString());
        userData.put("lastName", mLastName.getText().toString());
        userData.put("city", mCity.getText().toString());
        userData.put("country", mCountry.getSelectedItem().toString());
        userData.put("phoneNumber", mPhoneNumber.getText().toString());
        if (!mIsStudent) {
            userData.put("hostel", mTextHostelName.getText().toString());
            userData.put("manager", true);
        }
        mDb.collection(mCollection)
                .add(userData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            assert documentReference != null;
                            Log.d("userData:", "DocumentSnapshot added with ID:" + documentReference.getId());
                            DocumentSnapshot document = documentReference.get().getResult();
                            assert document != null;
                            updateUI(user, document);
                            if(!mIsStudent) {
                                saveHostelData(user.getUid());
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("userData:", "Error adding document", e);
                        saveUserDetails(user);
                    }
                });
    }

    private void saveHostelData(String uid) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", uid);
        userData.put("name", mTextHostelName.getText().toString());
        userData.put("location", new GeoPoint(0.0,0.0));
        userData.put("price", mTextHostelPrice.getText().toString());
        userData.put("numSingles", mTextHostelSinglesAv.getText().toString());
        userData.put("numDoubles", mTextHostelDoublesAv.getText().toString());
        mDb.collection("hostels")
                .add(userData)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d("up_hostel","Hostel Upload Successful");
                    }
                });
    }

    private void updateUI(FirebaseUser user, DocumentSnapshot document) {
        mLoadingOverLay.setVisibility(View.INVISIBLE);
        if (user != null) {
            mDm.setUser(new UserModel(user, document));
            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void getRegisterType() {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}