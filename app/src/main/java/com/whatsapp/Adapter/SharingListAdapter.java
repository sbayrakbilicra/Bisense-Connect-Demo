package com.whatsapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.whatsapp.Client.RetrofitClient;
import com.whatsapp.databinding.SharingListRecyclerRowBinding;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharingListAdapter extends RecyclerView.Adapter<SharingListAdapter.SharingListHolder> {

    private Activity activity;
    private ArrayList<String> emailAddresses;
    private ArrayList<Integer> userIdFromUserGroup;

    public SharingListAdapter(Activity activity, ArrayList<String> emailAddresses,ArrayList<Integer> userIdFromUserGroup) {
        this.activity = activity;
        this.emailAddresses = emailAddresses;
        this.userIdFromUserGroup = userIdFromUserGroup;
    }

    @NonNull
    @Override
    public SharingListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SharingListRecyclerRowBinding sharingListRecyclerRowBinding = SharingListRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new SharingListHolder(sharingListRecyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SharingListHolder holder, int position) {
        holder.binding.sharingListRecyclerRow.setText(emailAddresses.get(position));
        holder.binding.sharingListIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked Icon");

                AlertDialog.Builder builder = new AlertDialog.Builder(activity,AlertDialog.THEME_HOLO_DARK);
                builder.setTitle("Kullanıcıyı Sil");
                builder.setMessage("Kullanıcıyı gruptan silmek istiyor musunuz?");
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUserFromUserGroup(userIdFromUserGroup.get(position));

                        //Refresh activity when click delete icon
                        activity.finish();
                        activity.startActivity(activity.getIntent());
                    }
                });
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return emailAddresses.size();
    }

    public class SharingListHolder extends RecyclerView.ViewHolder{

        private SharingListRecyclerRowBinding binding;
        public SharingListHolder(SharingListRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    public void deleteUserFromUserGroup(int id){
        Call<ResponseBody> deleteUserFromUserGroup = RetrofitClient.getInstance().getMyApi().deleteUserGroup(id);
        deleteUserFromUserGroup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("DeleteUserFromUserGroup Başarılı");
                }else{
                    System.out.println("DeleteUserFroumUserGroup Başarısız");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("DeleteUserFroumUserGroup Fail");
            }
        });

    }







}
