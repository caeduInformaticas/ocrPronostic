package com.aasana.caedu.ocrprognostic.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aasana.caedu.ocrprognostic.R;
import com.aasana.caedu.ocrprognostic.controller.fragments.MensajeFragment;
import com.aasana.caedu.ocrprognostic.controller.fragments.VueloFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;


public class NavigateActivity extends AppCompatActivity {

    private static final int IDACTIVITYFORRESULT = 120;
    private static final int READ_BLOCK_SIZE = 100;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private String nameDominio= null;
    private String nameAeropuerto = null;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String[] arrayReadFile;
    public DatesAirport datesAirport;

    //ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container );
        this.toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
//        Intent intent = getIntent();
// Get the extras (if there are any)
        /*Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(AeropuertoActivity.NOMBRE_AEROPUERTO)) {
                nameAeropuerto = getIntent().getExtras().getString(AeropuertoActivity.NOMBRE_AEROPUERTO);
                nameDominio= getIntent().getExtras().getString(AeropuertoActivity.NOMBRE_DOMINIO);
                Log.e("bundleExtras",nameAeropuerto+nameDominio);
            }
        }*/
        /*if (savedInstanceState != null && savedInstanceState.containsKey(AeropuertoActivity.NOMBRE_AEROPUERTO)){
            nameAeropuerto = savedInstanceState.getString(AeropuertoActivity.NOMBRE_AEROPUERTO);
            nameDominio = savedInstanceState.getString(AeropuertoActivity.NOMBRE_DOMINIO);
            Log.e("fragmentContainercreate", "savedInstanceState  "+nameAeropuerto);
        }*/

        readInternalFile();

