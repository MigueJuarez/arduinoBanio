package com.example.arduinobanio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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
            new BanioItem("BAÑO CENTRAL", "LIMPIO", "![](../../../../res/drawable-v24/banio.png)"),
            new BanioItem("BAÑO 1 Piso", "SUCIO", "![](../../../../res/drawable-v24/banio.png)"),
            new BanioItem("BAÑO 2 Piso", "SUCIO", "![](../../../../res/drawable-v24/banio.png)"),
            new BanioItem("BAÑO 3 Piso", "SUCIO", "![](../../../../res/drawable-v24/banio.png)"),
            new BanioItem("BAÑO 4 Piso", "SUCIO", "![](../../../../res/drawable-v24/banio.png)")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_banios);
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(this, manager.getOrientation());
        RecyclerView recyclerView = findViewById(R.id.rvListaBanios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BanioAdapter(banios));
        // recyclerView.addItemDecoration(decoration);
    }

    public void goToWelcome(View view) {
        Intent goToHome = new Intent(this, Welcome.class);
        startActivity(goToHome);
    }

    public void goToDetail(View view) {
        Intent goToDetail = new Intent(this, BanioDetalle.class);
        startActivity(goToDetail);
    }

    public void buscar(View view) {

    }
}