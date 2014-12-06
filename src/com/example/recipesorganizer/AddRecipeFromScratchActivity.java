package com.example.recipesorganizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddRecipeFromScratchActivity extends ActionBarActivity {
	
	// currentActivity is used in a dialog to open a new activity
	Activity currentActivity;

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
		
		currentActivity = this;
		
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
		if (id == R.id.action_save) {
			if( isAddMode ) {
				saveState();
				Toast.makeText(getApplicationContext(), "Recipe Was Saved", 
						   Toast.LENGTH_SHORT).show();
				// Moving to the MainActivity
				Intent intent = new Intent(this, MainActivity.class);
				startActivity( intent );
			} else {
				// confirmation that user wants to update recipe details:
				// http://stackoverflow.com/questions/19286135/android-alert-dialog-and-set-positive-button
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

			    builder.setTitle("Edit Confirmation");
			    builder.setMessage("Are you sure you want to change recipe information?");
			    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
			        	updateState();
						Toast.makeText(getApplicationContext(), "Recipe Was Updated", 
								   Toast.LENGTH_SHORT).show();
						
						// Moving to the MainActivity
						Intent intent = new Intent(currentActivity, MainActivity.class);
						startActivity( intent );
			         }
			    });
			    builder.setNegativeButton("Cancel", null);
			    builder.show();
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
	}
	
	private void updateState() {

		String title =((EditText) findViewById(R.id.recipe_name)).getText().toString();
		String ingredients =((EditText) findViewById(R.id.recipe_ingredients)).getText().toString();
		String instructions =((EditText) findViewById(R.id.recipe_instructions)).getText().toString();

		mDbHelper.updateRecipe(mRowId, title, recipe.imageURL, ingredients, instructions);
	}
}
