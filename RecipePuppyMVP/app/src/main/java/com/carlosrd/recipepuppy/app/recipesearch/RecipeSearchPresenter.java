package com.carlosrd.recipepuppy.app.recipesearch;

import com.carlosrd.recipepuppy.data.source.RecipesDataSource;
import com.carlosrd.recipepuppy.data.source.RecipesRepository;
import com.carlosrd.recipepuppy.data.models.PuppyRecipes;
import com.carlosrd.recipepuppy.data.models.PuppyRecipesResult;

public class RecipeSearchPresenter implements
        RecipeSearchContract.Presenter,
        RecipesDataSource.LoadRecipesCallback {

    // ATRIBUTOS
    // ***********************************************

    private RecipesRepository mRecipesData;
    private RecipeSearchContract.View mRecipesView;

    private String mCurrentQueryText;

    // CONSTRUCTORAS
    // ***********************************************

    public RecipeSearchPresenter(RecipesRepository repository, RecipeSearchContract.View view) {

        mRecipesData = repository;
        mRecipesView = view;

        mCurrentQueryText = "";

    }

    @Override
    public void loadRecipes(String query) {

        if (!query.trim().equals(""))
            mRecipesData.loadRecipes(query, this);
        else
            mRecipesView.showNoResults();

        mCurrentQueryText = query;

    }

    @Override
    public void onDestroy() {

        mRecipesData = null;
        mRecipesView = null;

    }

    @Override
    public void openRecipeDetails(PuppyRecipesResult puppyRecipe) {
        if (mRecipesView != null)
            mRecipesView.showRecipeDetails(puppyRecipe);
    }

    @Override
    public void setCurrentQuery(String currentQueryText) {
            mCurrentQueryText = currentQueryText;
    }

    @Override
    public String getCurrentQuery() {
        return mCurrentQueryText;
    }

    @Override
    public void recoverCurrentQuery() {

        if (mRecipesView != null) {
            if (!mCurrentQueryText.equals(""))
                mRecipesView.setCurrentQuery(mCurrentQueryText);
        }

    }


    // CALLBACKS RecipeRepository
    // ***********************************************

    @Override
    public void onRecipesLoaded(PuppyRecipes recipesList) {

        if (recipesList == null || recipesList.getResults().size() == 0)
            mRecipesView.showNoResults();
        else
            mRecipesView.showRecipes(recipesList);

    }

    @Override
    public void onError(int statusCode) {
        mRecipesView.showError(statusCode);
    }

}
