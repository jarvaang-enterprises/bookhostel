package lan.tmsystem.bookhostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.io.IOException;

import lan.tmsystem.bookhostel.data.DataManager;
import lan.tmsystem.bookhostel.data.Hostel;

public class HostelsActivity extends AppCompatActivity {

    final DataManager mDb = DataManager.getInstance();
    private FirestoreRecyclerAdapter<Hostel, HostelHolder> mHostelsAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        mHostelsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mHostelsAdapter != null)
            mHostelsAdapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostels);

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> {
            finish();
        });

        RecyclerView hostels = findViewById(R.id.hostels_recycler);
        hostels.setLayoutManager(new LinearLayoutManager(this));
        Query query = mDb.getDb().collection("hostels").orderBy("name").limit(50);
        FirestoreRecyclerOptions<Hostel> options = new FirestoreRecyclerOptions.Builder<Hostel>()
                .setQuery(query, Hostel.class)
                .build();
        mHostelsAdapter = new FirestoreRecyclerAdapter<Hostel, HostelHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HostelHolder holder, int position, @NonNull Hostel model) {
                holder.setDetails(model);
            }

            @NonNull
            @Override
            public HostelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hostel_card_view, parent,false);
                return new HostelHolder(view);
            }
        };
        hostels.setAdapter(mHostelsAdapter);
    }

    static class HostelHolder extends RecyclerView.ViewHolder {
        private View view;

        public HostelHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        void setDetails(Hostel data) {
            ImageView imgHostel = view.findViewById(R.id.img_hostel);
            TextView textHostelName = view.findViewById(R.id.text_hostels_name);
            TextView textHostelLocation = view.findViewById(R.id.text_hostel_location);
            TextView linkToHostel = view.findViewById(R.id.link_to_hostel);

            linkToHostel.setOnClickListener(v -> {
//                Intent intent = new Intent(view.getContext(), HostelRooms.class);
//                view.getContext().startActivity(intent);
                Intent intent = new Intent(view.getContext(), PaymentsActivity.class);
                intent.putExtra("price", data.getPrice().toString());
                view.getContext().startActivity(intent);
            });

//            imgHostel.setImageDrawable(Dr);
            textHostelName.setText(data.getName());
            textHostelName.setTextSize(30);
            Geocoder _g = new Geocoder(view.getContext());

            try {
                textHostelLocation.setText(_g.getFromLocation(data.getLocation().getLatitude(), data.getLocation().getLongitude(), 1).get(0).getAddressLine(0));
                textHostelName.setTextSize(20);
            } catch (IOException e) {
                textHostelLocation.setText(R.string.location_name_get_error);
                textHostelName.setTextSize(20);
            }
        }
    }
}