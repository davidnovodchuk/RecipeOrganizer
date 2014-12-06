package com.example.recipesorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

// activity where the user chooses how to add a recipe to his / hers recipe collection
// options: 1) from scratch 2) from the Internet
public class ChooseAddOptionActivity extends ActionBarActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_add_option);
    }
	
	// Add from scratch option
	public void onClick1(View view) {
    	
    	startActivity( new Intent("com.example.recipesorganizer.AddRecipeFromScratchActivity") );
    }
	
	// Add from the Internet option
	public void onClick2(View view) {
    	
    	startActivity( new Intent("com.example.recipesorganizer.RecipeSearchIntentServiceActivity") );
    }
}