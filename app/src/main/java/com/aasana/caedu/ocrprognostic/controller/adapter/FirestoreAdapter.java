package com.aasana.caedu.ocrprognostic.controller.adapter;
/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.aasana.caedu.ocrprognostic.controller.activity.AeropuertoActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * RecyclerView adapter for displaying the results of a Firestore {@link Query}.
 *
 * Note that this class forgoes some efficiency to gain simplicity. For example, the result of
 * {@link DocumentSnapshot#toObject(Class)} is not cached so the same object may be deserialized
 * many times as the user scrolls.
 *
 * See the adapter classes in FirebaseUI (https://github.com/firebase/FirebaseUI-Android/tree/master/firestore) for a
 * more efficient implementation of a Firestore RecyclerView Adapter.
 */
public abstract class FirestoreAdapter<VH
        extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements EventListener<QuerySnapshot> {

    private static final String TAG = "Firestore Adapter";

    private Query mQuery;
    private ListenerRegistration mRegistration;
    public FirebaseFirestore firestore;
    public CollectionReference collectionReference;
    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();
    private ArrayList<Integer> mSnapshotsPositions = new ArrayList<>();
    private Map<Integer,DocumentSnapshot> mMapSnapshots = new HashMap<>();
//    private String[] arrayAirportDestination;
    private String nameAirport;

   /* public FirestoreAdapter(Query query, String[] arrayAirportDestination) {
        mQuery = query;
        this.arrayAirportDestination = new String[arrayAirportDestination.length];
        for(int i=0; i< arrayAirportDestination.length; i++){
            this.arrayAirportDestination[i] = arrayAirportDestination[i];
            Log.e("FirestoreDestination", ": "+this.arrayAirportDestination[i]);
        }
    }*/
    public FirestoreAdapter(Query query,String nameAirport) {
        this.nameAirport=nameAirport;
        mQuery = query;

    }



    public void startListening() {
        mQuery.addSnapshotListener(this);
        Log.e("FirestoreAdapter", "startListening   mRegistration  ::"+ mQuery.getFirestore().toString());

    }


    public void stopListening() {
        mSnapshots.clear();
        notifyDataSetChanged();
    }

    public void setQuery(Query query) {
        mSnapshots.clear();
        notifyDataSetChanged();

        // Listen to new query
        mQuery = query;
        startListening();
    }

    @Override
    public int getItemCount() {
        return mSnapshots.size();
    }

    protected DocumentSnapshot getSnapshot(int index) {
        return mSnapshots.get(index);
    }
    public Map<Integer,DocumentSnapshot> getmMapSnapshots(){
        return mMapSnapshots;
    }
    public ArrayList<DocumentSnapshot> getmSnapshots(){
        return mSnapshots;
    }
    public ArrayList<Integer> getmSnapshotsPosition(){
        return mSnapshotsPositions;
    }

    protected void onError(FirebaseFirestoreException e) {

    };
    protected void onModified(){

    }
    protected void onDelete(){

    }

    protected void onDataChanged() {

    }
    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        // Handle errors
        if (e != null) {
            Log.w(TAG, "onEvent:error", e);
            return;
        }
        Log.e("query OnEvent ", "  :: ");

        for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
            // Snapshot of the changed document
            DocumentSnapshot snapshot = change.getDocument();
            if (existAirportInArrayDestination(snapshot)) {
                switch (change.getType()) {
                    case ADDED:
                        onDocumentAdded(change);
                        break;
                    case MODIFIED:
                        onModified();
//                        onDocumentModified(change);
                        Log.e("document changed", "65466456");
                        break;
                    case REMOVED:
                        onDelete();
//                        onDocumentRemoved(change);
                        break;
                }
//            }else
                Log.e("elseFiresto", " // " + snapshot.getData().toString());
            }

        }
        onDataChanged();
    }
    protected boolean existAirportInArrayDestination(DocumentSnapshot snapshot){
        String destino = snapshot.getString("destino");

        if (destino!=null && nameAirport!= null){
        String arrayDestino[] = destino.split(",");
            for (int i = 0 ;i<arrayDestino.length ; i++){
//                Log.e("iterateSnapchot", arrayAirportDestination.length+"that is add " + arrayAirportDestination[i]);

                    if (nameAirport.equals(arrayDestino[i])){
                        Log.e("YESINLISTt", arrayDestino[i]+ "/"+ nameAirport+" size:"+arrayDestino.length);
                        return true;
                    }
                    else {
                        Log.e("YESINLISTnot", arrayDestino[i]+ "/"+ nameAirport+" size:"+arrayDestino.length);

//                    Log.e("YESINLIST", arrayDestino[j]+ "/"+ arrayAirportDestination[i]);
                }
            }
        }else{
            return false;

        }

        return false;
    }

    protected void onDocumentAdded(DocumentChange change) {
//        if (existAirportInArrayDestination(change.getDocument())){
//            mSnapshotsPositions.add(change.getNewIndex());

//        }

        mSnapshots.add(change.getDocument());
        notifyItemInserted(change.getNewIndex());
//        Log.e("onDocumentAdded", "destino is null " +mMapSnapshots.get(change.getNewIndex()).getId());
        /*else{
            if (mMapSnapshots.containsKey(change))
        }*/

//        mSnapshots.add(change.getNewIndex(), change.getDocument());
//        notifyItemInserted(change.getNewIndex());
    }

    protected void onDocumentModified(DocumentChange change) {
        /*if (change.getOldIndex() == change.getNewIndex() && mSnapshots.contains(change.getOldIndex())) {
            DocumentSnapshot doc = change.getOldIndex();
            // Item changed but remained in same position
            mSnapshots.set(change.getOldIndex(), change.getDocument());
            notifyItemChanged(change.getOldIndex());
        } else {*/
            // Item changed and changed position
            mSnapshots.remove(change.getOldIndex());
            mSnapshots.add(change.getNewIndex(), change.getDocument());
            notifyItemMoved(change.getOldIndex(), change.getNewIndex());
        /**/

//        Log.e("onDocumentModified ","  "+mSnapshots.get(change.getNewIndex()));

    }

    protected void onDocumentRemoved(DocumentChange change) {
       /* mSnapshots.remove(change.getOldIndex());
        notifyItemRemoved(change.getOldIndex());*/
        if (mSnapshots.contains(change.getOldIndex())) {
            mSnapshots.remove(change.getOldIndex());
            notifyItemRemoved(change.getOldIndex());
        }else
            Log.e("onDocumentRemnotCon","  "+mSnapshots.get(change.getOldIndex()));

    }

}
