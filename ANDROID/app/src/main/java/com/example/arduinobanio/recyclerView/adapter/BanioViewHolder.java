package com.example.arduinobanio.recyclerView.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arduinobanio.R;
import com.example.arduinobanio.recyclerView.BanioItem;

public class BanioViewHolder extends RecyclerView.ViewHolder {

    TextView banioNombre = itemView.findViewById(R.id.tvItemBanioNombre);
    TextView banioEstado = itemView.findViewById(R.id.tvItemBanioEstado);

    public BanioViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void render(BanioItem banioItemModel) {
        banioNombre.setText(banioItemModel.getName());
        banioEstado.setText(banioItemModel.getStatus());
    }
}
