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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.io.IOException;

import lan.tmsystem.bookhostel.data.DataManager;
import lan.tmsystem.bookhostel.data.Hostel;

public class HostelRooms extends AppCompatActivity {

    final DataManager mDb = DataManager.getInstance();
    private FirestoreRecyclerAdapter<Hostel, HostelRooms.HostelRoomHolder> mHostelsAdapter;

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
        setContentView(R.layout.activity_hostel_rooms);

        ImageButton btnBack = findViewById(R.id.btn_back_room);

        btnBack.setOnClickListener(v->{
            onBackPressed();
        });

        RecyclerView hostels = findViewById(R.id.room_list);
        hostels.setLayoutManager(new LinearLayoutManager(this));
        Query query = mDb.getDb().collection("hostels").orderBy("name").limit(50);
        FirestoreRecyclerOptions<Hostel> options = new FirestoreRecyclerOptions.Builder<Hostel>()
                .setQuery(query, Hostel.class)
                .build();
        mHostelsAdapter = new FirestoreRecyclerAdapter<Hostel, HostelRooms.HostelRoomHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HostelRooms.HostelRoomHolder holder, int position, @NonNull Hostel model) {
                holder.setDetails(model);
            }

            @NonNull
            @Override
            public HostelRooms.HostelRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_view_card, parent,false);
                return new HostelRooms.HostelRoomHolder(view);
            }
        };
        hostels.setAdapter(mHostelsAdapter);
    }

    static class HostelRoomHolder extends RecyclerView.ViewHolder {
        private View view;

        public HostelRoomHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        void setDetails(Hostel data) {
            ImageView imgHostel = view.findViewById(R.id.img_hostel_room);
            TextView textHostelRoomDescription = view.findViewById(R.id.txt_room_description);
            TextView textHostelPrice = view.findViewById(R.id.txt_room_price);
            Button book = view.findViewById(R.id.btn_book_room);

            book.setOnClickListener(v -> {
                Intent intent = new Intent(view.getContext(), PaymentsActivity.class);
                view.getContext().startActivity(intent);
            });

            textHostelRoomDescription.setText(data.getName());
            textHostelRoomDescription.setTextSize(30);
        }
    }
}