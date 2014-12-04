package com.example.recipesorganizer;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeIntentServiceActivity extends Activity {

	private IntentFilter intentFilter;
	private BroadcastReceiver intentReceiver;
	
	private Recipe recipe;

	private TextView recipeName;
	private ImageView recipeImage;
	private TextView recipeIngredients;
	private TextView recipeInstructions;
	
	// private TextView textView;
	private String recipeId;
	// public static ArrayList<String> titles;

	// ids of recipes provided by the api
	public static String[] ids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe);

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
				
				Log.d("test", result.get(0));

				recipe = new Recipe(result.get(0),result.get(1),result.get(2),result.get(3));
				
				recipeName.setText( recipe.title );

				// show The Image
				new DownloadImageTask((ImageView) findViewById(R.id.recipe_image))
				            .execute(recipe.imageURL);
				
				recipeIngredients.setText( recipe.ingredients );
				recipeInstructions.setText( recipe.instructions );
			}
		};
	}

	public void clickGet(View view){


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
