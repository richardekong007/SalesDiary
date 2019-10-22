package com.daveace.salesdiary.store;

import android.util.Log;
import android.view.ViewGroup;

import com.daveace.salesdiary.SubCollectionPath;
import com.daveace.salesdiary.alert.ErrorAlert;
import com.daveace.salesdiary.alert.InformationAlert;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;


public class FireStoreHelper {

    private static FireStoreHelper helper = null;
    private FirebaseFirestore fireStore;


    private FireStoreHelper() {
        fireStore = FirebaseFirestore.getInstance();
    }

    public static FireStoreHelper getInstance() {
        if (helper == null)
            helper = new FireStoreHelper();
        return helper;
    }

    public <T> void addDocument(String collection, String id, T document) {
        final String TAG = "FireStoreHelper add:";
        fireStore.collection(collection).document(id)
                .set(document)
                .addOnSuccessListener(documentReference -> {
                    String msg = "Document added successfully";
                    Log.d(TAG, msg);
                })
                .addOnFailureListener(exception -> {
                    String msg = exception.getMessage();
                    Log.d(TAG, msg);
                });
    }

    public void addDocumentToSubCollection(SubCollectionPath metaData, ViewGroup container) {
        final String TAG = "add to sub coll";
        fireStore.collection(metaData.getCollection())
                .document(metaData.getDoc())
                .collection(metaData.getSubCollection())
                .document(metaData.getSubDocId())
                .set(metaData.getSubDoc())
                .addOnSuccessListener(docRef -> {
                    String msg = "Added successfully.";
                    Log.d(TAG, msg);
                    InformationAlert.Builder()
                            .setContext(container.getContext())
                            .setRootView(container)
                            .setMessage(msg)
                            .build()
                            .show();
                })
                .addOnFailureListener(exception -> {
                    String msg = exception.getMessage();
                    ErrorAlert.Builder()
                            .setContext(container.getContext())
                            .setRootView(container)
                            .setMessage(msg)
                            .build()
                            .show();
                    Log.e(TAG, msg);
                });
    }

    public <T> void addDocumentToSubCollection(DocumentReference docRef, T doc) {
        final String TAG = "add to sub coll";
        if (docRef == null) {
            throw new RuntimeException("docRef is null");
        }
        docRef.set(doc)
                .addOnSuccessListener(ref -> {
                    String msg = "Added successfully.";
                    Log.d(TAG, msg);
                })
                .addOnFailureListener(exception -> {
                    String msg = exception.getMessage();
                    Log.e(TAG, msg);
                });
    }

    public DocumentReference getSubDocumentReference(String collection, String userId, String subCollection, String docId) {
        return fireStore.collection(collection)
                .document(userId)
                .collection(subCollection)
                .document(docId);
    }

    public CollectionReference readDocsFromSubCollection(String collection, String userId, String subCollection) {
        return fireStore.collection(collection)
                .document(userId)
                .collection(subCollection);
    }

    private DocumentSnapshot snapshot;

    public <T> Object readDocument(String collection, T document, ViewGroup container) {
        final String TAG = "FireStoreHelper read";
        fireStore.collection(collection)
                .document(document.getClass().getSimpleName())
                .get().addOnSuccessListener(documentSnapshot -> {
            String msg = "Document Read Successfully";
            snapshot = documentSnapshot;
            Log.d(TAG, msg);
            InformationAlert.Builder()
                    .setContext(container.getContext())
                    .setRootView(container)
                    .setMessage(msg)
                    .build()
                    .show();
        }).addOnFailureListener(exception -> {
            String msg = exception.getMessage();
            Log.e(TAG, msg);
            Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        });
        return snapshot.toObject(document.getClass());
    }

    public List<Object> readDocuments(String Collection) {
        final String TAG = "FireStoreHelper read :";
        List<Object> data = new ArrayList<>();
        fireStore.collection(Collection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.d(TAG, "Documents read successfully");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            data.add(document.getData());
                        }
                    } else {
                        Log.e(TAG, "Error getting Documents");
                    }
                });
        return data;
    }

    public <T> void delete(String collection, T document, ViewGroup container) {
        final String TAG = "FireStoreHelper delete:";
        fireStore.collection(collection)
                .document(document.getClass().getSimpleName())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    String msg = "Record Deleted";
                    Log.d(TAG, msg);
                    InformationAlert.Builder()
                            .setContext(container.getContext())
                            .setRootView(container)
                            .setMessage(msg)
                            .build()
                            .show();
                })
                .addOnFailureListener(exception -> {
                    String msg = exception.getMessage();
                    Log.d(TAG, msg);
                    Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
                });

    }

    public <T> void update(String collection, String userId, String subCollection, String docId, T document) {

        final String TAG = "FireStoreHelper update:";
        fireStore.collection(collection).document(userId).collection(subCollection)
                .document(docId).set(document).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Update Successful");
            }
        }).addOnFailureListener(exception -> Log.d(TAG, exception.getMessage()));

    }

    public <T> void update(DocumentReference docRef, T doc) {
        final String TAG = "FireStoreHelper update:";
        if (docRef == null) {
            throw new RuntimeException("Document is null");
        }
        docRef.set(doc)
                .addOnCompleteListener(task -> Log.d(TAG, "Update Successful"));
    }


    public <T> void delete(String collection, String userId, String subCollection, String docId) {
        final String TAG = "FireStoreHelper delete";
        fireStore.collection(collection).document(userId).collection(subCollection)
                .document(docId).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Log.d(TAG, "deleted " + (docId));
                })
                .addOnFailureListener(exception -> Log.d(TAG, exception.getMessage()));
    }

}