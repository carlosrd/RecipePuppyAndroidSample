package com.carlosrd.recipepuppy.app.recipesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlosrd.recipepuppy.R;
import com.carlosrd.recipepuppy.app.recipedetails.RecipeDetailsActivity;
import com.carlosrd.recipepuppy.app.recipesearch.adapters.RecipesRecyclerAdapter;
import com.carlosrd.recipepuppy.data.source.RecipesRepository;
import com.carlosrd.recipepuppy.data.models.PuppyRecipes;
import com.carlosrd.recipepuppy.data.models.PuppyRecipesResult;
import com.carlosrd.recipepuppy.data.source.remote.RecipesRemoteDataSource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeSearchActivity extends AppCompatActivity implements RecipeSearchContract.View{

    private final String CURRENT_QUERY = "CURRENT_QUERY";

    private SearchView mSearchView;

    @BindView(R.id.act_recipe_search_no_results) TextView noResultsLbl;

    @BindView(R.id.act_recipe_search_recyclerview) RecyclerView mRecyclerView;

    private RecipesRecyclerAdapter mAdapter;

    // Para apuntar a la ImageView de la lista que hará la animacion de transicion
    private ImageView imageTransition;

    private RecipeSearchContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_search_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.act_recipe_search_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Setear layout de la lista
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RecipeSearchActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Creamos el Presenter entre esta vista (Activity) y la capa de datos (RecipesRepository)
        mPresenter = new RecipeSearchPresenter(
                RecipesRepository.getInstance(RecipesRemoteDataSource.getInstance()), this);

        // Si esta disponible, cargamos ultima query hecha
        if (savedInstanceState != null) {
            String currentQuery = savedInstanceState.getString(CURRENT_QUERY);
            mPresenter.setCurrentQuery(currentQuery);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_QUERY, mPresenter.getCurrentQuery());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter = null;
    }

    // ACTION BAR
    // ***************************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflamos el menú de la ActionBar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recipe_search, menu);

        // Inicializar el SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.menu_action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Seteamos eventos de acción del SearchView
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Evento cuando se pulsa Enter o Buscar
            @Override
            public boolean onQueryTextSubmit(String query) {

                mPresenter.loadRecipes(query);

                // Quitar el foco para evitar peticiones duplicadas (al perder
                // el foco, se dispara de nuevo este evento)
                mSearchView.clearFocus();

                return true;
            }

            // Evento cuando cambia el texto de búsqueda
            @Override
            public boolean onQueryTextChange(String newText) {

                mPresenter.loadRecipes(newText);

                return true;

            }

        });

        // Una vez cargado el widget, recuperamos posible query anterior
        mPresenter.recoverCurrentQuery();

        return super.onCreateOptionsMenu(menu);

    }


    // METODOS
    // ***************************************************************************

    @Override
    public void showRecipes(final PuppyRecipes recipesList) {

        noResultsLbl.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        // Si ya tenemos adapter, actualizamos los datos
        // si no, inicializamos al completo
        if (mAdapter != null)
            mAdapter.updateDataSet(recipesList.getResults());
        else {
            // Especificamos un adaptador (y tambien oyente para evento OnClick de cada elemento)
            mAdapter = new RecipesRecyclerAdapter(recipesList.getResults(),
                    new RecipesRecyclerAdapter.RecipeListItemClickListener() {
                            @Override
                            public void onRecipeClicked(ImageView imgView, PuppyRecipesResult puppyRecipe) {

                                imageTransition = imgView;
                                mPresenter.openRecipeDetails(puppyRecipe);

                            }
                        });

            // Setear el adaptador al RecyclerView
            mRecyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    public void showNoResults() {

        noResultsLbl.setText(getString(R.string.act_recipe_search_lbl_empty_list));

        noResultsLbl.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

    }

    @Override
    public void showError(int statusCode) {

        if (statusCode != -1) {
            String msg = getString(R.string.act_recipe_search_msg_server_error);
            msg = msg.replace("@statusCode",String.valueOf(statusCode));
            noResultsLbl.setText(msg);
        } else {
            noResultsLbl.setText(getString(R.string.act_recipe_search_msg_generic_error));
        }

        noResultsLbl.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

    }

    @Override
    public void setCurrentQuery(String query) {

        mSearchView.setQuery(query, true);
        mSearchView.setIconified(false);

    }

    @Override
    public void showRecipeDetails(PuppyRecipesResult puppyRecipe) {

        Intent i = new Intent(RecipeSearchActivity.this, RecipeDetailsActivity.class);
        i.putExtra(RecipeDetailsActivity.RECIPE_DETAILS, puppyRecipe);

        // Activamos animaciones de transición solo si es la API igual o mayor a la 16
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(
                            RecipeSearchActivity.this, imageTransition, "imageRecipeTrans");

            startActivity(i, options.toBundle());
        } else
            startActivity(i);

    }

}
