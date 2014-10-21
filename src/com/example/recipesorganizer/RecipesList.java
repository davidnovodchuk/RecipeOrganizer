package com.example.recipesorganizer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecipesList extends ListActivity {

	// recipes array is currently hard coded, in the future the recipes
	// names will be taken from the database
	String recipes[] = {
            "Recipe 1",
            "Recipe 2"
            };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            recipes
            ));
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id)
    {
        super.onListItemClick(list, view, position, id);
        String recipeName = recipes[position];
        try
        {
            // Class class_ = Class.forName("com.badlogic.androidgames." + testName);
            Intent intent = new Intent("com.example.recipesorganizer.Recipe");
            intent.putExtra("recipeName", recipeName);
            startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
