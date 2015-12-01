package br.com.Test;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import br.com.Model.LexicalAnalyzerExecutor;
import br.com.Model.LexicalAnalyzerListener;

public class TestLexicalAnalyzerJMock {
	
	private LexicalAnalyzerExecutor lae;
	
	@Rule 
	public JUnitRuleMockery ctx = new JUnitRuleMockery();
	
	@Before
	public void initLexicalAnalyzer(){
		lae = new LexicalAnalyzerExecutor();
	}
	
	final LexicalAnalyzerListener mock = ctx.mock(LexicalAnalyzerListener.class);
	String file;
	
	@Test
	public void fileExist(){
		ctx.checking(new Expectations(){{
			oneOf(mock).fileExist("file exists!");
		}});
		lae.addListener(mock);
		lae.fileExist("src/file.cm");
	}
	
	@Test
	public void fileNotExist(){
		lae.addListener(mock);
		ctx.checking(new Expectations(){{
			oneOf(mock).fileExist("file not found!");
		}});
		lae.fileExist("file.cm");
	}
	
	@Test
	public void fileValidExtension(){
		ctx.checking(new Expectations(){{
			atLeast(1).of(mock).fileValidateExtension("valid file extension!");
			never(mock).fileValidateExtension("invalid file extension!");
		}});
		lae.addListener(mock);
		lae.validateExtension("anything.cm");
		lae.validateExtension(".cm");
		lae.validateExtension("anything.again.cm");
		lae.validateExtension("I.don't.care.about.my.filename!#@.cm");
		lae.validateExtension("1125653.cm");
		lae.validateExtension("sas1254.cm");
		lae.validateExtension("###8**DIE!!@@.cm");
	}
	
	@Test
	public void fileInvalidExtension(){
		ctx.checking(new Expectations(){{
			atLeast(1).of(mock).fileValidateExtension("invalid file extension!");
			never(mock).fileValidateExtension("valid file extension!");
		}});
		lae.addListener(mock);
		lae.validateExtension("anything.txt");
		lae.validateExtension(".c");
		lae.validateExtension("filecm");
		lae.validateExtension("file.cm.cm.cm.cm.cm.cm.cn");
		lae.validateExtension("1125653");
		lae.validateExtension(".cm.java");
		lae.validateExtension("#1;A!@.8");
	}
	
	@Test
	public void fileReaded(){
		lae.addListener(mock);
		file = "src/file.cm";
		ctx.checking(new Expectations(){{
			oneOf(mock).fileExist("file exists!");
			oneOf(mock).fileValidateExtension("valid file extension!");
			oneOf(mock).fileReadable("file read successfully!");
			never(mock).fileReadable("file cannot be read!");
			never(mock).fileError(with(any(String.class)));
		}});
		lae.fileExist(file);
		lae.validateExtension(file);
		lae.fileReader();
	}
	
	@Test
	public void fileHasLexicalError(){
		lae.addListener(mock);
		file = "src/file_error.cm";
		ctx.checking(new Expectations(){{
			oneOf(mock).fileExist("file exists!");
			oneOf(mock).fileValidateExtension("valid file extension!");
			oneOf(mock).fileReadable("file read successfully!");
			atLeast(1).of(mock).addToken("ERR");
			ignoring(mock);
		}});
		lae.fileExist(file);
		lae.validateExtension(file);
		lae.fileReader();
		lae.bufferAnalyzer();
	}
	
	@Test
	public void fileIsCorrect(){
		lae.addListener(mock);
		file = "src/file.cm";
		ctx.checking(new Expectations(){{
			oneOf(mock).fileExist("file exists!");
			oneOf(mock).fileValidateExtension("valid file extension!");
			oneOf(mock).fileReadable("file read successfully!");
			never(mock).addToken("ERR");
			ignoring(mock);
		}});
		lae.fileExist(file);
		lae.validateExtension(file);
		lae.fileReader();
		lae.bufferAnalyzer();
	}

}
