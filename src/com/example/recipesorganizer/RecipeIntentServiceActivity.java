package com.example.recipesorganizer;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RecipeIntentServiceActivity extends Activity {

	private IntentFilter intentFilter;
	private BroadcastReceiver intentReceiver;

	private TextView textView;
	private String recipeId;
	public static ArrayList<String> titles;

	// ids of recipes provided by the api
	public static String[] ids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_intent_service);

		textView = new TextView(this); 

		textView = (TextView)findViewById(R.id.recipeTextView);

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

				Recipe recipe = new Recipe(result.get(0),result.get(1),result.get(2),result.get(3));
				String view = recipe.title + 
							  "\n\nIngredients:\n" + recipe.ingredients + 
							  "\n\nInstructions\n" + recipe.instructions;
				textView.setText( view );
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
	public void onPause(){
		super.onPause();

		unregisterReceiver( intentReceiver );
	}

}
