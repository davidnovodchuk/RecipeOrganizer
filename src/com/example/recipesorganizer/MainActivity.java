package com.example.recipesorganizer;

import android.support.v7.app.ActionBarActivity;
import android.view.View; 
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView; 
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ActionBarActivity {
	ListView listView ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		 // Get ListView object from xml
        listView = (ListView) findViewById(R.id.recipeList);
        
        // Defined Array values to show in ListView
        String[] values = new String[] { "Recipe 1", 
                                         "Recipe 2",
                                         "Recipe 3", 
                                         "Recipe 4"
                                        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, values);


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
               intent.putExtra("recipe_name", itemValue);
               startActivity( intent );
               /*
                // Show Alert 
                Toast.makeText(getApplicationContext(),
                  "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                  .show();
                */
              }

         });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		if (id == R.id.add_recipe) {
			Intent intent = new Intent("com.example.recipesorganizer.ChooseAddOptionActivity");
            intent.putExtra("recipe_name", "test");
            startActivity( intent );
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
