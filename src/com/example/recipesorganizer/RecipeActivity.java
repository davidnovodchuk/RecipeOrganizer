package com.example.recipesorganizer;

import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

// view recipe
public class RecipeActivity extends ActionBarActivity {
	private Recipe recipe;
	private TextView recipeName;
	private TextView recipeIngredients;
	private TextView recipeInstructions;
	private DBAdapter mDbHelper;
	private int mRowId;
	private Bundle bundle;
	// currentActivity is used in the remove recipe dialog
	private Activity currentActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe);
		
		currentActivity = this;
		
		mDbHelper = new DBAdapter(this);
        mDbHelper.open();
        
        // getting recipe info to present on the screen
		Intent intent = getIntent();
		bundle = intent.getBundleExtra("recipe");
		recipe = new Recipe(bundle.getString("title"), bundle.getString("image"), 
				bundle.getString("ingredients"), bundle.getString("instructions"));
		mRowId = Integer.parseInt(bundle.getString("rowId"));
		
		recipeName = (TextView)findViewById(R.id.recipe_name);
		recipeIngredients = (TextView)findViewById(R.id.recipe_ingredients);
		recipeInstructions = (TextView)findViewById(R.id.recipe_instructions);
		
		// presenting recipe on the screen
		recipeName.setText(recipe.title);
		recipeIngredients.setText(recipe.ingredients);
		recipeInstructions.setText(recipe.instructions);
		new DownloadImageTask((ImageView) findViewById(R.id.recipe_image))
        .execute(recipe.imageURL);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.recipecontent, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.action_edit) {
			
			// Using AddRecipeFromScratchActivity for editing existing recipe
			Intent intent = new Intent("com.example.recipesorganizer.AddRecipeFromScratchActivity");
			
			intent.putExtra("recipe", bundle);
			
			startActivity( intent );
			
			return true;
		}
		
		if (id == R.id.action_discard) {
			// confirmation that user wants to delete recipe:
			// taken from: http://stackoverflow.com/questions/19286135/android-alert-dialog-and-set-positive-button
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

		    builder.setTitle("Deletion Confirmation");
		    builder.setMessage("Are you sure you want to delete the " + recipe.title + " recipe?");
		    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {

					mDbHelper.deleteRecipe(mRowId);
					Intent intent = new Intent(currentActivity, MainActivity.class);
					startActivity(intent);
		         }
		    });
		    builder.setNegativeButton("Cancel", null);
		    builder.show();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	// loading recipe image from image url
	// code taken from: http://stackoverflow.com/questions/5776851/load-image-from-url
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
	
	//fetch recipe from the cursor into the recipe object
	public void getRecipe(Cursor c)
	{
		recipe.title = c.getString(1);
		recipe.imageURL = c.getString(2);
		recipe.ingredients = c.getString(3);
		recipe.instructions = c.getString(4);
		
	}
}