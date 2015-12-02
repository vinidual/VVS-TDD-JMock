# VVS-TDD-JMock

Trabalho realizado para a disciplina de Validação e Verificação de Software ministrada pelo professor Dr. Fábio Fagundes Silveira na Unifesp campus de São José dos Campos. Atividade realizada pelos alunos:

  - Karen Saori Suzuki
  - Vinícius Duarte de Almeida

LexicalAnalysisC- é um programa em Java para realizar a análise léxica da [linguagem C-](http://www.cs.dartmouth.edu/~cs57/Project/C-%20Spec.pdf) implementado com metodologia TDD utilizando o framework [JUnit4](http://junit.org/) e [JMock](http://jmock.org).

O objetivo desse trabalho é colocar em prática o TDD com utilização do JMock para entender seu funcionamento e filosofia no desenvolvimento de software. Para seu desenvolvimento foi utilizado a IDE [Eclipse](https://projects.eclipse.org/releases/mars) Mars.

## O Problema

A classe que executa a análise léxica possui a depêndencia de um `Listener` que recebe o resultado de cada método da análise.  A análise léxica da linguagem C- é executada a partir de um arquivo de entrada com extensão .cm. Devemos guiar uma implementaçao para que possamos ter um objeto de teste desacoplado da aplicaçao, assim podemos simular cenários de teste em ambiente controlado.

## Motivaçao

Utilizando JMock podemos avaliar o que realmente é necessário ser implementado na nossa depência, guiando a implementaçao de classes melhor estruturadas. Além disso, com JMock é possível realizar os testes sem se preocupar com acesso as dependências, pois utilizamos objetos mock desacoplados que substituem as depências sem interferir no fluxo da aplicaçao.

## Estrutura

O programa possui 3 classes para essa aplicaçao:

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
- Arquivo com extensao válida.
- Arquivo com extensao inválida.
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
    
Já estamos citando um passo do processo de refatoraçao, pois inicialmente todos os testes instânciavam seu próprio mock, assim criamos somente uma instância para todos os testes.

O método `.addListener(mock)` recebe e inicializa o  `Listener`. 
O método `.fileExist(String.class)` é uma implementaçao necessária da `interface` que mockamos. Além disso, é esperado pelo nosso teste que esse método seja invocado uma única vez.

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
    	
### Arquivo com extensao válida

Verifica se a extensao do arquivo é correta.

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

O interessante aqui é observar que podemos criar quantos casos de testes julgarmos necessários. o método `atLeast(1)` indica que deve haver, no mínimo, 1 ocorrência válida para `"valid file extension!"`. Além disso, nunca deve ocorrer erro de extensao inválida nesse teste.

### Arquivo com extensao inválida

O contrário do que ocorre no teste anterior, aqui estamos interessados em obter resultados válidos para arquivos com extensao inválida, ou seja, o teste é correto se o resultado retornado é de extensao inváida. 

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

### Análise léxica com erros

### Análise léxica com sucesso































