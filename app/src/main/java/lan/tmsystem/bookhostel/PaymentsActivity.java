package lan.tmsystem.bookhostel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentsActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        ImageButton btn_back = findViewById(R.id.btn_back);
        TextView textPrice = findViewById(R.id.text_payment_price);
        Button btnPay = findViewById(R.id.btn_pay);
        EditText phoneNumber = findViewById(R.id.text_phone_number_pA);

        btn_back.setOnClickListener(v->{
            onBackPressed();
        });


        textPrice.setText(getIntent().getExtras().get("price").toString());
        btnPay.setText("PAY UGX "+getIntent().getExtras().get("price").toString());
        btnPay.setOnClickListener(v->{
            Toast.makeText(this, "Payments API not intergrated!, But payment would be processed to " + phoneNumber.getText().toString(), Toast.LENGTH_LONG).show();
        });
    }
}