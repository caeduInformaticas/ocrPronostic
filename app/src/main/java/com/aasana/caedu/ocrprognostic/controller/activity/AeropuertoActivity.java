package com.aasana.caedu.ocrprognostic.controller.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aasana.caedu.ocrprognostic.R;

public class AeropuertoActivity extends AppCompatActivity  {

    public static final String NOMBRE_AEROPUERTO = "AeropuertoActivity.NOMBRE_AEROPUERTO";
    public static final String NOMBRE_DOMINIO = "AeropuertoActivity.NOMBRE_DOMINIO";
    TextView insertNameAeropuerto =null;
    EditText EditNameAeropuerto = null;
    TextView insertDominioAeropuerto =null;
    EditText EditNameDominio= null;
    Button btnSendName = null;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_select_source);
        insertNameAeropuerto = (TextView) findViewById(R.id.inserte_nombre_aeropuerto);
        EditNameAeropuerto = (EditText) findViewById(R.id.nombre_aeropuerto);
        insertDominioAeropuerto = (TextView) findViewById(R.id.inserte_dominio);
        EditNameDominio = (EditText) findViewById(R.id.dominio_perteneciente);
        context=this;
        btnSendName = (Button) findViewById(R.id.btn_send);
        btnSendName.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                String torre = EditNameAeropuerto.getText().toString();
                String dominio = EditNameDominio.getText().toString();
                if (!torre.isEmpty() && !dominio .isEmpty()) {
                    Intent nextScreen = new Intent(getBaseContext(), FragmentContainerActivity.class);
                    nextScreen.putExtra(NOMBRE_AEROPUERTO, torre);
                    nextScreen.putExtra(NOMBRE_DOMINIO, dominio);
                    startActivity(nextScreen);

                }
                /*if (!torre.isEmpty() && !dominio .isEmpty()) {
                    mDomainAndTorre.setDomainAndTorre(dominio,torre);

                    Intent notification = new Intent(getBaseContext(), NotificationActivity.class);
                    notification.putExtra(NOMBRE_AEROPUERTO, torre);
                    notification.putExtra(NOMBRE_DOMINIO, dominio);
                    startActivity(notification);
                }*/
            }
        });

    }

}
