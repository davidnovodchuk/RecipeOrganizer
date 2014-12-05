package com.example.recipesorganizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class AddRecipeFromScratchActivity extends Activity {

	private DBAdapter mDbHelper;
	
	// isAddMode indicates if current activity is used for adding recipe from scratch
	// or editing existing recipe
	private boolean isAddMode;
	private Recipe recipe;
	private String mRowId;
	private Bundle recipeBundle;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_add_from_scratch);
		
		mDbHelper = new DBAdapter(this);
		mDbHelper.open();
		
		recipe = null;
		try {
			
			Intent intent = getIntent();
			recipeBundle = intent.getBundleExtra("recipe");
			recipe = new Recipe(recipeBundle.getString("title"), recipeBundle.getString("image"), 
					recipeBundle.getString("ingredients"), recipeBundle.getString("instructions"));
			mRowId = recipeBundle.getString("rowId");
			
			EditText titleField = (EditText)findViewById(R.id.recipe_name);
			EditText ingredientsField = (EditText) findViewById(R.id.recipe_ingredients);
			EditText instructionsField = (EditText) findViewById(R.id.recipe_instructions);

			titleField.setText(recipe.title);
			ingredientsField.setText(recipe.ingredients);
			instructionsField.setText(recipe.instructions);
			
		} catch( Exception e ){};
		
		if( recipe == null )
			isAddMode = true;
		else
			isAddMode = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.saverecipe, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*
		if (id == R.id.action_settings) {
			return true;
		}
		 */
		if (id == R.id.action_save) {
			if( isAddMode ) {
				saveState();
				// Moving to the RecipeActivity and passing selected recipe name
				Intent intent = new Intent(this, MainActivity.class);
				startActivity( intent );
			} else {
				updateState();
				
				String title =((EditText) findViewById(R.id.recipe_name)).getText().toString();
				String ingredients =((EditText) findViewById(R.id.recipe_ingredients)).getText().toString();
				String instructions =((EditText) findViewById(R.id.recipe_instructions)).getText().toString();
				
				// Moving to the RecipeActivity and passing selected recipe name
				Intent intent = new Intent("com.example.recipesorganizer.RecipeActivity");
				
				Bundle bundle = new Bundle();
				bundle.putString("rowId", mRowId);
				bundle.putString("title", title);
				bundle.putString("image", recipe.imageURL);
				bundle.putString("ingredients", ingredients);
				bundle.putString("instructions", instructions);
				
				intent.putExtra("recipe", bundle);
				
				startActivity( intent );
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void saveState() {

		String title =((EditText) findViewById(R.id.recipe_name)).getText().toString();
		String ingredients =((EditText) findViewById(R.id.recipe_ingredients)).getText().toString();
		String instructions =((EditText) findViewById(R.id.recipe_instructions)).getText().toString();

		mDbHelper.createRecipe(title, "", ingredients, instructions);
		/*
		long id = mDbHelper.createRecipe(title, "", ingredients, instructions);
		if (id > 0) 
			mRowId = id;
		*/
	}
	
	private void updateState() {

		String title =((EditText) findViewById(R.id.recipe_name)).getText().toString();
		String ingredients =((EditText) findViewById(R.id.recipe_ingredients)).getText().toString();
		String instructions =((EditText) findViewById(R.id.recipe_instructions)).getText().toString();

		mDbHelper.updateRecipe(mRowId, title, recipe.imageURL, ingredients, instructions);
		/*
		long id = mDbHelper.createRecipe(title, "", ingredients, instructions);
		if (id > 0) 
			mRowId = id;
		*/
	}
}
