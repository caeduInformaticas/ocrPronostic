package com.aasana.caedu.ocrprognostic.controller.adapter;


import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aasana.caedu.ocrprognostic.R;
import com.aasana.caedu.ocrprognostic.controller.activity.AeropuertoActivity;
import com.aasana.caedu.ocrprognostic.controller.activity.NotificationActivity;
import com.aasana.caedu.ocrprognostic.model.Mensaje;
import com.aasana.caedu.ocrprognostic.model.Message;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotifyAdapter extends FirestoreAdapter<NotifyAdapter.ViewHolder>{

    private OnMesaggeSelectedListener mListener;
//    private String[] arrayDestin ;
    public NotifyAdapter(Query query, OnMesaggeSelectedListener listener, String nameAirport) {
        super(query,nameAirport);



//        this.arrayDestin =arrayDestin;
        mListener = listener;
//        Log.e("Notify adapter", getmSnapshots().toString()+"   onmessaggeSelectListener " +listener.getClass().toString() );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Log.e("onCreateViewHolder", " parent "+parent.getClass().toString());
        return new ViewHolder(inflater.inflate(R.layout.message_list_row, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);

        /*if (getmMapSnapshots().containsKey(position)) {
        }
        else
            Log.e("onbindnot found"," position"+position);
*/
        /*}else {
            Log.e("elseSnapshotsPosition", getmSnapshots().get(position).toString() );
//            bindViewHolder(holder,position);
//            getmSnapshots().remove(position);
//            notifyItemRemoved(position);
        }*/
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    /*@Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (!getmSnapshotsPosition().contains(position)) {
            getmSnapshots().remove(position);
            notifyItemRemoved(position);
            Log.e("paySnapshotsPosition"," position"+position);

        }

        }*/

    public interface  OnMesaggeSelectedListener{
        void onMessageSelect(DocumentSnapshot message);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_row)
        TextView title_valor;
        @BindView(R.id.source_message)
        TextView source;
        @BindView(R.id.prioridad_message)
        TextView priority;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Log.e("ViewHolderButter", "item view  "+this.title_valor);
        }
        public void remove(){

        }
        public void bind(final DocumentSnapshot snapshot,
                         final OnMesaggeSelectedListener listener) {
            // Message message = snapshot.toObject(Message.class);
//            Resources resources = itemView.getResources();
            String idDateSnapshot = snapshot.getId();

            if (idDateSnapshot.length() > 20) {
                String idSnapshot = idDateSnapshot.substring(0, idDateSnapshot.length() - 19);
                String stringDate = idDateSnapshot.substring(idDateSnapshot.length() - 19,idDateSnapshot.length());
//                Log.e("iflentidcorto",stringDate+" "+idSnapshot);

                title_valor.setText(idSnapshot);
                source.setText(stringDate);
                priority.setText(snapshot.getString("prioridad"));
                listener.onMessageSelect(snapshot);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onMessageSelect(snapshot);
                    }
                }
            });

            }else
                Log.e("elselentidcorto","finalize()");
        }
    }
}
