package com.whatsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.whatsapp.Adapter.SharingListAdapter;
import com.whatsapp.Client.RetrofitClient;
import com.whatsapp.ViewModel.BilicraApiViewModel;
import com.whatsapp.databinding.ActivitySharingListBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharingListActivity extends AppCompatActivity {

    private ActivitySharingListBinding activitySharingListBindingbinding;
    BilicraApiViewModel bilicraApiViewModel;
    SharingListAdapter sharingListAdapter;
    ArrayList<String> emailAddresses;
    ArrayList<Integer> userIdFromUserGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySharingListBindingbinding = ActivitySharingListBinding.inflate(getLayoutInflater());
        View view = activitySharingListBindingbinding.getRoot();
        setContentView(view);
        emailAddresses = new ArrayList<String>();
        userIdFromUserGroup = new ArrayList<Integer>();
        bilicraApiViewModel = new ViewModelProvider(SharingListActivity.this).get(BilicraApiViewModel.class);
        activitySharingListBindingbinding.progressBarSharingList.setVisibility(View.VISIBLE);


        activitySharingListBindingbinding.imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        //RecyclerView - Adapter
        activitySharingListBindingbinding.sharingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharingListAdapter = new SharingListAdapter(SharingListActivity.this,emailAddresses,userIdFromUserGroup);
        activitySharingListBindingbinding.sharingListRecyclerView.setAdapter(sharingListAdapter);

        //getAllUserGroup();
         getAllUserGroupFromAPI();


    }

    private void getAllUserGroupFromAPI(){
        bilicraApiViewModel.getAllUserGroup();
        bilicraApiViewModel.getAllUserGroupObserver().observe(SharingListActivity.this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if(responseBody==null){
                    System.out.println("getAllUserGroupFromAPI Başarısız");
                    activitySharingListBindingbinding.progressBarSharingList.setVisibility(View.INVISIBLE);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        System.out.println("GetAllUserGroup: "+jsonObject);

                        if(jsonObject.getJSONObject("result").getJSONArray("items").length()==0){
                            activitySharingListBindingbinding.progressBarSharingList.setVisibility(View.INVISIBLE);
                        }else{
                            for(int i =0 ; i<jsonObject.getJSONObject("result").getJSONArray("items").length(); i++){
                                System.out.println("getAllUserGroupEmailAdresses "+  jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress"));
                                int userIdUserGroup = jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getInt("id");
                                String apiEmailAddresses = jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress");
                                activitySharingListBindingbinding.progressBarSharingList.setVisibility(View.INVISIBLE);
                                activitySharingListBindingbinding.sharingListRecyclerView.setVisibility(View.VISIBLE);
                                emailAddresses.add(apiEmailAddresses);
                                userIdFromUserGroup.add(userIdUserGroup);
                                sharingListAdapter.notifyDataSetChanged();

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }













    public void getAllUserGroup(){

        Call<ResponseBody> getAllUserGroup = RetrofitClient.getInstance().getMyApi().getAllUserGroup();
        getAllUserGroup.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    System.out.println("UserGroupGetAll Başarılı");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        System.out.println("GetAllUserGroup: "+jsonObject);



                        for(int i =0 ; i<jsonObject.getJSONObject("result").getJSONArray("items").length(); i++){
                            System.out.println("getAllUserGroupEmailAdresses "+  jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress"));
                            int userIdUserGroup = jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getInt("id");
                            String apiEmailAddresses = jsonObject.getJSONObject("result").getJSONArray("items").getJSONObject(i).getJSONArray("userGroupUsers").getJSONObject(0).getJSONObject("user").getString("emailAddress");
                            activitySharingListBindingbinding.progressBarSharingList.setVisibility(View.INVISIBLE);
                            activitySharingListBindingbinding.sharingListRecyclerView.setVisibility(View.VISIBLE);
                            emailAddresses.add(apiEmailAddresses);
                            userIdFromUserGroup.add(userIdUserGroup);
                            sharingListAdapter.notifyDataSetChanged();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    System.out.println("UserGroupGetAll Başarısız");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("UserGroupGetAll Fail");
            }
        });



    }




}