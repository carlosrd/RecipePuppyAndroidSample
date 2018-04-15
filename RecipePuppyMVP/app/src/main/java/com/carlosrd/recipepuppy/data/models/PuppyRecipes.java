package com.carlosrd.recipepuppy.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PuppyRecipes {

    // ATRIBUTOS
    // ***************************************************************************
    @SerializedName("title")
    private String title;

    @SerializedName("version")
    private String version;

    @SerializedName("href")
    private String url;

    @SerializedName("results")
    private ArrayList<PuppyRecipesResult> results;

    // CONSTRUCTORAS
    // ***************************************************************************

    public PuppyRecipes(String title, String version, String url, ArrayList<PuppyRecipesResult> results) {
        this.title = title;
        this.version = version;
        this.url = url;
        this.results = results;
    }

    // CONSULTORAS
    // ***************************************************************************

    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return version;
    }

    public String getURL() {
        return url;
    }

    public ArrayList<PuppyRecipesResult> getResults() {
        return results;
    }


}
