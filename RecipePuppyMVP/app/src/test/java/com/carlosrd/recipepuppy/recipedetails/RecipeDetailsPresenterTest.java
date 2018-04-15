package com.carlosrd.recipepuppy.recipedetails;

import com.carlosrd.recipepuppy.app.recipedetails.RecipeDetailsContract;
import com.carlosrd.recipepuppy.app.recipedetails.RecipeDetailsPresenter;
import com.carlosrd.recipepuppy.app.recipesearch.RecipeSearchContract;
import com.carlosrd.recipepuppy.app.recipesearch.RecipeSearchPresenter;
import com.carlosrd.recipepuppy.data.models.PuppyRecipes;
import com.carlosrd.recipepuppy.data.models.PuppyRecipesResult;
import com.carlosrd.recipepuppy.data.source.RecipesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeDetailsPresenterTest {

    @Mock
    private PuppyRecipesResult mockPuppyRecipe;
    private final String NAME = "Name1";
    private final String INGREDIENTS = "Ingredients1";
    private final String IMAGE_URL = "ImageURL1";
    private final String URL = "URL1";

    @Mock
    RecipeDetailsContract.View mRecipeDetailsView;

    @Mock
    RecipesRepository mRecipesData;

    @Captor
    private ArgumentCaptor<RecipesRepository.LoadRecipesCallback> loadRecipesCallbackCaptor;

    private RecipeDetailsContract.Presenter mRecipeSearchPresenter;

    @Before
    public void setupmSportsmenPresenter() {

        // Iniciar Mockito
        MockitoAnnotations.initMocks(this);

        // Referencia a clase a testear
        mRecipeSearchPresenter = new RecipeDetailsPresenter(mRecipesData, mRecipeDetailsView);

        when(mockPuppyRecipe.getName()).thenReturn(NAME);
        when(mockPuppyRecipe.getImageURL()).thenReturn(IMAGE_URL);
        when(mockPuppyRecipe.getIngredients()).thenReturn(INGREDIENTS);
        when(mockPuppyRecipe.getURL()).thenReturn(URL);

    }

    @Test
    public void loadPuppyRecipe() {

        // Cargamos los detalles de la receta pasados
        mRecipeSearchPresenter.loadRecipeDetails(mockPuppyRecipe);
        ArgumentCaptor<String> setRecipeArgumentCaptor =
                ArgumentCaptor.forClass(String.class);

        // Verificamos que se setean los detalles pasados
        verify(mRecipeDetailsView, times(1)).showRecipeName(setRecipeArgumentCaptor.capture());
        assertThat(setRecipeArgumentCaptor.getValue(), is(NAME));
        verify(mRecipeDetailsView, times(1)).showRecipeIngredients(setRecipeArgumentCaptor.capture());
        assertThat(setRecipeArgumentCaptor.getValue(), is(INGREDIENTS));
        verify(mRecipeDetailsView, times(1)).showRecipeImage(setRecipeArgumentCaptor.capture());
        assertThat(setRecipeArgumentCaptor.getValue(), is(IMAGE_URL));
        verify(mRecipeDetailsView, times(1)).showRecipeURL(setRecipeArgumentCaptor.capture());
        assertThat(setRecipeArgumentCaptor.getValue(), is(URL));
    }

    @Test
    public void checkOpenRecipeURL() {

        // Cargamos los detalles de la receta pasados
        mRecipeSearchPresenter.loadRecipeDetails(mockPuppyRecipe);

        // Abrir URL
        mRecipeSearchPresenter.openRecipeURL();
        ArgumentCaptor<String> setRecipeURLArgumentCaptor = ArgumentCaptor.forClass(String.class);

        // Verificamos que se llama a abrir la URL especificada
        verify(mRecipeDetailsView, times(1)).showRecipeURL(setRecipeURLArgumentCaptor.capture());

        assertThat(setRecipeURLArgumentCaptor.getValue(), is(URL));
    }



}