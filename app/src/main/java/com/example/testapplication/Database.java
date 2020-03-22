package com.example.testapplication;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testapplication.EntityClass.Favourite;
import com.example.testapplication.EntityClass.Park;
import com.example.testapplication.EntityClass.Review;
import com.example.testapplication.EntityClass.User;
import com.google.android.gms.tasks.*;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Ref;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static android.content.ContentValues.TAG;

// Completable Futures are used to handle async requests, explained here https://www.callicoder.com/java-8-completablefuture-tutorial/

/**
 * The type Database.
 */
class Database {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Instantiates a new Database.
     */
    Database() {

    }

    // Park Functions

    /**
     * Load all parks and update overall ratings completable future.
     *
     * @return the completable future
     */
    CompletableFuture<List<Park>> loadAllParksAndUpdateOverallRatings() {
        CompletableFuture<List<Park>> parksFutures = loadAllParks();
        CompletableFuture<List<Review>> reviewsFutures = loadAllReviews();

        return CompletableFuture.allOf(parksFutures, reviewsFutures).thenCompose(result -> {
            List<Park> parks = parksFutures.join();
            List<Review> reviews = reviewsFutures.join();

            HashMap<String, Double> avgReviews = getParkIdAvgRatingsFromReviews(reviews);
            for (String parkId : avgReviews.keySet()) {
                Double curAvgRating = avgReviews.get(parkId);
                Park park = parks.stream().filter(p -> p.getId().equals(parkId)).findFirst().orElse(null);
                if (park != null && curAvgRating != null) {
                    park.setOverallRating(curAvgRating);
                }
            }
            return saveAllParks(parks);
        });
    }

    /**
     * Load all parks reviews and update user name completable future.
     *
     * @return the completable future
     */
    CompletableFuture<Void> loadAllParksReviewsAndUpdateUserName() {
        return loadAllParks().thenCompose(parks -> CompletableFuture
                .allOf(parks
                        .stream()
                        .map(park -> loadAllReviewsAndUpdateUserName(park.getId()))
                        .toArray(CompletableFuture[]::new)));
    }

    /**
     * Load park completable future.
     *
     * @param id the id
     * @return the completable future
     */
    CompletableFuture<Park> loadPark(String id) {
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

    /**
     * Load all reviews and update user name completable future.
     *
     * @param parkId the park id
     * @return the completable future
     */
// Review Functions
    CompletableFuture<List<Review>> loadAllReviewsAndUpdateUserName(String parkId) {
        CompletableFuture<List<Review>> reviewFutures = loadReviewsByParkId(parkId);

        return reviewFutures.thenCompose(reviews -> {
            List<CompletableFuture<Review>>  updatedReviews = reviews.stream()
                    .map(this::updateReviewUserName)
                    .collect(Collectors.toList());
            CompletableFuture<Void> done = CompletableFuture
                    .allOf(updatedReviews.toArray(new CompletableFuture[0]));
            return done.thenApply(v -> updatedReviews
                    .stream()
                    .map(CompletionStage::toCompletableFuture)
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));
        });
    }

    /**
     * Load reviews by park id completable future.
     *
     * @param parkId the park id
     * @return the completable future
     */
    CompletableFuture<List<Review>> loadReviewsByParkId(String parkId) {
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

    /**
     * Create review completable future.
     *
     * @param review the review
     * @return the completable future
     */
    CompletableFuture<String> createReview(Review review) {
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

    /**
     * Update review completable future.
     *
     * @param review the review
     * @return the completable future
     */
    CompletableFuture<Review> updateReview(Review review) {
        final CompletableFuture<Review> future = new CompletableFuture<>();
        db.collection("reviews").document(review.getId())
                .set(review)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        future.complete(review);
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

    private CompletableFuture<Review> updateReviewUserName (Review review) {
        return loadUserByUserId(review.getUserId()).thenCompose(user -> {
            Log.d("DEBUG_APP", user.toString());
            review.setUserName(user.getName());
            return updateReview(review);
        });
    }

    /**
     * Load favourite parks by user id completable future.
     *
     * @param uid the uid
     * @return the completable future
     */
    // Favourite Functions
    CompletableFuture<List<Park>> loadFavouriteParksByUserId(String uid) {
        return loadFavouritesByUserId(uid).thenCompose(favourites -> {
            List<CompletableFuture<Park>> parks = favourites
                    .stream()
                    .map(favourite -> loadPark(favourite.getParkId()))
                    .collect(Collectors.toList());
            CompletableFuture<Void> done = CompletableFuture
                    .allOf(parks.toArray(new CompletableFuture[0]));
            return done.thenApply(v -> parks
                    .stream()
                    .map(CompletionStage::toCompletableFuture)
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));
        });
    }

    /**
     * Load favourites by user id completable future.
     *
     * @param uid the uid
     * @return the completable future
     */
    CompletableFuture<List<Favourite>> loadFavouritesByUserId(String uid) {
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

    /**
     * Delete favourite by park user id completable future.
     *
     * @param parkId the park id
     * @param userId the user id
     * @return the completable future
     */
    CompletableFuture<Boolean> deleteFavouriteByParkUserId(String parkId, String userId) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        CollectionReference colRef = db.collection("favourites");
        colRef.whereEqualTo("userId", userId)
                .whereEqualTo("parkId", parkId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                colRef.document(document.getId()).delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting document", e);
                        future.completeExceptionally(e);
                    }
                });
        return future;
    }


