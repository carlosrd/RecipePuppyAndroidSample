package com.carlosrd.recipepuppy.data.source.remote;

import com.carlosrd.recipepuppy.data.models.PuppyRecipes;
import com.carlosrd.recipepuppy.data.source.RecipesDataSource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementacion de fuente de datos remota que conecta con la API
 */
public class RecipesRemoteDataSource implements RecipesDataSource {

    private static RecipesRemoteDataSource INSTANCE;

    public static RecipesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private RecipesRemoteDataSource() {}

    @Override
    public void loadRecipes(String query, final LoadRecipesCallback callback) {

        String queryEncoded = encodeQuery(query);

        // Create a very simple REST adapter which points the GitHub API endpoint.
        RecipePuppyAPI client = APIClient.createService(RecipePuppyAPI.API_BASE_URL, RecipePuppyAPI.class);

        Map<String, String> options = new HashMap<>();
        options.put("q", queryEncoded);
        options.put("p", "1");

        // Hacer llamada API de las recetas
        Call<PuppyRecipes> call = client.getPuppyRecipes(options);

        // Encolar petición en segundo plano
        call.enqueue(new Callback<PuppyRecipes>() {
            @Override
            public void onResponse(Call<PuppyRecipes> call, Response<PuppyRecipes> response) {
                // Llamada exitosa y con respuesta
                if (response.isSuccessful()) {

                    PuppyRecipes recipesList = response.body();

                    callback.onRecipesLoaded(recipesList);

                } else {
                    callback.onError(response.code());
                }

            }

            @Override
            public void onFailure(Call<PuppyRecipes> call, Throwable t) {
                // Fallo en la llamada de red
                callback.onError(-1);

            }

        });

    }

    /**
     * Codifica los caracteres de la consulta para que puedan ser enviados en la URL
     */
    private String encodeQuery(String query){

        String queryEncoded = null;

        try {
            queryEncoded = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return queryEncoded;

    }
}
