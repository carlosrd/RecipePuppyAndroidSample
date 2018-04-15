package com.carlosrd.recipepuppy.app.recipedetails;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlosrd.recipepuppy.R;
import com.carlosrd.recipepuppy.data.source.RecipesRepository;
import com.carlosrd.recipepuppy.data.models.PuppyRecipesResult;
import com.carlosrd.recipepuppy.data.source.remote.RecipesRemoteDataSource;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsContract.View {

    // ATRIBUTOS
    // ***************************************************************************

    public static String RECIPE_DETAILS = "RECIPE_DETAILS";

    private RecipeDetailsPresenter mPresenter;

    @BindView(R.id.act_recipe_details_toolbar_image) ImageView imageToolbar;
    @BindView(R.id.act_recipe_details_lbl_recipe_name) TextView recipeName;
    @BindView(R.id.act_recipe_details_lbl_ingredients) TextView ingredients;
    @BindView(R.id.act_recipe_details_lbl_web_reference) TextView webReference;


    // ACTIVITY LIFECYCLE
    // ***************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details_activity);

        // Seteamos las Toolbar
        setUpToolbar();

        ButterKnife.bind(this);

        // Creamos el Presenter entre esta vista (Activity) y la capa de datos (RecipesRepository)
        mPresenter = new RecipeDetailsPresenter(
                RecipesRepository.getInstance(RecipesRemoteDataSource.getInstance()), this);

        // Recibir receta a mostrar
        PuppyRecipesResult recipe = getIntent().getParcelableExtra(RECIPE_DETAILS);

        if (recipe != null)
            mPresenter.loadRecipeDetails(recipe);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter = null;
    }

    // MÉTODOS
    // ***************************************************************************

    private void setUpToolbar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.act_recipe_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para que se ejecute la animación de forma inversa al cerrar la Activity
                supportFinishAfterTransition();
            }
        });

        // Eliminamos el Titulo de la Toolbar
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.act_recipe_details_toolbar_layout);
        toolbarLayout.setTitle(" ");

    }

    @Override
    public void showRecipeName(String name) {
        recipeName.setText(Html.fromHtml(name));
    }

    @Override
    public void showRecipeIngredients(String ingredientsList) {
        ingredients.setText(ingredientsList);
    }

    @Override
    public void showRecipeURL(String url) {

        webReference.setText(url);
        webReference.setPaintFlags(webReference.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Acción para abrir el enlace de la receta en el navegador
        webReference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPresenter.openRecipeURL();

            }
        });
    }

    @Override
    public void showRecipeImage(String imageURL) {

        if (imageURL.equals("")) {
            Picasso.get()
                    .load(R.drawable.recipe_placeholder)
                    .into(imageToolbar);
        }
        else {
            Picasso.get()
                    .load(imageURL)
                    .fit()
                    .placeholder(R.drawable.recipe_placeholder)
                    .error(R.drawable.recipe_placeholder)
                    .into(imageToolbar);
        }

    }

    @Override
    public void browseRecipeURL(String url) {

        // Abrir URL en el explorador web
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }


}
