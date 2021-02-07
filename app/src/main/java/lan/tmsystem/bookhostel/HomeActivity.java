package lan.tmsystem.bookhostel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import lan.tmsystem.bookhostel.data.DataManager;

public class HomeActivity extends AppCompatActivity {

    private DataManager mDm;
    private ImageView mImgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDm = DataManager.getInstance();
        mImgUser = findViewById(R.id.img_user);

        setUserDetails();
    }

    private void setUserDetails() {
        mImgUser.setImageURI(mDm.getUser().getPhotoUrl());
    }
}