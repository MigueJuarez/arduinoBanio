package com.example.arduinobanio.recyclerView.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arduinobanio.R;
import com.example.arduinobanio.recyclerView.BanioItem;

import java.util.List;

public class BanioAdapter extends RecyclerView.Adapter<BanioViewHolder> {

    private final List<BanioItem> banioList;

    public BanioAdapter(List<BanioItem> banioList){
        this.banioList = banioList;
    }

    @NonNull
    @Override
    public BanioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new BanioViewHolder(layoutInflater.inflate(R.layout.item_banio, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BanioViewHolder holder, int position) {
        BanioItem banioItem = banioList.get(position);
        holder.render(banioItem);
    }

    @Override
    public int getItemCount() {
        return banioList.size();
    }
}
