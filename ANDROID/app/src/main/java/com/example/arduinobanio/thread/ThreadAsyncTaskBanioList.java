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
            new BanioItem("Baño Central", "Limpio"),
            new BanioItem("Baño 1 Piso", "Muy Sucio"),
            new BanioItem("Baño 2 Piso", "Sucio"),
            new BanioItem("Baño 3 Piso", "Sucio"),
            new BanioItem("Baño 4 Piso", "Muy Sucio")
    );

    @Override
    protected Object doInBackground(Object[] objects) {

        RecyclerView recyclerView = (RecyclerView) objects[0];
        recyclerView.setLayoutManager(new LinearLayoutManager((Context) objects[1]));
        recyclerView.setAdapter(new BanioAdapter(banios));
        return recyclerView;
    }
}
