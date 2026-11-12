# Oficina Box 42 — Sistema Avançado de Gerenciamento de Ordens de Serviço

Projeto de consolidação da disciplina **Linguagem de Programação Orientada a Objetos (LPOO)** do curso de **Engenharia da Computação** da **Escola Politécnica de Pernambuco (POLI / UPE)**.

---

## Visão Geral

O **Oficina Box 42** é um sistema completo em Java para gerenciamento de oficinas mecânicas, responsável pelo cadastro de veículos, abertura e fechamento de ordens de serviço, inclusão de diferentes modalidades de manutenção (peças, horas trabalhadas e diagnóstico), aplicação de descontos, pesquisas avançadas e validações rígidas de domínio.

O projeto foi construído respeitando integralmente as especificações da disciplina:
* **Uso exclusivo de arrays nativos (`[]`)** para armazenamento interno de ordens e serviços, sem utilização de `ArrayList` ou outras coleções.
* **Cálculo polimórfico estrito**: sem uso de `instanceof`, `getClass()` ou condicionais manuais para distinguir subclasses.
* **Encapsulamento pleno** de atributos e validações através de exceções personalizadas checadas (`Exception`).
* **Interface de Console interativa** com tratamento de erros de leitura para evitar encerramento inesperado.

---

##  Arquitetura e Modelagem OO

O código-fonte está estruturado no pacote base `br.upe.oficinabox42`:

```
src/
└── br/upe/oficinabox42/
    ├── exceptions/                       # Exceções personalizadas de negócio
    │   ├── ValorInvalidoException.java
    │   ├── OrdemVaziaException.java
    │   ├── CapacidadeExcedidaException.java
    │   └── OrdemFinalizadaException.java
    ├── interfaces/                       # Contratos obrigatórios
    │   ├── Descontavel.java
    │   └── Buscavel.java
    ├── model/                            # Modelo de dados e regras de negócio
    │   ├── Veiculo.java
    │   ├── Servico.java                 (classe abstrata)
    │   ├── ServicoComPecas.java         (subclasse de Servico)
    │   ├── ServicoPorHora.java          (subclasse de Servico)
    │   ├── ServicoDiagnostico.java      (subclasse de Servico)
    │   ├── OrdemServico.java            (implementa Descontavel)
    │   └── EstadoOrdem.java             (enum: ABERTA, FINALIZADA)
    ├── service/
    │   └── GerenciadorOrdens.java       (implementa Buscavel)
    └── Main.java                        (Console interativo via terminal)
```

---

##  Aplicação dos Conceitos de POO

### 1. Encapsulamento
* Todos os atributos de todas as classes de domínio (`Veiculo`, `Servico`, `OrdemServico`, `GerenciadorOrdens`) são estritamente `private`.
* O acesso e a mutação de estado ocorrem via *getters* e *setters* que validam as entradas imediatamente.
* Exemplo: em `Veiculo`, a placa não pode ser nula/vazia e o ano deve ser positivo e não superior ao ano corrente (`Year.now().getValue()`), disparando `ValorInvalidoException` caso violado.

### 2. Herança e Sobrescrita (`@Override`)
* A classe base abstrata `Servico` encapsula `descricao` e `valorBase`.
* Suas subclasses (`ServicoComPecas`, `ServicoPorHora` e `ServicoDiagnostico`) reutilizam o construtor base via `super(descricao, valorBase)` e sobrescrevem:
  * `calcularPrecoFinal()`
  * `exibirInformacoes()`

### 3. Classe Abstrata e Métodos Abstratos
* `Servico` é declarada como `public abstract class Servico` e **não pode ser instanciada diretamente**.
* Os métodos `public abstract double calcularPrecoFinal();` e `public abstract void exibirInformacoes();` definem o contrato de execução que cada modalidade de serviço é obrigada a implementar com suas próprias fórmulas.