        initializeNames();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }


        setupNavigationDrawerContent(navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Force onPrepareOptionsMenu() to be called
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Force onPrepareOptionsMenu() to be called
                invalidateOptionsMenu();
            }
        };

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("fragmentConPaused","pause"+nameDominio+nameAeropuerto);
//        saveData();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        loadData();
//        /*readFile();
//        ReadFromFileExternal();*/

        Log.e("fragmentConStart","onstart"+nameDominio+nameAeropuerto);


    }

    private void initializeNames() {
        nameAeropuerto = arrayReadFile[0];
        nameDominio = arrayReadFile[1];
        for (int i=0;i<arrayReadFile.length;i++){
            Log.e("NotifyarrayReadFile","<" +arrayReadFile[i]+">");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred
        actionBarDrawerToggle.syncState();
        Log.e("FragmenCononPostCreate",nameAeropuerto+nameDominio);
    }



    private void saveData() {
        SharedPreferences sp =
                getSharedPreferences("MyPrefs",
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AeropuertoActivity.NOMBRE_DOMINIO,nameDominio);
        editor.putString(AeropuertoActivity.NOMBRE_AEROPUERTO,nameAeropuerto);
        editor.commit();
    }
    private void loadData() {
        SharedPreferences sp =
                getSharedPreferences("MyPrefs",
                        Context.MODE_PRIVATE);
        nameAeropuerto = sp.getString(AeropuertoActivity.NOMBRE_AEROPUERTO, nameAeropuerto);
        nameDominio = sp.getString(AeropuertoActivity.NOMBRE_DOMINIO, nameDominio);
    }


    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
           /* FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
*/
            if (!isNullAirportAndDomain()){
                switch (menuItem.getItemId()) {
                    case R.id.item_notification:
                        menuItem.setChecked(true);
                        setActivity(0);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.item_message_vuelos:
                        menuItem.setChecked(true);
                        setFragment(1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.item_pronostico:
                        menuItem.setChecked(true);
                        setFragment(2);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.select_airports:
                        menuItem.setChecked(true);
                        renewFileByRewrite();
                        setActivity(3);
                        iterateArraySelectAirport();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.update_airport:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }
            }else Log.e("FragmentContainer","isNullAirportAndDomain");

            return true;
            }
        });
    }

    private void renewFileByRewrite() {
        readInternalFile();
        if (arrayReadFile.length>=2){
            try {
                arrayReadFile = null;
                deleteInternalFile();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(nameAeropuerto);
                stringBuilder.append(",");
                stringBuilder.append(nameDominio);
                stringBuilder.append(",");
                FileOutputStream fOut = openFileOutput(AeropuertoActivity.FILENAME,MODE_APPEND);
                fOut.write(stringBuilder.toString().getBytes());
                fOut.close();
                Log.e("NavigationtSaveInternal", " = "+stringBuilder.toString());

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    void deleteInternalFile(){
        try {
            File[] files = getBaseContext().getFilesDir().listFiles();
            if(files != null)
                for(File file : files) {
                    file.delete();
                    Log.e("deleteInternalFile", " files = " + getFilesDir().toString());

                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void readInternalFile(){
        try {
            FileInputStream fileIn = openFileInput(AeropuertoActivity.FILENAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                arrayReadFile = readstring.split(",");
            }
            Log.e("NotifiReadfile",arrayReadFile.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void iterateArraySelectAirport(){
    if (arrayReadFile!=null) {
//        for (String airport : arraySelectAirport){
        for (int i = 0; i < arrayReadFile.length; i++) {
            Log.e("iterateArraySe", " <" + arrayReadFile[i] + " >  : " + i);
        }
        }
    }


    private void setActivity(int position){
        switch (position){
            case 0:
                Intent notification = new Intent(NavigateActivity.this, NotificationActivity.class);
//                notification.putExtra(AeropuertoActivity.NOMBRE_AEROPUERTO, nameAeropuerto);
//                notification.putExtra(AeropuertoActivity.NOMBRE_DOMINIO, nameDominio);
                notification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Log.e("case0", "MensajeFragment "+nameAeropuerto);
                startActivity(notification);
                break;
            case 3:
                Intent nextScreen = new Intent(getApplicationContext(), SelectAirportActivity.class);
                startActivity(nextScreen);
                break;

        }
    }
    private void obtainStringSelectAirport() {

    }



    private void makeToast(String stringToast){
        Toast.makeText(getApplicationContext(),stringToast,Toast.LENGTH_LONG).show();

    }
    public void setFragment(int position) {


        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
//        Bundle arguments = new Bundle();

        switch (position) {
            case 1:
                VueloFragment vuelo = new VueloFragment();
                fragmentTransaction.replace(R.id.fragment_container, vuelo);
                fragmentTransaction.commit();
                Log.e("case2", "MensajeFragment "+nameAeropuerto);
                break;
            case 2:
//              arguments.putSerializable(MensajeFragment.ARG_WINE, getIntent().getSerializableExtra(EXTRA_WINE));
//                arguments.putString(AeropuertoActivity.NOMBRE_AEROPUERTO,nameAeropuerto);
//                arguments.putString(AeropuertoActivity.NOMBRE_DOMINIO,nameDominio);


                MensajeFragment mensajeFragment = new MensajeFragment();
                fragmentTransaction.replace(R.id.fragment_container, mensajeFragment);
                fragmentTransaction.commit();
                Log.e("case2", "MensajeFragment "+mensajeFragment);
                break;
        }

    }

    private boolean isNullAirportAndDomain(){
        return nameAeropuerto== null && nameAeropuerto==null;
    }


    public class DatesAirport{

        private String nameDomain = null;
        private String nameAirport = null;
        private String [] arraySelectDestination = null;

        public DatesAirport(String nameDomain, String nameAirport, String[] arraySelectDestination) {
            this.nameDomain = nameDomain;
            this.nameAirport = nameAirport;
            this.arraySelectDestination = arraySelectDestination;
        }

        public void setNameDomain(String nameDomain) {
            this.nameDomain = nameDomain;
        }

        public void setNameAirport(String nameAirport) {
            this.nameAirport = nameAirport;
        }

        public void setArraySelectDestination(String[] arraySelectDestination) {
            this.arraySelectDestination = arraySelectDestination;
        }

        public String getNameDomain() {
            return nameDomain;
        }

        public String getNameAirport() {
            return nameAirport;
        }

        public String[] getArraySelectDestination() {
            return arraySelectDestination;
        }
    }

}
