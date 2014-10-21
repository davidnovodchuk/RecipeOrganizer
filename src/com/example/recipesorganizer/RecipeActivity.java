package com.example.recipesorganizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RecipeActivity extends Activity {

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.recipe);
		
		Intent intent = getIntent();
		String s = intent.getStringExtra("recipe_name");
		
		TextView txtView = (TextView)findViewById(R.id.recipe_name);
		
		txtView.setText(s);
	}
}
