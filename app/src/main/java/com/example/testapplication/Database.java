package com.example.testapplication;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.*;
import com.google.firebase.firestore.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static android.content.ContentValues.TAG;

// Completable Futures are used to handle async requests, explained here https://www.callicoder.com/java-8-completablefuture-tutorial/

class Database {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Database () {

    }

    public <T> CompletableFuture<T> load (final Class<T> type, String collection, UUID id) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        DocumentReference docRef = db.collection(collection).document(id.toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        future.complete(document.toObject(type));
                    } else {
                        Log.d(TAG, "No such document");
                        future.completeExceptionally(new CustomExceptions("Database Error: Document doesn't exist."));
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    future.completeExceptionally(task.getException());
                }
            }
        });
        return future;
    }

    public <T> CompletableFuture<List<T>> loadAll(final Class<T> type, String collection){
        final CompletableFuture<List<T>> future = new CompletableFuture<>();
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<T> results = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                T documentObj = document.toObject(type);
                                results.add(documentObj);
                            }
                            future.complete(results);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return future;
    }

    public <T> CompletableFuture<String> create (final T object, String collection, final String id){
        final CompletableFuture<String> future = new CompletableFuture<>();
        db.collection(collection).document(id)
                .set(object)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        future.complete(id);
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

    public <T> CompletableFuture<String> update (final T object, String collection, final String id){
        final CompletableFuture<String> future = new CompletableFuture<>();
        db.collection(collection).document(id)
                .set(object)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        future.complete(id);
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

    public <T> CompletableFuture<Boolean> delete (final T object, String collection, final String id) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        db.collection(collection).document(id)
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