### 4. Polimorfismo Obrigatório
* Em `OrdemServico`, os serviços são armazenados em um array heterogêneo `Servico[] servicos`.
* O método `calcularSubtotal()` calcula o total somando `servicos[i].calcularPrecoFinal()` dinamicamente em tempo de execução:
  ```java
  public double calcularSubtotal() {
      double subtotal = 0.0;
      for (int i = 0; i < quantidadeServicos; i++) {
          subtotal += servicos[i].calcularPrecoFinal(); // Polimorfismo puro
      }
      return subtotal;
  }
  ```
* Nenhuma checagem manual por tipo (`instanceof`, `getClass()` ou `switch/case`) é realizada no cálculo ou na exibição.

### 5. Interfaces Java
* `Descontavel`: implementada por `OrdemServico`. Define o contrato `aplicarDesconto(double percentual)` que aceita valores de `0.0` a `100.0` e impede alteração em ordens com estado `FINALIZADA`.
* `Buscavel`: implementada por `GerenciadorOrdens`. Define `buscarPorInicioDoNome(String inicio)`, realizando buscas *case-insensitive* ignorando espaços nas extremidades.

### 6. Exceções Personalizadas e Tratamento Robusto
Todas as exceções estendem `Exception` (exceções checadas) e são tratadas na camada de interação:
1. `ValorInvalidoException`: valor base não positivo, peças com custo negativo, horas/valor-hora <= 0, desconto fora de 0-100, placa/nome/modelo vazios, capacidade <= 0, ordem duplicada, veículo nulo, serviço nulo.
2. `OrdemVaziaException`: tentativa de finalizar uma ordem de serviço sem nenhum serviço inserido.
3. `CapacidadeExcedidaException`: tentativa de ultrapassar a capacidade fixa do array de ordens ou de serviços.
4. `OrdemFinalizadaException`: tentativa de adicionar serviços ou alterar desconto de uma ordem já `FINALIZADA`.
* O `Main` trata ainda erros de leitura do console (`NumberFormatException`), garantindo que o programa nunca encerre de forma inesperada diante de entradas inválidas.

---

##  Como Compilar e Executar

Certifique-se de ter o JDK (Java Development Kit) 17 ou superior instalado (o projeto foi verificado no Java 23).

### 1. Compilação
No terminal, dentro da pasta raiz do projeto (`lpoo-oficina-box42`), execute:

```bash
# Compilar todos os arquivos fonte para o diretório bin
javac -d bin -sourcepath src src/br/upe/oficinabox42/Main.java
```

---

### 2. Execução

```bash
java -cp bin br.upe.oficinabox42.Main
```
Permite executar todas as opções do menu interativo no terminal:
```text
=== OFICINA BOX 42 ===
1 - Criar ordem de serviço
2 - Adicionar serviço à ordem
3 - Listar ordens
4 - Buscar ordens por cliente
5 - Buscar ordens por placa
6 - Aplicar desconto
7 - Finalizar ordem
8 - Sair
```

---

## Validação dos Cálculos (Exemplo da Seção 9 do pdf Consolidação Final — Oficina Box 42)

Cenário demonstrado:
* **Ordem nº 101**: Cliente **Ana Silva**, Veículo **Honda Civic (Placa ABC1D23, Ano 2022)**.
* **Serviços**:
  1. *Com peças*: Troca de pastilhas (Base: R\$ 150,00 + Peças: R\$ 200,00) = **R\$ 350,00**
  2. *Por hora*: Reparo elétrico (Base: R\$ 80,00 + 2h × R\$ 100,00/h) = **R\$ 280,00**
  3. *Diagnóstico*: Diagnóstico motor com reparo contratado (Base: R\$ 120,00 × 0,5) = **R\$ 60,00**
* **Subtotal**: R\$ 350,00 + R\$ 280,00 + R\$ 60,00 = **R\$ 690,00**
* **Desconto (10%)**: R\$ 69,00
* **Total**: **R\$ 621,00**
* **Estado pós-finalização**: `FINALIZADA` (bloqueando quaisquer modificações subsequentes).
