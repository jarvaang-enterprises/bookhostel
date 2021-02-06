package lan.tmsystem.bookhostel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_RESPONSE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStudentLogin = findViewById(R.id.btn_student_login);
        Button btnManLogin = findViewById(R.id.btn_man_login);
        Button btnStudentRegister = findViewById(R.id.btn_student_register);
        Button btnManRegister = findViewById(R.id.btn_man_register);

        btnStudentLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            Bundle type = new Bundle();
            type.putInt("loginType", 0);
            intent.putExtra("type", type);
            startActivityForResult(intent, LOGIN_RESPONSE);
        });

        btnManLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            Bundle type = new Bundle();
            type.putInt("loginType", 1);
            intent.putExtra("type", type);
            startActivityForResult( intent, LOGIN_RESPONSE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_RESPONSE){
            assert data != null;
            Log.d("Login", data.getExtras().getString("result"));
            Toast.makeText(this, data.getExtras().get("result").toString()+"\n"+data.getExtras().get("result_value").toString(), Toast.LENGTH_LONG).show();
        }
        else Log.d("Login", Integer.toString(resultCode));
    }
}