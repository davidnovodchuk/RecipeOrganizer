package com.example.recipesorganizer;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	ListView listView ;
	private static final int ACTIVITY_CREATE=0;
	private static final int ACTIVITY_EDIT=1;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private DBAdapter mDbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDbHelper = new DBAdapter(this);
		mDbHelper.open();

		// Get ListView object from xml
		listView = (ListView) findViewById(R.id.recipeList);

		Cursor recipesCursor = mDbHelper.fetchAllNotes();

		String[] title = null;
		if(recipesCursor.moveToFirst())
		{
			do {
				title = new String[]{DBAdapter.KEY_TITLE};
			} while (recipesCursor.moveToLast());
		}

		if( title != null ) {
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, title);

			// Assign adapter to ListView
			listView.setAdapter(adapter); 

			// ListView Item Click Listener
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// ListView Clicked item index
					// int itemPosition     = position;

					// ListView Clicked item value
					String  itemValue    = (String) listView.getItemAtPosition(position);

					// Moving to the RecipeActivity and passing selected recipe name
					Intent intent = new Intent("com.example.recipesorganizer.RecipeActivity");
					intent.putExtra(DBAdapter.KEY_ROWID, id);
					startActivity( intent );

				}

			});
		}
		else {
			// presenting message when there are no recipes in the database
			
			RelativeLayout rr = new RelativeLayout(this);
			TextView tv = new TextView(this);
			tv.setText("No Recipes in the Database!");
			rr.addView(tv);
			
			setContentView(rr);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
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
		if (id == R.id.action_new) {
			Intent intent = new Intent("com.example.recipesorganizer.ChooseAddOptionActivity");
			intent.putExtra("recipe_name", "test");
			startActivity( intent );
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
