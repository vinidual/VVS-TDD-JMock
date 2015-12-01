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

## Testes 

A `Task list` utilizada foi a seguinte:

- Arquivo existente.
- Arquivo inexistente.
- Arquivo com extensao válida.
- Arquivo com extensao inválida.
- Arquivo de entrada lido com sucesso.
- Análise léxica com erros.
- Análise léxica com sucesso.

### Arquivo inexistente































