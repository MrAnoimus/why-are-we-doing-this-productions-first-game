package com.GDT.sidm_2014;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.SparseArray;

public class ConversationTextManager {
	AssetManager am;
	
	SparseArray<String> Conversations;
	SparseArray<String> ScamConversations;
	
	public ConversationTextManager(Context context) throws FileNotFoundException {
		am = context.getAssets();
		
		Conversations = new SparseArray<String>();
		ScamConversations = new SparseArray<String>();
		
		ParseFile("text/Conversations.txt", Conversations);
		ParseFile("text/ScamConversations.txt", ScamConversations);
	}
	
	private void ParseFile(String filename, SparseArray<String> theList)throws FileNotFoundException {
		File TheFile = new File(filename);
		Scanner fileInput = new Scanner(TheFile);
		
		while(fileInput.hasNextLine()){
			String line = fileInput.nextLine();
			
			theList.put(theList.size(), line);
		}
		
		fileInput.close();
	}

	public String GetText(SparseArray<String> theList){
		Random r = new Random();
		return theList.get(r.nextInt(theList.size()));
	}
}