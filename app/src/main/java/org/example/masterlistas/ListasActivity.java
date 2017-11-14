package org.example.masterlistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.List;

public class ListasActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener
{
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private FlowingDrawer mDrawer;

    private FirebaseAnalytics analytics;


    private FirebaseRemoteConfig remoteConfig;
    private static final int CACHE_TIME_SECONDS = 30; // 30 segundos para pruebas

    //    private static final int CACHE_TIME_SECONDS = 3600; // 10 HORAS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas);

        analytics = FirebaseAnalytics.getInstance(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.se_presiono_fab, Snackbar.LENGTH_LONG).show();
            }
        });

        //Inicializar los elementos
        List items = new ArrayList();
        items.add(new Lista(R.drawable.trabajo, getString(R.string.trabajo), 2));
        items.add(new Lista(R.drawable.casa, getString(R.string.personal), 3));
        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
        // Crear un nuevo adaptador
        adapter = new ListaAdapter(items, getBaseContext());
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(ListasActivity.this, new
                        RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
//                                Toast.makeText(ListasActivity.this, "" + position,
//                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ListasActivity.this,
                                        DetalleListaActivity.class);
                                intent.putExtra("numeroLista", position);
//                                startActivity(intent);
                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(ListasActivity.this,
                                                new Pair<View, String>(v.findViewById(R.id.imagen),
                                                        getString(R.string.transition_name_img)),
                                                new Pair<View, String>(ListasActivity.this.findViewById(R.id.fab),
                                                        getString(R.string.transition_name_boton)));
                                ActivityCompat.startActivity(ListasActivity.this, intent, options
                                        .toBundle());
                            }
                        })
        );

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        // Navigation Drawer
//        DrawerLayout drawer = (DrawerLayout) findViewById(
//                R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
//                drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = (NavigationView) findViewById(
//                R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        NavigationView navigationView = (NavigationView) findViewById(
                R.id.vNavigation);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.toggleMenu();
            }
        });

        //        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
//                drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
//        drawer.addDrawerListener(toggle);

        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
                                                   @Override
                                                   public void onDrawerStateChange(int oldState, int newState) {
                                                       if (newState == ElasticDrawer.STATE_OPEN) {
                                                           Log.i("MainActivity", "Drawer STATE_OPEN");

                                                           Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "app");
                                                           bundle.putString("accion", "open");
                                                           bundle.putLong("pulsacion_ms", 34);
//                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text/html");
                                                           analytics.logEvent("NavDrawer", bundle);
                                                       }
                                                   }

                                                   @Override
                                                   public void onDrawerSlide(float openRatio, int offsetPixels) {
                                                   }
                                               }
        );


        Transition lista_enter = TransitionInflater.from(this)
                .inflateTransition(R.transition.transition_lista_enter);
        getWindow().setEnterTransition(lista_enter);


        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings config = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        remoteConfig.setConfigSettings(config);
        remoteConfig.setDefaults(R.xml.remote_config);
        remoteConfig.fetch(CACHE_TIME_SECONDS).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ListasActivity.this, R.string.fetch_ok, Toast.LENGTH_SHORT).show();
                    remoteConfig.activateFetched();
                } else {
                    Toast.makeText(ListasActivity.this, R.string.fetch_ko, Toast.LENGTH_SHORT).show();
                }
                boolean navigationAbiertoPrimeraVez = remoteConfig.getBoolean("navigation_drawer_abierto");

                abrePrimeraVez(navigationAbiertoPrimeraVez);
            }
        });


    }

    //
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.nav_1) {
//            // …
//        } else if (id == R.id.nav_2) {
//            // …
//        } else if (id == R.id.nav_3) {
//            // …
//        } // …
//        DrawerLayout drawer = (DrawerLayout) findViewById(
//                R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(
//                R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
    @Override
    public void onBackPressed() {
        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    public void abrePrimeraVez(boolean abrirNavigationDrawerPrimeraVez) {
        SharedPreferences sp = getSharedPreferences("mispreferencias", 0);
        boolean primerAcceso = sp.getBoolean("abrePrimeraVez", true);
        if (primerAcceso) {
            if (abrirNavigationDrawerPrimeraVez) {
                mDrawer.openMenu();
                analytics.setUserProperty( "experimento_nav_drawer", "abierto" );
            } else {
                analytics.setUserProperty( "experimento_nav_drawer", "cerrado" );
            }

            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("abrePrimeraVez", false).commit();
        }
    }
}
