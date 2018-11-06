package com.aasana.caedu.ocrprognostic.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.aasana.caedu.ocrprognostic.R;
import com.aasana.caedu.ocrprognostic.controller.fragments.MensajeFragment;
import com.aasana.caedu.ocrprognostic.controller.fragments.ValorMensajeFragment;


public class FragmentContainerActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    String saved;
    private String domain = "domai";
    private String torre = "torre";
    private String nameDominio= null;
    private String nameAeropuerto = null;

    //ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container );
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        nameAeropuerto = getIntent().getExtras().getString(AeropuertoActivity.NOMBRE_AEROPUERTO);
        nameDominio= getIntent().getExtras().getString(AeropuertoActivity.NOMBRE_DOMINIO);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        setupNavigationDrawerContent(navigationView);
        //First start (Inbox Fragment)
        /*FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = setFragment(0);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }*/

    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_notification:
                                menuItem.setChecked(true);
                                setFragment(0);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_message_vuelos:
                                menuItem.setChecked(true);
//                                setFragment(1);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_pronostico:
                                menuItem.setChecked(true);
                                setFragment(2);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_about:
                                menuItem.setChecked(true);
                                Toast.makeText(FragmentContainerActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.item_navigation_drawer_help_and_feedback:
                                menuItem.setChecked(true);
                                Toast.makeText(FragmentContainerActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }

    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        Bundle arguments = new Bundle();

        switch (position) {
            case 0:
                Intent notification = new Intent(getBaseContext(), NotificationActivity.class);
                notification.putExtra("TORRE", nameAeropuerto);
                notification.putExtra("DOMAIN", nameDominio);
                Log.e("case0", "MensajeFragment "+nameAeropuerto);
                startActivity(notification);

                break;
            case 2:
//                arguments.putSerializable(MensajeFragment.ARG_WINE, getIntent().getSerializableExtra(EXTRA_WINE));
                arguments.putString("TORRE",nameAeropuerto);
                arguments.putString("DOMAIN",nameDominio);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MensajeFragment pronostic = new MensajeFragment();
                pronostic.setArguments(arguments);
                fragmentTransaction.replace(R.id.fragment_container, pronostic);
                fragmentTransaction.commit();
                Log.e("case2", "MensajeFragment "+nameAeropuerto);
                break;
        }

    }
}
