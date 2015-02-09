package com.GDT.sidm_2014;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.SparseArray;

public class ConversationTextManager {
	InputStream is;
	
	private Vector<String> Conversations;
	private Vector<String> ScamConversations;
	
	public ConversationTextManager(Context context) {		
		Conversations = new Vector<String>();
		ScamConversations = new Vector<String>();
		
		ParseFile(context.getResources().openRawResource(R.raw.conversations), Conversations);
		ParseFile(context.getResources().openRawResource(R.raw.scamconversations), ScamConversations);
	}
	
	private void ParseFile(InputStream input, Vector<String> theList) {
		is = input;
		Scanner fileInput = new Scanner(is);
		
		while(fileInput.hasNextLine()){
			String line = fileInput.nextLine();
			theList.add(line);
		}
		
		fileInput.close();
	}
	
	public Conversation GetConversationText(){
		Random r = new Random();
		Conversation convo = new Conversation(false, Conversations.get(r.nextInt(Conversations.size())), null);
		return convo;
	}
	public Conversation GetScamConversationText(){
		Random r = new Random();
		Conversation convo = new Conversation(true, ScamConversations.get(r.nextInt(ScamConversations.size())), null);
		return convo;
	}
}