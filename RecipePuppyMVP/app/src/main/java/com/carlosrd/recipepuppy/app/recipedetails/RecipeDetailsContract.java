package com.carlosrd.recipepuppy.app.recipedetails;

import com.carlosrd.recipepuppy.data.models.PuppyRecipesResult;

public interface RecipeDetailsContract {

    interface View {

        void showRecipeName(String name);

        void showRecipeIngredients(String ingredients);

        void showRecipeURL(String recipeURL);

        void showRecipeImage(String imageURL);

        void browseRecipeURL(String url);
    }

    interface Presenter {

        void loadRecipeDetails(PuppyRecipesResult recipe);

        void openRecipeURL();

        void onDestroy();

    }

}
