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
		lae.addListener(mock);
		ctx.checking(new Expectations(){{
			oneOf(mock).fileExist("file exist!");
		}});
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
		
	}
	
	@Test
	public void fileInvalidExtension(){
		
	}
	
	@Test
	public void fileReaded(){
		
	}
	
	@Test
	public void fileHasLexicalError(){
		
	}
	
	@Test
	public void fileIsCorrect(){
		
	}
	
	
	/*
	@Test
	public void fileInexistent() {
		file = new FileCheck("file");
		assertFalse(file.exists());
	}
	
	@Test
	public void fileExist() {
		file = new FileCheck("src/testSuccess.cm");
		assertTrue(file.exists());
	}

	
	@Test
	public void fileExtensionInvalid() {
		file = new FileCheck("testInvalid.txt");
		assertFalse(file.verifyExtension());
	}
	
	@Test
	public void fileExtensionValid() {
		file = new FileCheck("testValid.cm");
		assertTrue(file.verifyExtension());
	}
	
	@Test
	public void fileScannerCreated() {
		file = new FileCheck("src/file.cm");
		assertTrue(fs.createFileScanner(file.getFile()));
	}
	
	@Test
	public void validCaracterVectorReaded(){
		char[] expected = {'1','2','3',';'};
		file = new FileCheck("src/testNumValid.cm");
		fs.createFileScanner(file.getFile());
		assertArrayEquals(expected, fs.readFile());
	}
	
	@Test
	public void validTokenReserved(){
		file = new FileCheck("src/file.cm");
		fs.createFileScanner(file.getFile());
		assertTrue(la.checkTokenReserved("int"));
	}
	
	@Test
	public void lexicalError1() {
		file = new FileCheck("src/file.cm");
		fs.createFileScanner(file.getFile());
		char[] buffer = {'#','$','a','\n'};
		la.lexicalAnalysis(buffer);
		expected.clear();
		expected.add("ERR");
		expected.add("ERR");
		expected.add("ID");
		assertEquals(expected, la.getResult());
	}
	
	@Test
	public void lexicalError2() {
		file = new FileCheck("src/file.cm");
		fs.createFileScanner(file.getFile());
		char[] buffer = {'#','$', '~','a','\n','&'};
		la.lexicalAnalysis(buffer);
		expected.clear();
		expected.add("ERR");
		expected.add("ERR");
		expected.add("ID");
		System.out.println(la.getError());
		assertFalse(la.getError().compareTo("Successfull Analysis")==0);
	}
	
	@Test
	public void successfullAnalysis1() {
		file = new FileCheck("src/file.cm");
		fs.createFileScanner(file.getFile());
		char[] buffer = fs.readFile();
		la.lexicalAnalysis(buffer);
		expected.clear();
		expected.add("ID");
		expected.add("ATR");
		expected.add("NUM");
		expected.add("COMPT");
		assertEquals(expected, la.getResult());
	}
	
	@Test
	public void successfullAnalysis2() {
		file = new FileCheck("src/file.cm");
		fs.createFileScanner(file.getFile());
		char[] buffer = fs.readFile();
		la.lexicalAnalysis(buffer);
		assertTrue(la.getError().compareTo("Successfull Analysis")==0);
	}
	*/
	
	

}
