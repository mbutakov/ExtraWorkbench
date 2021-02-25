package mbutakov.ExtraWorkbench;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftFileSecond {
	
	public String stringInTxt;
	public ItemStack itemToCraft;
	public CraftFileSecond(String stringInTxt) {
		this.stringInTxt = stringInTxt;
		addRecipe();
		Main.craftFileSecond.add(this);
	}
	
	public ItemStack getCraftedItem() {
		
	    String[] words2 = stringInTxt.split(","); // Разбиение строки на слова с помощью разграничителя (пробел)
	    String[] words = stringInTxt.split(","); // Разбиение строки на слова с помощью разграничителя (пробел)
	    String itemName = words[0].substring(0, words[0].indexOf("/")-1);
		String[] numberg = words[0].substring(words[0].indexOf("/")+2).split(" ");
		for (Object object : Item.itemRegistry) {
			Item item = (Item) object;
				if (item != null && item.getUnlocalizedName() != null && (item.getUnlocalizedName().equals("item." + itemName))
						|| item.getUnlocalizedName().equals("tile." + itemName)) {
					if(numberg.length == 2) {
						return new ItemStack(item, Integer.parseInt(numberg[1]), Integer.parseInt(numberg[0]));
					}else {
						return new ItemStack(item, Integer.parseInt(numberg[0]), 0);
					}
			}
		}
	    
	    
		return new ItemStack(Items.bed);
	}
	
	public void addRecipe() {
	    String[] words = stringInTxt.split(","); // Разбиение строки на слова с помощью разграничителя (пробел)
	    Item is = null;
	    ItemStack[] stack = new ItemStack[words.length - 1];
		      for(int i = 1; i < words.length; i++) {
		    	  String[] wordsCount = words[i].split("\\s"); // Разбиение строки на слова с помощью разграничителя (пробел)
		  	    String itemName = words[i].substring(0, words[i].indexOf("/")-1);
				String[] numberg = words[i].substring(words[i].indexOf("/")+2).split(" ");
		  		for (Object object : Item.itemRegistry) {
		  			is = (Item) object;
		  			
		  			
					if (is != null && is.getUnlocalizedName() != null && (is.getUnlocalizedName().equals("item." + itemName)|| is.getUnlocalizedName().equals("tile." + itemName ))) {
						if(numberg.length == 2 ) {
							stack[i-1] = new ItemStack(is, Integer.parseInt(numberg[1]), Integer.parseInt(numberg[0]));
								
						}else {
							stack[i-1] = new ItemStack(is, Integer.parseInt(numberg[0]), 0);
							
						}
					}  
		      }
		}
			Main.recipeSecond.put(this, stack);
	}
	
	
}
