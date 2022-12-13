package com.example.proyectoemisoras;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoemisoras.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    Context context;
    ExoPlayer player;
    String emisora = "https://radio.veracruzstereo.com:20000/stream";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_emisora, R.id.nav_dormir, R.id.nav_info)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        comenzarServicio();

        player = new ExoPlayer.Builder(MainActivity.this).build();
        reproductor();
    }

    public void reproductor(){

        //Se establece el origen del audio a reproducir.
        MediaItem mediaItem = MediaItem.fromUri(emisora);
        // Se establece el elemento multimedia que se reproducirá.
        player.setMediaItem(mediaItem);
        // Se prepara el reproductor.
        player.prepare();
        player.setVolume(1.0f);
        // Se inicia la reproduccion.
        player.play();
    }

    //---------------------------------------------------------------------------------
    //Método para mostrar y ocultar el menú lateral derecho
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //---------------------------------------------------------------------------------


    //---------------------------------------------------------------------------------
    //Método para asignar Items al menu lateral derecho
    public boolean onOptionsItemSeleted(MenuItem item){
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    //---------------------------------------------------------------------------------

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void comenzarServicio(){

        Intent serviceIntent = new Intent(this,ServicioAudio.class);
        this.startService(serviceIntent);
    }

    public void detenerServicio(){

        Intent serviceIntent = new Intent(this,ServicioAudio.class);
        this.stopService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detenerServicio();
        if (player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }

    }

}