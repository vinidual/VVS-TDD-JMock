package br.com.Model;

import java.io.File;

public class LexicalAnalyzerExecutor {
	
	private MockLexicalAnalyzerListener lal;
	private File file;
	
	public void addListener(MockLexicalAnalyzerListener listener){
		this.lal = listener;
	}

	public void fileExist(String string) {
		file = new File(string);
		if( file.exists() )
			lal.fileExist("file exist!");
		else
			lal.fileExist("file not found!");
	}

}