    /**
     * Create favourite completable future.
     *
     * @param favourite the favourite
     * @return the completable future
     */
    CompletableFuture<String> createFavourite(Favourite favourite) {
        return loadFavouriteByParkAndUserId(favourite.getParkId(), favourite.getUserId()).thenCompose(fav -> {
            if (fav == null ) {
                return addFavourite(favourite);
            } else {
                return CompletableFuture.completedFuture("");
            }
        });
    }

    private CompletableFuture<Favourite> loadFavouriteByParkAndUserId(String parkId, String userId) {
        final CompletableFuture<Favourite> future = new CompletableFuture<>();
        db.collection("favourites")
                .whereEqualTo("userId", userId)
                .whereEqualTo("parkId", parkId)
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
                            if(results.size() <= 0) {
                                future.complete(null);
                            } else {
                                future.complete(results.get(0));
                            }
                        } else {
                            Log.d("ERROR", "Error getting documents: ", task.getException());
                            future.completeExceptionally(task.getException());
                        }
                    }
                });
        return future;
    }

    private CompletableFuture<String> addFavourite(Favourite favourite) {
        final CompletableFuture<String> future = new CompletableFuture<>();
        db.collection("favourites").document(favourite.getId())
                .set(favourite)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        future.complete(favourite.getId());
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

    /**
     * Create user completable future.
     *
     * @param user the user
     * @return the completable future
     */
// User Functions
    CompletableFuture<String> createUser(User user) {
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

    /**
     * Load user by user id completable future.
     *
     * @param userId the user id
     * @return the completable future
     */
    CompletableFuture<User> loadUserByUserId(String userId) {
        final CompletableFuture<User> future = new CompletableFuture<>();
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d("SUCCESS", "DocumentSnapshot data: " + document.getData());
                        future.complete(document.toObject(User.class));
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

    /*
    Private Helper Methods
     */

    private CompletableFuture<List<Park>> loadAllParks() {
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

    private CompletableFuture<List<Park>> saveAllParks(List<Park> parks) {
        final CompletableFuture<List<Park>> future = new CompletableFuture<>();
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
                        Log.d("SUCCESS", "Parks successfully saved!");
                        future.complete(parks);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FAILURE", "An error has occurred - parks are not saved!");
                        future.completeExceptionally(e);
                    }
                });
        return future;
    }

    /**
     * Load all reviews completable future.
     *
     * @return the completable future
     */
    public CompletableFuture<List<Review>> loadAllReviews() {
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

    /**
     * Gets park id avg ratings from reviews.
     *
     * @param reviews the reviews
     * @return the park id avg ratings from reviews
     */
    public HashMap<String, Double> getParkIdAvgRatingsFromReviews(List<Review> reviews) {
        HashMap<String, List<Double>> parkIdToRatings = new HashMap<>();
        for (Review review : reviews) {
            String reviewedParkId = review.getParkId();
            Double reviewedRating = review.getRating();
            List<Double> ratingsList = parkIdToRatings.get(reviewedParkId);

            // if list does not exist create it
            if (ratingsList == null) {
                ratingsList = new ArrayList<>();
                ratingsList.add(reviewedRating);
                parkIdToRatings.put(reviewedParkId, ratingsList);
            } else {
                ratingsList.add(reviewedRating);
            }
        }

        HashMap<String, Double> parkIdToAvgRatings = new HashMap<>();
        for (String key : parkIdToRatings.keySet()) {
            List<Double> ratingsList = parkIdToRatings.get(key);
            assert ratingsList != null;
            Double avg = 0.0;
            for (Double rating : ratingsList) {
                avg += rating;
            }
            avg /= ratingsList.size();
            parkIdToAvgRatings.put(key, avg);
        }

        return parkIdToAvgRatings;
    }

    private CompletableFuture<Boolean> deletePark(String parkId) {
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

    private CompletableFuture<List<String>> getParkIdsWithHigherReviewThan(Double baseline) {
        return loadAllReviews().thenApply(reviews -> {
            HashMap<String, Double> parkIdAndAvgRatings = getParkIdAvgRatingsFromReviews(reviews);
            List<String> selectedParkIds = new ArrayList<>();
            for (String parkId : parkIdAndAvgRatings.keySet()) {
                Double avgRating = 5.0;
                avgRating = parkIdAndAvgRatings.get(parkId);
                if (baseline <= avgRating) {
                    selectedParkIds.add(parkId);
                }
            }
            return selectedParkIds;
        });
    }

    private CompletableFuture<List<Park>> getParksWithHigherReviewThan(Double baseline) {
        return loadAllReviews().thenCompose(reviews -> {

            HashMap<String, Double> parkIdAndAvgRatings = getParkIdAvgRatingsFromReviews(reviews);

            List<CompletableFuture<Park>> selectedParksFutures = new ArrayList<>();
            for (String parkId : parkIdAndAvgRatings.keySet()) {
                Double avgRating = 5.0;
                avgRating = parkIdAndAvgRatings.get(parkId);
                if (baseline <= avgRating) {
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

    private CompletableFuture<List<Park>> loadParksFromParkIds(List<String> parkIds) {
        List<CompletableFuture<Park>> futures = new ArrayList<>();
        for (String parkId : parkIds) {
            futures.add(loadPark(parkId));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    private CompletableFuture<List<Review>> loadReviewsByUserId(String uid) {
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

    /**
     * Delete all parks completable future.
     *
     * @return the completable future
     */
// Context context = getApplicationContext();
    CompletableFuture<List<Boolean>> deleteAllParks() {
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

    private void pushParksToFirestore(Context context) {
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
                Park park = makePark(attributes);
                parks.add(park);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return parks;
    }

    private static Park makePark(String[] metadata) {

        String name = metadata[0];
        double locationX = Double.parseDouble(metadata[1].replaceAll("[^\\d.]", ""));
        double locationY = Double.parseDouble(metadata[2].replaceAll("[^\\d.]", ""));

        String amenities_raw = metadata[3];
        ArrayList<String> amenities = new ArrayList<>();
        ;
        if (!amenities_raw.contains("Null")) {
            amenities = Arrays.stream(metadata[3].split("  "))
                    .map(String::toLowerCase).collect(Collectors.toCollection(ArrayList::new));
            amenities.removeAll(Arrays.asList("and", "or", ""));
        }

        String website = metadata[4].replaceAll("\"", "").replaceAll("\'", "");
        String locationAddress = metadata[5];

        return new Park(UUID.randomUUID().toString(), name, "", locationX, locationY, locationAddress, website, amenities);
    }

    private void pushReviewsToFirestore(Context context) {
        List<Review> reviews = getReviewFromCSV(context);
        WriteBatch batch = db.batch();
        for (int i = 0; i < reviews.size(); i++) {
            Review curReview = reviews.get(i);
            DocumentReference docRef = db.collection("reviews").document(curReview.getId());
            batch.set(docRef, curReview);
        }

        batch.commit()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("SUCCESS", "Reviews successfully added!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("FAILURE", "An error has occurred - reviews are not added!");
                }
            });
    }

    private static List<Review> getReviewFromCSV(Context context) {
        // Context context = getApplicationContext();

        ArrayList<Review> reviews = new ArrayList<>();
        try {
            InputStreamReader is = new InputStreamReader(context.getAssets()
                    .open("Reviews.csv"));
            BufferedReader br = new BufferedReader(is);
            String header = br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                Review review = makeReview(attributes);
                reviews.add(review);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return reviews;
    }

    private static Review makeReview(String[] metadata) {
        String userId = metadata[0];
        String parkId = metadata[1];
        double rating = Double.parseDouble(metadata[2].replaceAll("[^\\d.]", ""));
        String description = "";

        return new Review(UUID.randomUUID().toString(), "Chiah Soon", userId, parkId, rating, description);
    }

    private CompletableFuture<Boolean> deleteFavourite(String favouriteId) {
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

    private CompletableFuture<Boolean> deleteReview(String reviewId) {
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