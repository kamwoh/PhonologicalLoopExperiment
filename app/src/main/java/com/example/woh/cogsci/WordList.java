package com.example.woh.cogsci;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

public class WordList{

	protected static ArrayList<String> task1 = new ArrayList<>();
	protected static ArrayList<Stirng> task2 = new ArrayList<>();
	private Random r = new Random();
	
	public void loadFile(int experiment){
		String file = "wordsE"+experiment+".txt";
		loadList(file);
	}

	private void loadList(String file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			int toRead = r.nextInt(Integer.parseInt(line))+1;

			//skip pass the unwanted list
			for(int i=0;i<toRead-1;i++){line = br.readLine();line = br.readLine();}
			//
			
			for(String s:br.readLine().split(", ") task1.add(s);
			for(String s:br.readLine().split(", ") task2.add(s);
			br.close();
		}catch(FileNotFoundException e){
			/*
				ABORT MISSION
				NO WORD LIST FOUND	
			*/
		}catch(IOException e){
			// ERROR ???
		}
	}

}
