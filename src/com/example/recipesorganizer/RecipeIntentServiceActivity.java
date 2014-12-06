package com.example.recipesorganizer;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

public class RecipeIntentServiceActivity extends ActionBarActivity {

	private DBAdapter mDbHelper;
	
	private IntentFilter intentFilter;
	private BroadcastReceiver intentReceiver;

	private Recipe recipe;

	// references to screen layout
	private TextView recipeName;
	private ImageView recipeImage;
	private TextView recipeIngredients;
	private TextView recipeInstructions;

	// recipeId for finding recipe in the API
	private String recipeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe);

		mDbHelper = new DBAdapter(this);
		mDbHelper.open();
		recipeName = new TextView(this);
		recipeIngredients = new TextView(this);
		recipeInstructions = new TextView(this);
		// textView = new TextView(this); 

		recipeName = (TextView)findViewById(R.id.recipe_name);
		recipeImage = (ImageView) findViewById(R.id.recipe_image);
		recipeIngredients = (TextView)findViewById(R.id.recipe_ingredients);
		recipeInstructions = (TextView)findViewById(R.id.recipe_instructions);

		Intent intent = getIntent();
		recipeId = intent.getStringExtra( "recipeId" );

		// create an explicit intent
		intent = new Intent( this,
				RecipeIntentService.class );

		// add data to the Intent: pass data to the IntentService
		intent.putExtra( "recipeId", recipeId );		

		// start IntentService and pass data to the service
		startService( intent );

		// create a broadcast receiver
		intentReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intentBroadcast){

				ArrayList<String> result = new ArrayList<String>();
				try {
					result = intentBroadcast.getStringArrayListExtra( 
							"com.example.recipeintentservice.answer"
							);
				}
				catch(Exception ex){ ex.printStackTrace(); }

				recipe = new Recipe(result.get(0),result.get(1),result.get(2),result.get(3));

				recipeName.setText( recipe.title );

				// show The Image
				new DownloadImageTask( recipeImage )
				.execute(recipe.imageURL);

				recipeIngredients.setText( recipe.ingredients );
				recipeInstructions.setText( recipe.instructions );
			}
		};
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
			saveState();
			Toast.makeText(getApplicationContext(), "Recipe Was Saved", 
					   Toast.LENGTH_SHORT).show();
			// Moving to the MainActivity
			Intent intent = new Intent(this, MainActivity.class);
			startActivity( intent );
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void saveState() {

		mDbHelper.createRecipe(recipe.title, recipe.imageURL, recipe.ingredients, recipe.instructions);
		/*
		if (id > 0) 
			mRowId = id;
		*/
	}

	/* register a broadcast receiver */
	@Override
	public void onResume(){
		super.onResume();

		// create an IntentFilter for matching purpose
		intentFilter = new IntentFilter( "NUMBER_CRUNCHING_ACTION" );
		//intentFilter.addAction( "NUMBER_CRUNCHING_ACTION" );

		// register a broadcast receiver with an IntentFilter
		registerReceiver( intentReceiver, intentFilter );	
	}

	/* unregister a broadcast receiver */
	@Override
	public void onPause(){
		super.onPause();

		unregisterReceiver( intentReceiver );
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
}