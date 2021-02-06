package lan.tmsystem.bookhostel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import lan.tmsystem.bookhostel.data.DataManager;

public class LoginActivity extends AppCompatActivity {

    private DataManager mDm;
    private boolean mIsStudent;

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("result", "");
        data.putExtra("result_value", false);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDm = DataManager.getInstance();

        EditText textEmail = findViewById(R.id.txtEmail);
        EditText textPassword = findViewById(R.id.txtPassword);
        TextView textErrorLogin = findViewById(R.id.txtErrorLogin);
        Button login = findViewById(R.id.btnLogin);

        getLoginType();

        login.setOnClickListener(v -> {
            if (authUser(textEmail.getText().toString(), textPassword.getText().toString(), textErrorLogin)) {
                Intent data = new Intent();
                data.putExtra("result", mDm.userName);
                data.putExtra("result_value", mDm.loggedIn);
                setResult(RESULT_OK, data);
                finish();
            }
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

    private boolean authUser(String uemail, String upasswd, TextView errors) {
        String error = "";
        if (uemail.isEmpty()) {
            error += "Email Address required";
            errors.setText(error);
        } else if (upasswd.isEmpty()) {
            error += "Email Address required";
            errors.setText(error);
        } else {
            mDm.authUser(uemail, upasswd, mIsStudent);
            return true;
        }
        return false;
    }
}