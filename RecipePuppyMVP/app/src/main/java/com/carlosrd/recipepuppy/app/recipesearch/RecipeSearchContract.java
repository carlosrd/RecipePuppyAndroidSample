package com.carlosrd.recipepuppy.app.recipesearch;

import com.carlosrd.recipepuppy.data.models.PuppyRecipes;
import com.carlosrd.recipepuppy.data.models.PuppyRecipesResult;

public interface RecipeSearchContract {

    interface View {

        void showRecipes(PuppyRecipes recipesList);

        void showNoResults();

        void showRecipeDetails(PuppyRecipesResult puppyRecipe);

        void showError(int statusCode);

        void setCurrentQuery(String query);

    }

    interface Presenter {

        void loadRecipes(String query);

        void openRecipeDetails(PuppyRecipesResult puppyRecipe);

        void setCurrentQuery(String currentQueryText);

        String getCurrentQuery();

        void recoverCurrentQuery();

        void onDestroy();



    }
}
