package com.example.arduinobanio.thread;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arduinobanio.recyclerView.BanioItem;
import com.example.arduinobanio.recyclerView.adapter.BanioAdapter;

import java.util.Arrays;
import java.util.List;

public class ThreadAsyncTaskBanioList extends AsyncTask {

    List<BanioItem> banios = Arrays.asList(
            new BanioItem("Planta Baja", "Sucio"),
            new BanioItem("Primer Piso", "Muy Sucio"),
            new BanioItem("Segundo Piso", "Limpio"),
            new BanioItem("Tercer Piso", "Sucio"),
            new BanioItem("Terraza", "Limpio"),
            new BanioItem("Patio Comidas", "Muy Sucio"),
            new BanioItem("Cuarto Piso", "Muy Sucio")
    );

    @Override
    protected Object doInBackground(Object[] objects) {

        RecyclerView recyclerView = (RecyclerView) objects[0];
        recyclerView.setLayoutManager(new LinearLayoutManager((Context) objects[1]));
        recyclerView.setAdapter(new BanioAdapter(banios));
        return recyclerView;
    }
}
