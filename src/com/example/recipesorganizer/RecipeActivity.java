package com.example.recipesorganizer;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecipeActivity extends Activity {

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		//setContentView(R.layout.recipe);
		Intent intent = getIntent();
		//int id = intent.getIntExtra(", defaultValue)
	
		
		String recipeName = "Elbows and Ground Beef \n";
		
		//LinearLayout linear = (LinearLayout)findViewById(R.id.my_linear_layout);
		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);

		//TextView textView = (TextView)findViewById(R.id.recipe_name);
		//TextView textView_ingredients = (TextView)findViewById(R.id.recipe_ingredients);
		//TextView textView_method = (TextView)findViewById(R.id.recipe_method);
		
		TextView textview1 = new TextView(this);
		TextView textview2 = new TextView(this);
		TextView textview3 = new TextView(this);
		ImageView image = new ImageView(this);
		
		
		String ingredients = "\n" +"\u2022 1 1/2 pounds lean ground beef \n" +
				"\u2022 1 green bell pepper, chopped \n" +
				"\u2022 1 onion, chopped \n" +
				"\u2022 2 (29 ounce) cans tomato sauce \n" +
				"\u2022 1 (16 ounce) package macaroni \n";
		
		
		
		String method = "1. Cook pasta according to package directions. Drain. \n" +
				"2. In a Dutch oven, brown ground beef over medium heat. Add chopped onion, and cook until onion is soft. Add green pepper and tomato sauce; cook until pepper is soft. \n" +
				"3. Serve sauce over pasta.";
		
		textview1.setTextSize(30);
		textview1.setText(recipeName);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
		
		image.setLayoutParams(params);	
		image.setImageResource(R.drawable.recipe);
		
		textview2.setTextSize(15);
		textview2.setText(ingredients);
		
		textview3.setTextSize(15);
		textview3.setText(method);
		
		linear.addView(textview1);
		linear.addView(image);
		linear.addView(textview2);
		linear.addView(textview3);
	
		
		setContentView(linear);
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.recipecontent, menu);
	    return super.onCreateOptionsMenu(menu);
	}
}
