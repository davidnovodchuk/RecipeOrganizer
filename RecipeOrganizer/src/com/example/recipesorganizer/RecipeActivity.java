package com.example.recipesorganizer;

import java.io.InputStream;
import java.util.ArrayList;

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

public class RecipeActivity extends ActionBarActivity {
	private Recipe recipe;
	private TextView recipeName;
	private TextView recipeIngredients;
	private TextView recipeInstructions;
	private DBAdapter mDbHelper;
	private Long mRowId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe);
		
		mDbHelper = new DBAdapter(this);
        mDbHelper.open();
        
        /*mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DBAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(DBAdapter.KEY_ROWID)
									: null;
		}*/
	
		Intent intent = getIntent();
		/*Bundle bundle = intent.getBundleExtra("recipe");
		recipe = new Recipe(bundle.getString("title"), bundle.getString("image"), bundle.getString("ingredients"), bundle.getString("instructions"));*/
		ArrayList<String> result = new ArrayList<String>();
		result = intent.getStringArrayListExtra("recipe");
		recipe = new Recipe(result.get(0),result.get(1),result.get(2),result.get(3));
		
		recipeName = (TextView)findViewById(R.id.recipe_name);
		recipeIngredients = (TextView)findViewById(R.id.recipe_ingredients);
		recipeInstructions = (TextView)findViewById(R.id.recipe_instructions);
		
		recipeName.setText(recipe.title);
		recipeIngredients.setText(recipe.ingredients);
		recipeInstructions.setText(recipe.instructions);
		new DownloadImageTask((ImageView) findViewById(R.id.recipe_image))
        .execute(recipe.imageURL);
		
	}
	
	
	private void saveState() {
        
           long id = mDbHelper.createRecipe(recipe.title, recipe.imageURL, recipe.ingredients, recipe.instructions);
            if (id > 0) 
                mRowId = id;
        
            
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
	
	public void getRecipe(Cursor c)
	{
		recipe.title = c.getString(1);
		recipe.imageURL = c.getString(2);
		recipe.ingredients = c.getString(3);
		recipe.instructions = c.getString(4);
		
	}
}