package com.example.recipesorganizer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
	private static DBAdapter mDbHelper;
	private static Recipe recipe;
	private Cursor recipesCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDbHelper = new DBAdapter(this);
		mDbHelper.open();
		recipe = new Recipe();
		Log.d("debug", "i dont know");
		// Get ListView object from xml
		listView = (ListView) findViewById(R.id.recipeList);

		getDatabase();
		recipesCursor = mDbHelper.fetchColumn();

		String[] title = null;
		if(recipesCursor.moveToFirst())
		{
			do {
				title = new String[]{recipesCursor.getString(0)};
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
					recipesCursor = mDbHelper.fetchRecipe(id);
					getRecipe(recipesCursor);
					Bundle bundle = new Bundle();
		        	bundle.putString("title", recipe.title);
		        	bundle.putString("image", recipe.imageURL);
		        	bundle.putString("ingredients", recipe.ingredients);
		        	bundle.putString("instructions", recipe.instructions);
				
		        	intent.putExtra("recipe", bundle);
			
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

	public void getRecipe(Cursor c)
	{
		recipe.title = c.getString(1);
		recipe.imageURL = c.getString(2);
		recipe.ingredients = c.getString(3);
		recipe.instructions = c.getString(4);
		
	}
	
	public void getDatabase()
	{
		try {
			Log.d("debug", "inside database");
            String destPath = "/data/data/" + getPackageName() +
                              "/databases";
            
            File f = new File( destPath );
            
            if ( !f.exists() ) {  
            	
            	Log.i( "Database", "create directory: /databases/" );
            	
            	f.mkdirs();
                f.createNewFile();
            	
            	// copy the database from the assets folder (/assets) into 
            	// the databases folder on the device (/data/data/<package name>/databases)
                // - database file name at /assets: mydb
                
                copyDB( getBaseContext().getAssets().open( "mydb" ),
                        new FileOutputStream( destPath + "/MyDB" ) ); // Watch out: UPPERCASE LETTERS
            }
        } catch (FileNotFoundException e) {
                 e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

	private void copyDB( InputStream inputStream, OutputStream outputStream ) throws IOException {
//---copy 1K bytes at a time---
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		inputStream.close();
		outputStream.close();

		Log.i( "Database", "copying done" );
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