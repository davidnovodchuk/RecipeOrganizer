package com.example.recipesorganizer;

public class Recipe {

	public String title;
	public String imageURL;
	public String ingredients;
	public String instructions;
	
	public Recipe() {
		
		this("","","","");
	}
	
	public Recipe( String title , String imageURL , String ingredients , String instructions ) {
		
		this.title = title;
		this.imageURL = imageURL;
		this.ingredients = ingredients;
		this.instructions = instructions;
	}
}