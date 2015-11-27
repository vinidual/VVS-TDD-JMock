package br.com.Test;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import br.com.Model.LexicalAnalyzerExecutor;
import br.com.Model.MockLexicalAnalyzerListener;

public class TestLexicalAnalyzerJMock {
	
	private LexicalAnalyzerExecutor lae;
	
	@Rule 
	public JUnitRuleMockery ctx = new JUnitRuleMockery();
	
	@Before
	public void initLexicalAnalyzer(){
		lae = new LexicalAnalyzerExecutor();
	}
	
	@Test
	public void fileExist(){
		final MockLexicalAnalyzerListener mock = ctx.mock(MockLexicalAnalyzerListener.class);
		ctx.checking(new Expectations(){{
			oneOf(mock).fileExist("file exists!");
		}});
		lae.addListener(mock);
		lae.fileExist("src/file.cm");
	}
	
	@Test
	public void fileNotExist(){
		final MockLexicalAnalyzerListener mock = ctx.mock(MockLexicalAnalyzerListener.class);
		lae.addListener(mock);
		ctx.checking(new Expectations(){{
			oneOf(mock).fileExist("file not found!");
		}});
		lae.fileExist("file.cm");
	}
	
	@Test
	public void fileValidExtension(){
		final MockLexicalAnalyzerListener mock = ctx.mock(MockLexicalAnalyzerListener.class);
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
		final MockLexicalAnalyzerListener mock = ctx.mock(MockLexicalAnalyzerListener.class);
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
		final MockLexicalAnalyzerListener mock = ctx.mock(MockLexicalAnalyzerListener.class);
		lae.addListener(mock);
		String file = "src/file.cm";
		ctx.checking(new Expectations(){{
			oneOf(mock).fileExist("file exists!");
			oneOf(mock).fileValidateExtension("valid file extension!");
			oneOf(mock).fileReadable("file read successfully!");
		}});
		lae.fileExist(file);
		lae.validateExtension(file);
		lae.fileReader();
	}
	
	@Test
	public void fileHasLexicalError(){
		final MockLexicalAnalyzerListener mock = ctx.mock(MockLexicalAnalyzerListener.class);
		lae.addListener(mock);
		String file = "src/file_error.cm";
		ctx.checking(new Expectations(){{
			atLeast(1).of(mock).putError("ERR");
		}});
		lae.fileExist(file);
		lae.validateExtension(file);
		lae.fileReader();
		lae.bufferAnalyzer();
	}
	
	@Test
	public void fileIsCorrect(){
		final MockLexicalAnalyzerListener mock = ctx.mock(MockLexicalAnalyzerListener.class);
		lae.addListener(mock);
		String file = "src/file.cm";
		ctx.checking(new Expectations(){{
			never(mock).putError("ERR");
		}});
		lae.fileExist(file);
		lae.validateExtension(file);
		lae.fileReader();
		lae.bufferAnalyzer();
	}

}
