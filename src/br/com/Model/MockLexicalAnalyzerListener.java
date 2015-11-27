package br.com.Model;

public interface MockLexicalAnalyzerListener {
	
	public abstract boolean fileExist(String msg);
	
	public abstract boolean fileValidateExtension(String msg);

	public abstract boolean fileReadable(String string);

	public abstract boolean fileError(String message);

	public abstract void putError(String error);

	public abstract void addToken(String string);
	
}