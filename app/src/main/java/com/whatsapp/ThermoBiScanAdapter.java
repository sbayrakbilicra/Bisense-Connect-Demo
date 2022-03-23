package com.whatsapp;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whatsapp.databinding.ThermobiScanRecyclerRowBinding;

import java.util.ArrayList;

public class ThermoBiScanAdapter extends RecyclerView.Adapter<ThermoBiScanAdapter.ThermoBiScanHolder> {

    private Activity activity;


    public static int devicePosition;
    ArrayList<BluetoothDevice> bledevicesList;

    public ThermoBiScanAdapter(ArrayList<BluetoothDevice> bledevicesList,Activity activity) {
        this.bledevicesList = bledevicesList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ThermoBiScanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ThermobiScanRecyclerRowBinding thermobiScanRecyclerRowBinding = ThermobiScanRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ThermoBiScanHolder(thermobiScanRecyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ThermoBiScanHolder holder, int position) {
        holder.binding.ThermoBiRecyclerViewTextView.setText(bledevicesList.get(position).getAddress());
        holder.binding.ThermoBiRecyclerViewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePosition = position;
                Intent intent = new Intent(holder.itemView.getContext(), AfterScanActivity.class);
                holder.itemView.getContext().startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bledevicesList.size();
    }

    public class ThermoBiScanHolder extends RecyclerView.ViewHolder{

       private ThermobiScanRecyclerRowBinding binding;

        public ThermoBiScanHolder(ThermobiScanRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
