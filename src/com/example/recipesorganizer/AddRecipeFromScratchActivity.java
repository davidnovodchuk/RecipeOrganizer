package com.example.recipesorganizer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class AddRecipeFromScratchActivity extends Activity {

	private DBAdapter mDbHelper;
	// private Long mRowId;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_add_from_scratch);
		
		mDbHelper = new DBAdapter(this);
		mDbHelper.open();
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
			saveState();
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
}
