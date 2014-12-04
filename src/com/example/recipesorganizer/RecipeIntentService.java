package com.example.recipesorganizer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;

public class RecipeIntentService extends IntentService {

	private final String CLASS_NAME="LittleIntentService";
	private final String TITLE = "Title";
	private final String RECIPE_ID = "RecipeID";
	private final String INSTRUCTIONS = "Instructions";
	private final String RECIPE = "Recipe";
	private final String NAME = "Name";
	private final String INGREDIENT = "Ingredient";
	private final String METRICDISPLAYQUANTITY = "MetricDisplayQuantity";
	private final String METRICUNIT = "MetricUnit";
	private final String INGREDIENTS = "Ingredients";
	private final String IMAGEURL = "ImageURL";

	public RecipeIntentService(){  // Note: zero argument
		super( "Recipe IntentService" );
	}

	@Override
	protected void onHandleIntent( Intent intent ) {

		/* time-consuming operations/calculations */
		String recipeId = "47725";
		Recipe result = new Recipe();

		recipeId = intent.getStringExtra( "recipeId" );
		result = organizedRecipe( recipeId );
		ArrayList<String> recipeData = new ArrayList<String>();
		
		recipeData.add(result.title);
		recipeData.add(result.imageURL);
		recipeData.add(result.ingredients);
		recipeData.add(result.instructions);

		/* broadcast an implicit intent in order to report work status BACK TO an Activity */
		Intent broadcastIntent = new Intent( "NUMBER_CRUNCHING_ACTION" );

		// add data to the intent
		broadcastIntent.putStringArrayListExtra( "com.example.recipeintentservice.answer", recipeData );

		// broadcast the intent
		getBaseContext().sendBroadcast( broadcastIntent ); // getBaseContext()???
	}

	// connect to  a Word Dictionary web service and retrieve the definition of a word
	private Recipe organizedRecipe(String recipeId) {

		InputStream in = null;

		// String result = "";
		Recipe recipe = new Recipe();

		try {

			// 1. HTTP processing: open an HTTP connection
			in = OpenHttpConnection(

					"http://api.bigoven.com/recipe/" + recipeId + 
					"?api_key=dvxdQoysC1Kx12c9e19yl5w5fuFz89KI"
					);

			// use an XmlPullParser
			XmlPullParser parser = Xml.newPullParser();

			parser.setInput( in, null );

			int eventType = parser.getEventType();

			boolean done = false;

			String itemString = null;


			// going through the xml document, until reaching the end
			while ( eventType != XmlPullParser.END_DOCUMENT && !done ){
				String name = null;
				switch (eventType){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					name = parser.getName();

					if ( name.equalsIgnoreCase(RECIPE_ID) ){
						itemString = "";

					} else if ( itemString != null ){   // false: skip the heading section

						// getting the title of the recipe
						if ( name.equalsIgnoreCase(TITLE) ){
							// result = parser.nextText() + "\n\n";
							recipe.title = parser.nextText();
						}
						// getting the image URL of the recipe
						else if ( name.equalsIgnoreCase(IMAGEURL) ){
							recipe.imageURL = parser.nextText();
						}
						// getting the instructions of the recipe
						else if ( name.equalsIgnoreCase(INSTRUCTIONS) ){
							// result += "\n\nInstructions:\n\n" + parser.nextText();
							recipe.instructions = parser.nextText();
						}
						// checking if the parser in the "ingredients" area
						else if ( name.equalsIgnoreCase(INGREDIENTS) ){
							// result += "\nIngredients:\n\n";
							recipe.ingredients = "";
						}
						// getting the ingredients of the recipe
						else if ( name.equalsIgnoreCase(INGREDIENT) ){
									
							boolean completedIngredient = false;
							String ingredientInfo = "";
							while(!completedIngredient) {

								switch (eventType){
								case XmlPullParser.START_DOCUMENT:
									break;
								case XmlPullParser.START_TAG:
									name = parser.getName();

									// getting the name of the ingredient
									if ( name.equalsIgnoreCase(NAME) ){
										ingredientInfo += parser.nextText();
									}
									// getting the quantity of the ingredient
									else if ( name.equalsIgnoreCase(METRICDISPLAYQUANTITY) ){
										ingredientInfo += " - " + parser.nextText();
									}
									// getting the quantity of the ingredient
									else if ( name.equalsIgnoreCase(METRICUNIT) ){
										ingredientInfo += " " + parser.nextText();
										completedIngredient = true;
									}
									break;
								case XmlPullParser.END_TAG:
									name = parser.getName();

									if (name.equalsIgnoreCase(METRICUNIT)){
										completedIngredient = true;
									}
									break;
								}
								eventType = parser.next();
							}
							// result += ingredientInfo + "\n";
							recipe.ingredients += ingredientInfo + "\n";
						}
					}
					break;
				case XmlPullParser.END_TAG:
					name = parser.getName();

					if (name.equalsIgnoreCase(RECIPE)){
						done = true;
					}
					break;
				}

				eventType = parser.next();

			} // end while
		} catch( Exception ex ){ ex.printStackTrace(); }

		return recipe;
	}

	// open a HTTP connection
	private InputStream OpenHttpConnection( String urlString ) 
			throws IOException {

		InputStream inputStream = null;
		int response = -1;

		URL url = new URL(urlString); 

		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))                     
			throw new IOException( "Not an HTTP connection" );  

		try{
			HttpURLConnection httpConn = (HttpURLConnection) conn;

			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");

			httpConn.connect();

			response = httpConn.getResponseCode();   

			if (response == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();                                 
			}                     
		}
		catch (Exception ex)
		{
			Log.d( CLASS_NAME, ex.getLocalizedMessage() );
			throw new IOException("Error connecting");
		}
		return inputStream;     
	}    
}