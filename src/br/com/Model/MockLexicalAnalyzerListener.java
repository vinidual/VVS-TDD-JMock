package br.com.Model;

public interface MockLexicalAnalyzerListener {
	
	public abstract void fileExist(String msg);
	
	public abstract void fileValidateExtension(String msg);
	
}