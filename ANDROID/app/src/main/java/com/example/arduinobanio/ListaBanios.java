package com.example.arduinobanio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.arduinobanio.recyclerView.BanioItem;
import com.example.arduinobanio.recyclerView.adapter.BanioAdapter;

import java.util.Arrays;
import java.util.List;

public class ListaBanios extends AppCompatActivity {

    List<BanioItem> banios = Arrays.asList(
            new BanioItem("Baño Central", "Limpio"),
            new BanioItem("Baño 1 Piso", "Muy Sucio"),
            new BanioItem("Baño 2 Piso", "Sucio"),
            new BanioItem("Baño 3 Piso", "Sucio"),
            new BanioItem("Baño 4 Piso", "Muy Sucio")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_banios);
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvListaBanios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BanioAdapter(banios));
    }

    public void goToWelcome(View view) {
        Intent goToHome = new Intent(this, Welcome.class);
        startActivity(goToHome);
    }

    public void goToDetail(View view) {
        Intent goToDetail = new Intent(this, BanioDetalle.class);
        startActivity(goToDetail);
    }
}