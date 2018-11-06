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
import com.aasana.caedu.ocrprognostic.controller.activity.NotificationActivity;
import com.aasana.caedu.ocrprognostic.model.Mensaje;
import com.aasana.caedu.ocrprognostic.model.Message;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotifyAdapter extends FirestoreAdapter<NotifyAdapter.ViewHolder>{

    private OnMesaggeSelectedListener mListener;

    public NotifyAdapter(Query query, OnMesaggeSelectedListener listener) {
        super(query);
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
    }
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
            Log.e("ViewHolder", "item view  "+this.getItemId());

        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnMesaggeSelectedListener listener) {
           // Message message = snapshot.toObject(Message.class);
//            Resources resources = itemView.getResources();
            title_valor.setText(snapshot.getString("mMessage"));
            source.setText(snapshot.getString("mSource"));
            priority.setText(snapshot.getString("mPriority"));
            listener.onMessageSelect(snapshot);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onMessageSelect(snapshot);
                    }
                }
            });*/

        }
    }
}

/*extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    private List<Mensaje> mensajeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_row);
            genre = (TextView) view.findViewById(R.id.genre);
//            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public NotifyAdapter(List<Mensaje> mensajeList) {
        this.mensajeList = mensajeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Mensaje mensaje = mensajeList.get(position);
        holder.title.setText(mensaje.getNombreCampo());
        holder.genre.setText(mensaje.getValor());
//        holder.year.setText(mensaje.getYear());
    }

    @Override
    public int getItemCount() {
        return mensajeList.size();
    }
}*/