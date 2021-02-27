package lan.tmsystem.bookhostel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import lan.tmsystem.bookhostel.data.DataManager;
import lan.tmsystem.bookhostel.data.UserModel;

public class SettingsActivity extends AppCompatActivity {

    private DataManager mDm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDm = DataManager.getInstance();

        TextView btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v->{
            mDm.logOut();
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
        });
    }
}