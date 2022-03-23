package com.whatsapp.API;

import com.whatsapp.Model.AuthenticateModel;
import com.whatsapp.Model.AuthenticateResultModel;
import com.whatsapp.Model.DeviceCreateModel;
import com.whatsapp.Model.RefreshAuthenticationModel;
import com.whatsapp.Model.SensorDataCreateModel;
import com.whatsapp.Model.SensorsModel;
import com.whatsapp.Model.UserGroupCreateModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BilicraAPI {
    String BASE_URL = "http://91.191.173.185:21021/api/";

    @POST("TokenAuth/Authenticate")
    Call<ResponseBody> userLogin(@Body AuthenticateModel authenticateModel);

    // Burada Call içerisinde
    // Call<List<AuthanticateResultModel>> yerine sadece
    // Call<AuthanticateResultModel> olmasının sebebi
    // Dönen cevabın bir JSON dizisi değil,JSON Objesi olmasından dolayıdır.

    @POST("TokenAuth/Authenticate")
    Call<AuthenticateResultModel> userLogin1(@Body AuthenticateModel authenticateModel);

    @POST("services/app/Device/Create")
    Call<ResponseBody> deviceCreate(@Body DeviceCreateModel deviceCreateModel);

    @GET("services/app/Device/GetAll")
    Call<ResponseBody> getAllDevice();

    @POST("services/app/Sensor/Create")
    Call<ResponseBody> sensorCreate(@Body SensorsModel sensorsModel);

    @POST("services/app/SensorData/Create")
    Call<ResponseBody> sensorDataCreate(@Body SensorDataCreateModel sensorDataCreateModel);

    @GET("/api/services/app/Sensor/GetAll")
    Call<ResponseBody> sensorGetAll(@Query("DeviceId") int deviceId);

    @GET("services/app/User/Get")
    Call<ResponseBody> userGet(@Query("EmailAddress") String emailAddress);

    @GET("services/app/Session/GetCurrentLoginInformations")
    Call<ResponseBody> getCurrentLoginInformations();

    @POST("services/app/UserGroup/Create")
    Call<ResponseBody> userGroupCreate(@Body UserGroupCreateModel userGroupCreateModel);

    @DELETE("services/app/Device/Delete")
    Call<ResponseBody> deleteDevice(@Query("Id") int id);

    @GET("services/app/UserGroup/GetAll")
    Call<ResponseBody> getAllUserGroup();

    @DELETE("services/app/UserGroup/Delete")
    Call<ResponseBody> deleteUserGroup(@Query("Id") int id);

    @POST("TokenAuth/RefreshAuthentication")
    Call<ResponseBody> refreshAuthentication(@Body RefreshAuthenticationModel refreshAuthenticationModel);


}
