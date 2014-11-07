package com.example.recipesorganizer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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

	public RecipeIntentService(){  // Note: zero argument
		super( "Recipe IntentService" );
	}

	@Override
	protected void onHandleIntent( Intent intent ) {

		/* time-consuming operations/calculations */
		String recipeId = "47725";
		String result;

		recipeId = intent.getStringExtra( "recipeId" );
		result = wordDefinition( recipeId );

		/* broadcast an implicit intent in order to report work status BACK TO an Activity */
		Intent broadcastIntent = new Intent( "NUMBER_CRUNCHING_ACTION" );

		// add data to the intent
		broadcastIntent.putExtra( "com.example.recipeintentservice.answer", result );

		// broadcast the intent
		getBaseContext().sendBroadcast( broadcastIntent ); // getBaseContext()???
	}

	// connect to  a Word Dictionary web service and retrieve the definition of a word
	private String wordDefinition(String recipeId) {

		InputStream in = null;
		
		String result = "";

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

						// getting the id of the recipe
						if ( name.equalsIgnoreCase(TITLE) ){
							result = parser.nextText() + "\n\n";
						}
						// getting the title of the recipe
						else if ( name.equalsIgnoreCase(INSTRUCTIONS) ){
							result += "Instructions:\n" + parser.nextText();
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
		
		return result;
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
