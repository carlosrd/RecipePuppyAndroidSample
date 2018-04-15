package com.carlosrd.recipepuppy.data.source;

import com.carlosrd.recipepuppy.data.models.PuppyRecipes;

import java.util.ArrayList;

/**
 * Punto de entrada de datos principal para las Recetas.
 * Incluye callbacks para recoger la información
 */
public interface RecipesDataSource {

    interface LoadRecipesCallback {

        void onRecipesLoaded(PuppyRecipes recipesList);

        void onError(int statusCode);

    }

    void loadRecipes(String query, LoadRecipesCallback callback);

}