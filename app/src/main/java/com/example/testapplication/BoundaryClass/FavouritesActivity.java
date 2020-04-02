package com.example.testapplication.BoundaryClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.testapplication.EntityClass.Park;
import com.example.testapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Managing favourite parks in favourites UI.
 */
public class FavouritesActivity extends AppCompatActivity {


    Database db = new Database();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RecyclerView mRecyclerView;
    private FavouritesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * The  list of parks
     */
    public ArrayList<Park> parks = new ArrayList<>();

    /**
     * Remove item.
     * @param position the position of the park in favourites
     */
    public void removeItem(int position) {
        Park park = parks.get(position);
        this.parks.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        Intent intent = getIntent();
        this.parks = intent.getParcelableArrayListExtra("PARKS");


        mRecyclerView = findViewById(R.id.readFavouritesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new FavouritesAdapter(parks);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new FavouritesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Park park = parks.get(position);
                Intent intent = new Intent(FavouritesActivity.this, DisplayParkInformationActivity.class);
                intent.putExtra("PARK", park);
                startActivity(intent);
            }

            @Override
            public void onShareItemClick(int position) {
                Park park = parks.get(position);

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("PARK_DETAILS", park.printParkInformation());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);

                Toast.makeText(FavouritesActivity.this, "Park details has been copied to clip board!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRemoveItemClick(int position) {
                Park park = parks.get(position);
                parks.remove(position);
                mAdapter.notifyItemRemoved(position);
                db.deleteFavouriteByParkUserId(park.getId(), mAuth.getCurrentUser().getUid()).whenComplete((isDeleted, error) -> {
                    if (error == null) {
                        Toast.makeText(FavouritesActivity.this, "The park has been unfavourited.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FavouritesActivity.this, "An error has occurred while unfavouriting this park.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onReviewItemClick(int position) {
                Park park = parks.get(position);
                Intent intent = new Intent(FavouritesActivity.this, AddReviewActivity.class);
                intent.putExtra("PARK_ID", park.getId());
                startActivity(intent);
            }
        });
    }
}
