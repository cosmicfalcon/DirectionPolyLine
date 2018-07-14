package com.example.l.map2.Service;

import com.example.l.map2.PolylineResponse;
import com.example.l.map2.data.DirectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface EndPoint {
    @GET()
    public Call<DirectionResponse> getDirectionData(@Url String url);

    @GET()
    public Call<PolylineResponse> getPolylineData(@Url String url);
}
