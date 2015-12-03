# VVS-TDD-JMock

Trabalho realizado para a disciplina de Validação e Verificação de Software ministrada pelo professor Dr. Fábio Fagundes Silveira na Unifesp campus de São José dos Campos. Atividade realizada pelos alunos:

  - Karen sãori Suzuki
  - Vinícius Duarte de Almeida

LexicalAnalysisC- é um programa em Java para realizar a análise léxica da [linguagem C-](http://www.cs.dartmouth.edu/~cs57/Project/C-%20Spec.pdf) implementado com metodologia TDD utilizando o framework [JUnit4](http://junit.org/) e [JMock](http://jmock.org).

O objetivo desse trabalho é colocar em prática o TDD com utilização do JMock para entender seu funcionamento e filosofia no desenvolvimento de software. Para seu desenvolvimento foi utilizado a IDE [Eclipse](https://projects.eclipse.org/releases/mars) Mars.

## O Problema

A classe que executa a análise léxica possui a depêndencia de um `Listener` que recebe o resultado de cada método da análise.  A análise léxica da linguagem C- é executada a partir de um arquivo de entrada com extensão .cm. Devemos guiar uma implementação para que possamos ter um objeto de teste desacoplado da aplicação, assim podemos simular cenários de teste em ambiente controlado.

## Motivação

Utilizando JMock podemos avaliar o que realmente é necessário ser implementado na nossa depência, guiando a implementação de classes melhor estruturadas. Além disso, com JMock é possível realizar os testes sem se preocupar com acesso as dependências, pois utilizamos objetos mock desacoplados que substituem as depências sem interferir no fluxo da aplicação.

## Estrutura

O programa possui 3 classes para essa aplicação:

- `LexicalAnalyzerExecutor.java`: reponsável por realizar a análise léxica sobre o arquivo de entrada.
- `LexicalAnalyzerListener.java`: responsável por receber os resultados das etapas da análise léxica.
- `TestLexicalAnalyzerJMock.java`: classe responsável por realizar os testes sobre a classe que possui a depêndencia.

Além disso, existem dois arquivos que foram usados para testes: `file_error.cm` e `file.cm`.

## Funcionamento

A classe responsável pela análise léxica - `LexicalAnalyzerExecutor.java` - possui dependência de uma classe que recebe informações das etapas de análise do arquivo de entrada - `LexicalAnalyzerListener.java`. 

Nesse caso devemos trocar a depêndencia  por um `mock object` e enviá-lo para a classe que está sendo testada para verificar o que está sendo retornado.

Para isso é necessário criar uma `interface` que possa substituir a depêndencia. Assim vamos mostrar gradualmente como essa interface é implementada.

## Testes 

A `Task list` utilizada foi a seguinte:

- Arquivo existente.
- Arquivo inexistente.
- Arquivo com extensão válida.
- Arquivo com extensão inválida.
- Arquivo de entrada lido com sucesso.
- Análise léxica com erros.
- Análise léxica com sucesso.

### Arquivo existente

O teste para arquivo existente é o seguinte:

      @Test
    	public void fileExist(){
    		ctx.checking(new Expectations(){{
    			oneOf(mock).fileExist("file exists!");
    		}});
    		lae.addListener(mock);
    		lae.fileExist("src/file.cm");
    	}
    	
Temos no escopo global a instância do nosso mock: 

    final LexicalAnalyzerListener mock = ctx.mock(LexicalAnalyzerListener.class);
    
Já estamos citando um passo do processo de refatoração, pois inicialmente todos os testes instânciavam seu próprio mock, assim criamos somente uma instância para todos os testes.

O método `.addListener(mock)` recebe e inicializa o  `Listener`. 
O método `.fileExist(String.class)` é uma implementação necessária da `interface` que mockamos. Além disso, é esperado pelo nosso teste que esse método seja invocado uma única vez.

### Arquivo inexistente

O teste verifica a inexistência para um dado arquivo.

      @Test
    	public void fileNotExist(){
    		lae.addListener(mock);
    		ctx.checking(new Expectations(){{
    			oneOf(mock).fileExist("file not found!");
    		}});
    		lae.fileExist("file.cm");
    	}
    	
Aqui é esperado que o método `.fileExists(String.class)` seja executado uma única vez recebendo a string `"file not found!"`.
    	
### Arquivo com extensão válida

Verifica se a extensão do arquivo é correta.

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

O interessante aqui é observar que podemos criar quantos casos de testes julgarmos necessários. o método `atLeast(1)` indica que deve haver, no mínimo, 1 ocorrência válida para `"valid file extension!"`. Além disso, nunca deve ocorrer erro de extensão inválida nesse teste.

### Arquivo com extensão inválida

O contrário do que ocorre no teste anterior, aqui estamos interessados em obter resultados válidos para arquivos com extensão inválida, ou seja, o teste é correto se o resultado retornado é de extensão inváida. 

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

Assim, podemos criar vários casos de teste para validar extensoes inválidas. Aqui ocorre que será executado, ao menos 1 vez, a chamada do método `.fileValidateExtension(String.class)` do nosso mock com a string `"invalid file extension"` e nunca com string `"valid file extension!"`.

### Arquivo de entrada lido com sucesso

Teste que verifica se foi criado o `buffer` que armazena os caracteres lidos do arquivo de entrada.

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
    	
Para que seja possível ler o arquivo de entrada, ele deve ser válido, ou seja, deve existir, `.fileExist("file exists!")`, deve ter a extensão válida, `.fileValidateExtension("valid file extension!")`, e devrmos conseguir lê-lo, `.fileReadable("file read successfully!")`. Também devemos ter que nunca deve ser gerado erro ou não ser possível ler esse arquivo, para isso temos as duas últimas `Expectations` no nosso `checking`.

### Análise Léxica com erros

Teste que deve verificar a ocorrência de erros durante a Análise Léxica.

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

Aqui é importante que ocorra ao menos uma inserção de `Token` de erro, `"ERR"`, a instrução de `ignoring(mock)` vai ignorar qualquer outra `Expectations` que venha a ocorrer além das instruídas, pois podemos ter a adição de vários outros `Tokens` válidos, como `ID` e `NUM`, por exemplo.

### Análise Léxica com sucesso

Teste que verifica a ocorrência de Análise Léxica correta, nesse caso o arquivo de entrada não deve conter erros.

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
    	
Similar a estratégia utilizada no teste anterior, aqui nunca deve ocorrer a inserção de `Token` de erro, `.addToken("ERR")`.

Para os teste que incluem o uso de arquivo, como leitura e análise dos caracteres, utilizamos dois arquivos de teste contidos na pasta `src` da aplicação: `file.cm` e `file_error.cm`, que se referem, respectivamente, à análise com sucesso e análise com erros.
    	
## Interface

A classe testada `LexicalAnalyzerExecutor.java` possui a dependência `LexicalAnalyzerListener.java`, assim geramos uma interface dessa classe para testar as respostas obtidas da classe de teste em um ambiente controlado e de forma desacoplada do restante do fluxo da aplicação, gerando a seguinte `Interface`:

      public interface LexicalAnalyzerListener {
      	
      	public abstract boolean fileExist(String msg);
      	
      	public abstract boolean fileValidateExtension(String msg);
      
      	public abstract boolean fileReadable(String string);
      
      	public abstract boolean fileError(String message);
      
      	public abstract void addToken(String string);
      	
      }
      
Notamos que a `Interface` nos guia uma implementação que utilizamos somente métodos que realmente são necessários a nossa aplicação, o que permite criar um código mais consistente e organizado.
      
## Casos de Teste

Além dos casos vistos no próprio código, os arquivos podem ser editados e testados para diferentes entradas. 

Assim, verificamos alguns casos que engloba valores limites (ASCII) dos símbolos não pertencentes à linguagem.

Além da combinação de caracteres diversos pertencentes a linguagem, independente de espaçamento, tabulação ou quebra de linha.

## Refatoração

Foram poucas necessárias, foram colocados os seguintes objeto no escopo global da classe `TesteLexicalAnalyzerJMock.java`:

    final LexicalAnalyzerListener mock = ctx.mock(LexicalAnalyzerListener.class);
    String file;

## Conclusões

Algumas conclusões observadas:

- Com JMock podemos verificar quais os métodos que realmente testamos da nossa dependência, ou seja, quais realmente são utilizados pela classe testada.

- Não importa o que a dependência vai fazer com a resposta obtida, me importa somente o que é retornado.

- Com a implementacao de interface conseguidos testar a dependência de forma desacoplada do código, em ambiente controlado. 

## Referências

- JMock API: http://www.jmock.org/

- Vídeo aula sobre JMock, autor: Eduardo Guerra: https://www.youtube.com/watch?v=C1t4Zabi6S

































