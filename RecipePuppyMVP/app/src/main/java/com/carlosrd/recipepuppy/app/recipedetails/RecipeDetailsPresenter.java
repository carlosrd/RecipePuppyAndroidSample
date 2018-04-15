package com.carlosrd.recipepuppy.app.recipedetails;

import com.carlosrd.recipepuppy.data.source.RecipesRepository;
import com.carlosrd.recipepuppy.data.models.PuppyRecipesResult;

public class RecipeDetailsPresenter implements RecipeDetailsContract.Presenter {

    private PuppyRecipesResult recipe;

    // ATRIBUTOS
    // ***********************************************

    private RecipesRepository mRecipesData;
    private RecipeDetailsContract.View mRecipeDetailsView;

    // CONSTRUCTORAS
    // ***********************************************

    public RecipeDetailsPresenter(RecipesRepository repository, RecipeDetailsContract.View view) {

        mRecipesData = repository;
        mRecipeDetailsView = view;

    }

    @Override
    public void loadRecipeDetails(PuppyRecipesResult recipe) {

        this.recipe = recipe;

        if (mRecipeDetailsView != null) {

            mRecipeDetailsView.showRecipeImage(recipe.getImageURL());
            mRecipeDetailsView.showRecipeName(recipe.getName());
            mRecipeDetailsView.showRecipeIngredients(recipe.getIngredients());
            mRecipeDetailsView.showRecipeURL(recipe.getURL());

        }

    }

    @Override
    public void openRecipeURL() {

        if (mRecipeDetailsView != null)
            mRecipeDetailsView.browseRecipeURL(recipe.getURL());

    }

    @Override
    public void onDestroy() {

        mRecipeDetailsView = null;
        mRecipesData = null;

    }

}
