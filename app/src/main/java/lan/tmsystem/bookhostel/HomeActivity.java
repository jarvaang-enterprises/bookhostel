package lan.tmsystem.bookhostel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import lan.tmsystem.bookhostel.data.DataManager;
import lan.tmsystem.bookhostel.data.GPSTracker;

public class HomeActivity extends AppCompatActivity {

    private final DataManager mDm = DataManager.getInstance();
    private boolean mLocationSet;
    private FloatingActionButton mLocationEdit;
    private FloatingActionButton mLocationAdd;
    private ConstraintLayout mStudentLayout;
    private ConstraintLayout mManagerLayout;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mLocationEdit = findViewById(R.id.fab_edit_location);
        mLocationAdd = findViewById(R.id.fab_add_location);
        mLocationSet = false;
        mStudentLayout = findViewById(R.id.std_layout);
        mManagerLayout = findViewById(R.id.man_layout);
        ConstraintLayout btnSettings = findViewById(R.id.btn_settings);
        ConstraintLayout btnSettingsMan = findViewById(R.id.btn_settings_man);
        ConstraintLayout btnPayment = findViewById(R.id.btn_payment);
        ConstraintLayout btnHostels = findViewById(R.id.btn_hostels);
        ConstraintLayout btnHostelsInfo = findViewById(R.id.btn_hostels_info);

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnHostelsInfo.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnSettingsMan.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnPayment.setOnClickListener(v -> {
//            Intent intent = new Intent(HomeActivity.this, PaymentsActivity.class);
//            startActivity(intent);
        });

        btnHostels.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HostelsActivity.class);
            startActivity(intent);
        });

        setUserDetails();
        mLocationAdd.setOnClickListener(v -> {
            String[] perms = new String[3];
            perms[0] = Manifest.permission.ACCESS_COARSE_LOCATION;
            perms[1] = Manifest.permission.ACCESS_FINE_LOCATION;
            perms[2] = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
            for (String permission : perms) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, perms, 100);
                    break;
                }
            }
            GPSTracker tracker = new GPSTracker(this);
            if (!tracker.canGetLocation()) {
                tracker.showSettingsAlert();
            } else {
                mDm.updateUserLocation(this, tracker.getLatitude(), tracker.getLongitude());
                mLocationSet = true;
            }
            if (mLocationSet) {
                mLocationAdd.setVisibility(View.INVISIBLE);
                mLocationEdit.setVisibility(View.VISIBLE);
            } else {
                mLocationAdd.setVisibility(View.VISIBLE);
                mLocationEdit.setVisibility(View.INVISIBLE);
            }
        });
        mLocationEdit.setOnClickListener(v -> {
            Intent mapsIntent = new Intent(HomeActivity.this, MapsActivity.class);
            startActivity(mapsIntent);
        });
    }

    private void setUserDetails() {
        if (mDm.getUser() != null)
            if (mDm.getUser().isManager()) {
                mStudentLayout.setVisibility(View.INVISIBLE);
                mManagerLayout.setVisibility(View.VISIBLE);
                mLocationAdd.setVisibility(View.INVISIBLE);
                mLocationEdit.setVisibility(View.INVISIBLE);
            } else {
                mStudentLayout.setVisibility(View.VISIBLE);
                mManagerLayout.setVisibility(View.INVISIBLE);
                mLocationSet = mDm.getUser().getLocation() != null && !mDm.getUser().getLocation().toString().isEmpty();
                if (mLocationSet) {
                    mLocationAdd.setVisibility(View.INVISIBLE);
                    mLocationEdit.setVisibility(View.VISIBLE);
                } else {
                    mLocationAdd.setVisibility(View.VISIBLE);
                    mLocationEdit.setVisibility(View.INVISIBLE);
                }
            }
        else finish();
    }
}