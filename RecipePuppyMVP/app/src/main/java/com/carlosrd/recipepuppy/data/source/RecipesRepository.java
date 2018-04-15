package com.carlosrd.recipepuppy.data.source;

import com.carlosrd.recipepuppy.data.models.PuppyRecipes;

public class RecipesRepository implements RecipesDataSource {

    private static RecipesRepository INSTANCE;

    private final RecipesDataSource mSportsRemoteDataSource;

    private PuppyRecipes mCachedRecipes;

    // Prevent direct instantiation
    private RecipesRepository(RecipesDataSource sportsRemoteDataSource) {

        this.mSportsRemoteDataSource = sportsRemoteDataSource;

    }

    public static RecipesRepository getInstance(RecipesDataSource tasksRemoteDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new RecipesRepository(tasksRemoteDataSource);
        }
        return INSTANCE;

    }

    @Override
    public void loadRecipes(String query, final LoadRecipesCallback callback) {

        mSportsRemoteDataSource.loadRecipes(query, new LoadRecipesCallback() {
            @Override
            public void onRecipesLoaded(PuppyRecipes recipesList) {
                refreshCache(recipesList);
                callback.onRecipesLoaded(recipesList);
            }

            @Override
            public void onError(int statusCode) {
                callback.onError(statusCode);
            }

        });

    }

    private void refreshCache(PuppyRecipes recipesList) {

        mCachedRecipes = recipesList;

    }

}
