package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.EntityClass.Park;
import com.example.testapplication.EntityClass.Review;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavouritesActivity extends AppCompatActivity {

    Database db = new Database();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RecyclerView mRecyclerView;
    private FavouritesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<Park> parks = new ArrayList<>();

//    public void displayFavourites(ArrayList<Park> parks) {
//
//        // display 1st park
//        TextView f1Name = findViewById(R.id.f1Name);
//        f1Name.setText(parks.get(0).getName());
//
//        // display 2nd park, if available
//        if (parks.size() > 1) {
//            TextView f2Name = findViewById(R.id.f2Name);
//            f2Name.setText(parks.get(1).getName());
//        }
//
//        // display 3rd park, if available
//        if (parks.size() > 2) {
//            TextView f3Name = findViewById(R.id.f3Name);
//            f3Name.setText(parks.get(2).getName());
//        }
//
//        // display 4th park, if available
//        if (parks.size() > 3) {
//            TextView f4Name = findViewById(R.id.f4Name);
//            f4Name.setText(parks.get(3).getName());
//        }
//
//        // display 5th park, if available
//        if (parks.size() > 4) {
//            TextView f5Name = findViewById(R.id.f5Name);
//            f5Name.setText(parks.get(4).getName());
//        }
//
//        // display 6th park, if available
//        if (parks.size() > 5) {
//            TextView f6Name = findViewById(R.id.f6Name);
//            f6Name.setText(parks.get(5).getName());
//        }
//
//        // display 7th park, if available
//        if (parks.size() > 6) {
//            TextView f7Name = findViewById(R.id.f7Name);
//            f7Name.setText(parks.get(6).getName());
//        }
//
//        // display 8th park, if available
//        if (parks.size() > 7) {
//            TextView f8Name = findViewById(R.id.f8Name);
//            f8Name.setText(parks.get(7).getName());
//        }
//    }

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

//        ////////////////////////////////////////////
//        // All views
//        LinearLayout f1 = findViewById(R.id.f1);
//        LinearLayout f2 = findViewById(R.id.f2);
//        LinearLayout f3 = findViewById(R.id.f3);
//        LinearLayout f4 = findViewById(R.id.f4);
//        LinearLayout f5 = findViewById(R.id.f5);
//        LinearLayout f6 = findViewById(R.id.f6);
//        LinearLayout f7 = findViewById(R.id.f7);
//        LinearLayout f8 = findViewById(R.id.f8);
//        TextView f1Name = findViewById(R.id.f1Name);
//        TextView f2Name = findViewById(R.id.f2Name);
//        TextView f3Name = findViewById(R.id.f3Name);
//        TextView f4Name = findViewById(R.id.f4Name);
//        TextView f5Name = findViewById(R.id.f5Name);
//        TextView f6Name = findViewById(R.id.f6Name);
//        TextView f7Name = findViewById(R.id.f7Name);
//        TextView f8Name = findViewById(R.id.f8Name);
//        ImageButton f1ShareButton = findViewById(R.id.f1ShareButton);
//        ImageButton f2ShareButton = findViewById(R.id.f2ShareButton);
//        ImageButton f3ShareButton = findViewById(R.id.f3ShareButton);
//        ImageButton f4ShareButton = findViewById(R.id.f4ShareButton);
//        ImageButton f5ShareButton = findViewById(R.id.f5ShareButton);
//        ImageButton f6ShareButton = findViewById(R.id.f6ShareButton);
//        ImageButton f7ShareButton = findViewById(R.id.f7ShareButton);
//        ImageButton f8ShareButton = findViewById(R.id.f8ShareButton);
//        ImageButton f1FavouriteButton = findViewById(R.id.f1FavouriteButton);
//        ImageButton f2FavouriteButton = findViewById(R.id.f2FavouriteButton);
//        ImageButton f3FavouriteButton = findViewById(R.id.f3FavouriteButton);
//        ImageButton f4FavouriteButton = findViewById(R.id.f4FavouriteButton);
//        ImageButton f5FavouriteButton = findViewById(R.id.f5FavouriteButton);
//        ImageButton f6FavouriteButton = findViewById(R.id.f6FavouriteButton);
//        ImageButton f7FavouriteButton = findViewById(R.id.f7FavouriteButton);
//        ImageButton f8FavouriteButton = findViewById(R.id.f8FavouriteButton);
//        ImageButton f1ReviewButton = findViewById(R.id.f1ReviewButton);
//        ImageButton f2ReviewButton = findViewById(R.id.f2ReviewButton);
//        ImageButton f3ReviewButton = findViewById(R.id.f3ReviewButton);
//        ImageButton f4ReviewButton = findViewById(R.id.f4ReviewButton);
//        ImageButton f5ReviewButton = findViewById(R.id.f5ReviewButton);
//        ImageButton f6ReviewButton = findViewById(R.id.f6ReviewButton);
//        ImageButton f7ReviewButton = findViewById(R.id.f7ReviewButton);
//        ImageButton f8ReviewButton = findViewById(R.id.f8ReviewButton);
//        TextView emptyFavListTextView = findViewById(R.id.emptyFavListTextView);
//        ////////////////////////////////////////////
//
//
//        this.parks = getIntent().getParcelableArrayListExtra("PARKS");
//
//        // if not empty
//        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
//        if (parks.isEmpty()) {
//            // make all the 8 rows disappear, only show empty list message
//            f1.setVisibility(View.GONE);
//            f2.setVisibility(View.GONE);
//            f3.setVisibility(View.GONE);
//            f4.setVisibility(View.GONE);
//            f5.setVisibility(View.GONE);
//            f6.setVisibility(View.GONE);
//            f7.setVisibility(View.GONE);
//            f8.setVisibility(View.GONE);
//        } else {
//
//            // make empty list message disappear
//            emptyFavListTextView.setVisibility(View.GONE);
//
//            // display favourite parks
//            this.displayFavourites(this.parks);
//
//            ///// copy to clipboard
//
//            /// Hide parks if not present
//
//
//            ///// remove from favourites
//            f1FavouriteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    f1.setVisibility(View.GONE);
//                }
//            });
//            f2FavouriteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    f2.setVisibility(View.GONE);
//                }
//            });
//            f3FavouriteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    f3.setVisibility(View.GONE);
//                }
//            });
//            f4FavouriteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    f4.setVisibility(View.GONE);
//                }
//            });
//            f5FavouriteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    f5.setVisibility(View.GONE);
//                }
//            });
//            f6FavouriteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    f6.setVisibility(View.GONE);
//                }
//            });
//            f7FavouriteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    f7.setVisibility(View.GONE);
//                }
//            });
//            f8FavouriteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    f8.setVisibility(View.GONE);
//                }
//            });
//
//
//
//            /// select park from favourites list to view park information
//            Intent intent = new Intent(FavouritesActivity.this, DisplayParkInformationActivity.class);
//
//            f1Name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intent.putExtra("PARK", parks.get(0));
//                }
//            });
//            f2Name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intent.putExtra("PARK", parks.get(1));
//                }
//            });
//            f3Name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intent.putExtra("PARK", parks.get(2));
//                }
//            });
//            f4Name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intent.putExtra("PARK", parks.get(3));
//                }
//            });
//            f5Name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intent.putExtra("PARK", parks.get(4));
//                }
//            });
//            f6Name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intent.putExtra("PARK", parks.get(5));
//                }
//            });
//            f7Name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intent.putExtra("PARK", parks.get(6));
//                }
//            });
//            f8Name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intent.putExtra("PARK", parks.get(7));
//                }
//            });
//
//            /*
//            View.OnClickListener clickListener = new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(FavouritesActivity.this, DisplayParkInformationActivity.class);
//
//                    switch(v.getId()) {
//                        case R.id.f1Name:
//                            intent.putExtra("PARK", parks.get(0));
//                            break;
//                        case R.id.f2Name:
//                            intent.putExtra("PARK", parks.get(1));
//                            break;
//                        case R.id.f3Name:
//                            intent.putExtra("PARK", parks.get(2));
//                            break;
//                        case R.id.f4Name:
//                            intent.putExtra("PARK", parks.get(3));
//                            break;
//                        case R.id.f5Name:
//                            intent.putExtra("PARK", parks.get(4));
//                            break;
//                        case R.id.f6Name:
//                            intent.putExtra("PARK", parks.get(5));
//                            break;
//                        case R.id.f7Name:
//                            intent.putExtra("PARK", parks.get(6));
//                            break;
//                        case R.id.f8Name:
//                            intent.putExtra("PARK", parks.get(7));
//                            break;
//                    }
//                    startActivity(intent);
//                }
//            };
//            */
//
//
//            ///// write review
//        }
//
//        // home button
//        Button backToHomeBtn = findViewById(R.id.backToHomeBtn);
//        Intent intent = new Intent(FavouritesActivity.this, MainActivity.class);
//        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(intent);
//            }
//        });

    }
}
