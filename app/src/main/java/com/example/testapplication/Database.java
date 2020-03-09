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
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static android.content.ContentValues.TAG;

// Completable Futures are used to handle async requests, explained here https://www.callicoder.com/java-8-completablefuture-tutorial/

class Database {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Database() {

    }

    // Park Functions
    // Context context = getApplicationContext();
    void pushParksToFirestore (Context context) {
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
                                Park documentObj = document.toObject(Park.class);
//                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
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

    CompletableFuture<List<Boolean>> deleteAllParks(){
        return loadAllParks().thenCompose(parks -> {
            List<CompletableFuture<Boolean>> deletedParks = parks
                    .stream()
                    .map(park -> deletePark(park.getId()))
                    .collect(Collectors.toList());
            CompletableFuture<Void> done = CompletableFuture
                    .allOf(deletedParks.toArray(new CompletableFuture[0]));
            return done.thenApply(v -> deletedParks
                    .stream()
                    .map(CompletionStage::toCompletableFuture)
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));
        });
    }

    CompletableFuture<Park> loadPark(String id){
        final CompletableFuture<Park> future = new CompletableFuture<>();
        DocumentReference docRef = db.collection("parks").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d("SUCCESS", "DocumentSnapshot data: " + document.getData());
                        future.complete(document.toObject(Park.class));
                    } else {
                        Log.d("ERROR", "No such document");
                        future.completeExceptionally(new CustomExceptions("No such document"));
                    }
                } else {
                    Log.d("ERROR", "get failed with ", task.getException());
                    future.completeExceptionally(task.getException());
                }
            }
        });
        return future;
    }

    private CompletableFuture<List<String>> getParkIdsWithHigherReviewThan(Double baseline){
        return loadAllReviews().thenApply(reviews -> {
            HashMap<String, Double> parkIdAndAvgRatings = getParkIdAvgRatingsFromReviews(reviews);
            List<String> selectedParkIds = new ArrayList<>();
            for (String parkId: parkIdAndAvgRatings.keySet()) {
                Double avgRating = 5.0;
                avgRating = parkIdAndAvgRatings.get(parkId);
                if (baseline <= avgRating){
                    selectedParkIds.add(parkId);
                }
            }
            return selectedParkIds;
        });
    }

    CompletableFuture<List<Park>> getParksWithHigherReviewThan(Double baseline) {
        return loadAllReviews().thenCompose(reviews -> {

            HashMap<String, Double> parkIdAndAvgRatings = getParkIdAvgRatingsFromReviews(reviews);

            List<CompletableFuture<Park>> selectedParksFutures = new ArrayList<>();
            for (String parkId: parkIdAndAvgRatings.keySet()) {
                Double avgRating = 5.0;
                avgRating = parkIdAndAvgRatings.get(parkId);
                if (baseline <= avgRating){
                    selectedParksFutures.add(loadPark(parkId));
                }
            }

            return CompletableFuture
                    .allOf(selectedParksFutures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> selectedParksFutures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()));

        });
    }

    // Review Functions
    CompletableFuture<List<Review>> loadReviewsByParkId (String parkId) {
        final CompletableFuture<List<Review>> future = new CompletableFuture<>();
        db.collection("reviews")
                .whereEqualTo("parkId", parkId)
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

    // Favourite Functions
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

    // User Functions
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


    /*
    Private Helper Methods
     */
    private CompletableFuture<List<Review>> loadAllReviews() {
        final CompletableFuture<List<Review>> future = new CompletableFuture<>();
        db.collection("reviews")
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

    private HashMap<String, Double> getParkIdAvgRatingsFromReviews(List<Review> reviews){
        HashMap<String, List<Integer>> parkIdToRatings = new HashMap<>();
        for (Review review: reviews) {
            String reviewedParkId = review.getParkId();
            Integer reviewedRating = review.getRating();
            List<Integer> ratingsList = parkIdToRatings.get(reviewedParkId);

            // if list does not exist create it
            if(ratingsList == null) {
                ratingsList = new ArrayList<>();
                ratingsList.add(reviewedRating);
                parkIdToRatings.put(reviewedParkId, ratingsList);
            } else {
                // add if Car is not already in list
                ratingsList.add(reviewedRating);
            }
        }

        HashMap<String, Double> parkIdToAvgRatings = new HashMap<>();
        for (String key: parkIdToRatings.keySet()) {
            List<Integer> ratingsList = parkIdToRatings.get(key);
            assert ratingsList != null;
            Double avg = 0.0;
            for (Integer rating:ratingsList) {
                avg += rating;
            }
            avg /= ratingsList.size();
            parkIdToAvgRatings.put(key, avg);
        }

        return parkIdToAvgRatings;
    }

    private CompletableFuture<Boolean> deletePark (String parkId) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        db.collection("parks").document(parkId)
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
            amenities.removeAll(Arrays.asList("and", "or", ""));
        }

        String website = metadata[4].replaceAll("\"", "").replaceAll("\'", "");
        String locationAddress = metadata[5];

        return new Park(UUID.randomUUID().toString(), name, "", locationX, locationY, locationAddress, website, amenities);
    }

    private CompletableFuture<List<Park>> loadParksFromParkIds (List<String> parkIds) {
        List<CompletableFuture<Park>> futures = new ArrayList<>();
        for (String parkId: parkIds) {
            futures.add(loadPark(parkId));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    private CompletableFuture<List<Review>> loadReviewsByUserId (String uid) {
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

    private CompletableFuture<Boolean> deleteReview (String reviewId) {
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

}
