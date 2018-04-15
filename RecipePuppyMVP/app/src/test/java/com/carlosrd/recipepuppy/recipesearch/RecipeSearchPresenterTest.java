package com.carlosrd.recipepuppy.recipesearch;

import com.carlosrd.recipepuppy.app.recipesearch.RecipeSearchContract;
import com.carlosrd.recipepuppy.app.recipesearch.RecipeSearchPresenter;
import com.carlosrd.recipepuppy.data.source.RecipesRepository;
import com.carlosrd.recipepuppy.data.models.PuppyRecipes;
import com.carlosrd.recipepuppy.data.models.PuppyRecipesResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeSearchPresenterTest {


    private String mockQuery;
    @Mock
    private PuppyRecipes mockResponse;
    @Mock
    private ArrayList<PuppyRecipesResult> mockRecipesList;
    @Mock
    private PuppyRecipesResult mockPuppyRecipe;

    @Mock
    RecipeSearchContract.View mRecipeSearchView;

    @Mock
    RecipesRepository mRecipesData;

    @Captor
    private ArgumentCaptor<RecipesRepository.LoadRecipesCallback> loadRecipesCallbackCaptor;

    private RecipeSearchContract.Presenter mRecipeSearchPresenter;

    @Before
    public void setupmSportsmenPresenter() {

        // Iniciar Mockito
        MockitoAnnotations.initMocks(this);

        // Referencia a clase a testear
        mRecipeSearchPresenter = new RecipeSearchPresenter(mRecipesData, mRecipeSearchView);

    }


    @Test
    public void loadRecipesFromRepositoryAndLoadIntoView() {

        // Preparamos mocks
        mockQuery = "chicken";
        when(mockRecipesList.size()).thenReturn(7);
        when(mockResponse.getResults()).thenReturn(mockRecipesList);

        // Iniciamos búsqueda
        mRecipeSearchPresenter.loadRecipes(mockQuery);

        // Capturamos callback y lo invocamos con los mocks
        verify(mRecipesData).loadRecipes(eq(mockQuery), loadRecipesCallbackCaptor.capture());
        loadRecipesCallbackCaptor.getValue().onRecipesLoaded(mockResponse);

        // Verificamos que se cargan los datos pasados
        ArgumentCaptor<PuppyRecipes> setRecipesListArgumentCaptor = ArgumentCaptor.forClass(PuppyRecipes.class);
        verify(mRecipeSearchView).showRecipes(setRecipesListArgumentCaptor.capture());

        assertTrue(setRecipesListArgumentCaptor.getValue().getResults().size() == 7);

    }

    @Test
    public void loadEmptyQueryWithNoResultsFromRepository() {

        // Probamos a cargar una peticion vacia
        mockQuery = "";

        // Iniciamos búsqueda
        mRecipeSearchPresenter.loadRecipes(mockQuery);

        // Verificamos que no devuelve resultados
        verify(mRecipeSearchView).showNoResults();

    }

    @Test
    public void loadQueryWithNoResultsFromRepository() {

        // Busqueda con texto que no arroja resultados
        mockQuery = "abcdefg";
        when(mockRecipesList.size()).thenReturn(0);
        when(mockResponse.getResults()).thenReturn(mockRecipesList);

        // Iniciamos búsqueda
        mRecipeSearchPresenter.loadRecipes(mockQuery);

        // Capturamos callback y lo invocamos con los mocks
        verify(mRecipesData).loadRecipes(eq(mockQuery), loadRecipesCallbackCaptor.capture());
        loadRecipesCallbackCaptor.getValue().onRecipesLoaded(mockResponse);

        // Verificamos que no se cargan resultados
        verify(mRecipeSearchView).showNoResults();

    }

    @Test
    public void loadResultsRepositoryAndGetError() {

        mockQuery = "chicken";

        // Iniciamos búsqueda
        mRecipeSearchPresenter.loadRecipes(mockQuery);

        // Capturamos callback y lo invocamos con los mocks
        verify(mRecipesData).loadRecipes(eq(mockQuery), loadRecipesCallbackCaptor.capture());
        loadRecipesCallbackCaptor.getValue().onError(404);

        // Verificamos que se notificado el error
        ArgumentCaptor<Integer> setPlayersArgumentCaptor = ArgumentCaptor.forClass(int.class);
        verify(mRecipeSearchView, times(1)).showError(setPlayersArgumentCaptor.capture());

        assertTrue(setPlayersArgumentCaptor.getValue() == 404);

    }

    @Test
    public void checkIfLastQueryIsRecovered() {

        // Recuperamos ultima query hecha
        mockQuery = "chicken";

        // La seteamos
        mRecipeSearchPresenter.setCurrentQuery(mockQuery);

        // El widget de Busqueda llama a recupear la ultima query
        mRecipeSearchPresenter.recoverCurrentQuery();

        // Verificamos que el widget de busqueda recupera la ultima query almacenada
        ArgumentCaptor<String> setQueryArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(mRecipeSearchView, times(1)).setCurrentQuery(setQueryArgumentCaptor.capture());

        assertThat(setQueryArgumentCaptor.getValue(), is(mockQuery));
    }




    @Test
    public void loadRecipedDetails() {

        when(mockPuppyRecipe.getName()).thenReturn("Title1");
        when(mockPuppyRecipe.getImageURL()).thenReturn("ImageURL1");
        when(mockPuppyRecipe.getIngredients()).thenReturn("IngredientesList");
        when(mockPuppyRecipe.getURL()).thenReturn("URL1");

        mRecipeSearchPresenter.openRecipeDetails(mockPuppyRecipe);
        ArgumentCaptor<PuppyRecipesResult> captor = ArgumentCaptor.forClass(PuppyRecipesResult.class);
        verify(mRecipeSearchView, times(1)).showRecipeDetails(captor.capture());

        assertThat(captor.getValue().getName(), is("Title1"));
        assertThat(captor.getValue().getImageURL(), is("ImageURL1"));

    }

}