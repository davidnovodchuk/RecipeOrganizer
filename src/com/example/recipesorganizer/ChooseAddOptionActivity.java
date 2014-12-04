package com.example.recipesorganizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseAddOptionActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_add_option);
    }
	
	public void onClick1(View view) {
    	
    	startActivity( new Intent("com.example.recipesorganizer.AddRecipeFromScratchActivity") );
    }
	
	public void onClick2(View view) {
    	
    	startActivity( new Intent("com.example.recipesorganizer.RecipeSearchIntentServiceActivity") );
    }
}