package com.carlosrd.recipepuppy.data.source.remote;

import com.carlosrd.recipepuppy.data.models.PuppyRecipes;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RecipePuppyAPI {

    String API_BASE_URL = "http://www.recipepuppy.com";

    // Llamada para consultar recetas
    @GET("/api/")
    Call<PuppyRecipes> getPuppyRecipes(
            @QueryMap Map<String, String> options
    );
}
