package com.example.testapplication;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.testapplication.EntityClass.Favourite;
import com.example.testapplication.EntityClass.Park;
import com.example.testapplication.EntityClass.Review;
import com.example.testapplication.EntityClass.User;
import com.google.android.gms.tasks.*;
import com.google.firebase.firestore.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static android.content.ContentValues.TAG;

// Completable Futures are used to handle async requests, explained here https://www.callicoder.com/java-8-completablefuture-tutorial/

class Database {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Database() {

    }


    CompletableFuture<String> createReview (Review review){
        final CompletableFuture<String> future = new CompletableFuture<>();
        db.collection("reviews").document(review.getId())
                .set(review)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        future.complete(review.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        future.completeExceptionally(e);
                    }
                });
        return future;
    }

    CompletableFuture<Boolean> deleteReview (String reviewId) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        db.collection("reviews").document(reviewId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        future.complete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        future.completeExceptionally(e);
                    }
                });
        return future;
    }

    CompletableFuture<List<Review>> loadReviewsByUserId (String uid) {
        final CompletableFuture<List<Review>> future = new CompletableFuture<>();
        db.collection("reviews")
                .whereEqualTo("userId", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Review> results = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Review documentObj = document.toObject(Review.class);
                                results.add(documentObj);
                            }
                            future.complete(results);
                        } else {
                            Log.d("ERROR", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return future;
    }

    CompletableFuture<Boolean> deleteFavourite (String favouriteId) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        db.collection("favourites").document(favouriteId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        future.complete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        future.completeExceptionally(e);
                    }
                });
        return future;
    }

    CompletableFuture<List<Favourite>> loadFavouritesByUserId (String uid) {
        final CompletableFuture<List<Favourite>> future = new CompletableFuture<>();
        db.collection("favourites")
                .whereEqualTo("userId", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Favourite> results = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Favourite documentObj = document.toObject(Favourite.class);
                                results.add(documentObj);
                            }
                            future.complete(results);
                        } else {
                            Log.d("ERROR", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return future;
    }

    CompletableFuture<List<Park>> loadAllParks(){
        final CompletableFuture<List<Park>> future = new CompletableFuture<>();
        db.collection("parks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Park> results = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                Park documentObj = document.toObject(Park.class);
                                results.add(documentObj);
                            }
                            future.complete(results);
                        } else {
                            future.completeExceptionally(new CustomExceptions("Firebase Error"));
                        }
                    }
                });
        return future;
    }

    CompletableFuture deleteAllParks(){
        loadAllParks().thenApply(parks -> {
            for (Park park: parks) {
                deletePark(park.getId()).getNow(true);
            }
            return null;
        });
        return null;
    }

    CompletableFuture<String> createUser(User user){
        final CompletableFuture<String> future = new CompletableFuture<>();
        db.collection("users").document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        future.complete(user.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        future.completeExceptionally(e);
                    }
                });
        return future;
    }

    private CompletableFuture<Boolean> deletePark (String parkId) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        db.collection("favourites").document(parkId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        future.complete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        future.completeExceptionally(e);
                    }
                });
        return future;
    }


    // Call this from an activity to get the context variable using:
    // Context context = getApplicationContext();
    public void pushParksToFS (Context context) {
        List<Park> parks = getParksFromCSV(context);
        WriteBatch batch = db.batch();
        for (int i = 0; i < parks.size(); i++) {
            Park curPark = parks.get(i);
            DocumentReference docRef = db.collection("parks").document(curPark.getId());
            batch.set(docRef, curPark);
        }

        batch.commit()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("SUCCESS", "Parks successfully added!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("FAILURE", "An error has occurred - parks are not added!");
                }
            });
    }

    private static List<Park> getParksFromCSV(Context context) {
        // Context context = getApplicationContext();

        ArrayList<Park> parks = new ArrayList<>();
        try {
            InputStreamReader is = new InputStreamReader(context.getAssets()
                    .open("Parks.csv"));
            BufferedReader br = new BufferedReader(is);
            String header = br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                Park park = createPark(attributes);
                parks.add(park);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return parks;
    }

    private static Park createPark(String[] metadata) {

        String name = metadata[0];
        double locationX = Double.parseDouble(metadata[1].replaceAll("[^\\d.]", ""));
        double locationY = Double.parseDouble(metadata[2].replaceAll("[^\\d.]", ""));

        String amenities_raw = metadata[3];
        ArrayList<String> amenities = new ArrayList<>() ;;
        if (!amenities_raw.contains("Null")){
            amenities = Arrays.stream(metadata[3].split(" "))
                    .map(String::toLowerCase).collect(Collectors.toCollection(ArrayList::new));
            amenities.removeAll(Arrays.asList("and", "or"));
        }

        String website = metadata[4].replaceAll("\"", "").replaceAll("\'", "");
        String locationAddress = metadata[5];

        return new Park(UUID.randomUUID().toString(), name, "", locationX, locationY, locationAddress, website, amenities);
    }

}
