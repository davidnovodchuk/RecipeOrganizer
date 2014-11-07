package com.example.recipesorganizer;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class RecipeSearchIntentServiceActivity extends ListActivity {

	private IntentFilter intentFilter;
	private BroadcastReceiver intentReceiver;

	private EditText inputView;
	public static ArrayList<String> titles;

	// ids of recipes provided by the api
	public static String[] ids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_search_intent_service);

		inputView = (EditText) findViewById( R.id.editText1 );

		// create a broadcast receiver
		intentReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intentBroadcast){

				// String result="";
				ArrayList<String> titles = new ArrayList<String>();
				try {
					titles = intentBroadcast.getStringArrayListExtra( 
							"com.example.littleintentservice.answer"
							);
				}
				catch(Exception ex){ ex.printStackTrace(); }

				// converting the ArrayList of strings to array of Strings to use for ListView
				String[] titleItems = new String[ titles.size() ];
				RecipeSearchIntentServiceActivity.ids = new String[ titles.size() ];

				for( int i = 0 ; i < titles.size() ; i++ ) {

					String str = titles.get(i);

					RecipeSearchIntentServiceActivity.ids[i] = str.substring(0, str.indexOf('|'));
					titleItems[i] = str.substring(str.indexOf('|') + 1, str.length());
				}

				// creating a ListView
				ListView lstView = getListView();

				//lstView.setChoiceMode(ListView.CHOICE_MODE_NONE);   // normal list without choices
				lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // one choice
				//lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // many choices

				lstView.setTextFilterEnabled(true); // filter the children according to user input

				// load an array of strings - definitions

				// connect the ArrayAdapter to the ListView
				// - ArrayAdapter manages the array of strings for the ListView
				setListAdapter( new ArrayAdapter<String>(
						context,                                        // current context
						android.R.layout.simple_list_item_1,   // built-in rows layout file
						titleItems ) );                              // an array of strings
			}
		};
	}

	public void clickGet(View view){

		String word = "cococunt";

		word = inputView.getText().toString();  // what if the field is blank?
		// converting spaces, so it will work in the api url
		word = word.replaceAll("[ ]", "%20");

		// create an explicit intent
		Intent intent = new Intent( this,
				RecipeSearchIntentService.class );

		// add data to the Intent: pass data to the IntentService
		intent.putExtra( "keyword", word);		

		// start IntentService and pass data to the service
		startService( intent ); 
	}

	/* event handler for the ListView
	 *    - fired when a ListView item is clicked (i.e. selected AND deselected) 
	 * API: provided by the ListActivity class
	 */
	public void onListItemClick(
			ListView parent, View v, int position, long id )
	{
		// create an explicit intent
		Intent intent = new Intent( this,
				RecipeIntentServiceActivity.class );

		// add data to the Intent: pass data to the IntentService
		intent.putExtra( "recipeId", ids[position] );		

		// start IntentService and pass data to the service
		startActivity( intent );
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
