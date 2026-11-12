package br.upe.oficinabox42.model;

import br.upe.oficinabox42.exceptions.CapacidadeExcedidaException;
import br.upe.oficinabox42.exceptions.OrdemFinalizadaException;
import br.upe.oficinabox42.exceptions.OrdemVaziaException;
import br.upe.oficinabox42.exceptions.ValorInvalidoException;
import br.upe.oficinabox42.interfaces.Descontavel;

public class OrdemServico implements Descontavel {
    private int numero;
    private String nomeCliente;
    private Veiculo veiculo;
    private Servico[] servicos;
    private int quantidadeServicos;
    private double percentualDesconto;
    private EstadoOrdem estado;

    public OrdemServico(int numero, String nomeCliente, Veiculo veiculo, int capacidadeMaximaServicos)
            throws ValorInvalidoException {
        if (numero <= 0) {
            throw new ValorInvalidoException("O número da ordem deve ser positivo.");
        }
        if (nomeCliente == null || nomeCliente.trim().isEmpty()) {
            throw new ValorInvalidoException("O nome do cliente não pode ser vazio.");
        }
        if (veiculo == null) {
            throw new ValorInvalidoException("O veículo da ordem não pode ser nulo.");
        }
        if (capacidadeMaximaServicos <= 0) {
            throw new ValorInvalidoException("A capacidade máxima de serviços deve ser maior que zero.");
        }

        this.numero = numero;
        this.nomeCliente = nomeCliente.trim();
        this.veiculo = veiculo;
        this.servicos = new Servico[capacidadeMaximaServicos];
        this.quantidadeServicos = 0;
        this.percentualDesconto = 0.0;
        this.estado = EstadoOrdem.ABERTA;
    }

    public void adicionarServico(Servico servico)
            throws CapacidadeExcedidaException, OrdemFinalizadaException, ValorInvalidoException {
        if (this.estado == EstadoOrdem.FINALIZADA) {
            throw new OrdemFinalizadaException("Tentativa de adicionar serviço em ordem FINALIZADA (nº " + numero + ").");
        }
        if (servico == null) {
            throw new ValorInvalidoException("O serviço a ser adicionado não pode ser nulo.");
        }
        if (this.quantidadeServicos >= this.servicos.length) {
            throw new CapacidadeExcedidaException("Capacidade máxima de serviços (" + this.servicos.length + ") atingida na ordem nº " + numero + ".");
        }

        this.servicos[this.quantidadeServicos] = servico;
        this.quantidadeServicos++;
    }

    public double calcularSubtotal() {
        double subtotal = 0.0;
        for (int i = 0; i < quantidadeServicos; i++) {
            // Chamada polimórfica: sem instanceof nem condicionais por tipo!
            subtotal += servicos[i].calcularPrecoFinal();
        }
        return subtotal;
    }

    public double calcularTotal() {
        double subtotal = calcularSubtotal();
        return subtotal * (1.0 - (percentualDesconto / 100.0));
    }

    public double calcularValorDesconto() {
        return calcularSubtotal() * (percentualDesconto / 100.0);
    }

    public void finalizar() throws OrdemVaziaException, OrdemFinalizadaException {
        if (this.estado == EstadoOrdem.FINALIZADA) {
            throw new OrdemFinalizadaException("A ordem nº " + numero + " já está FINALIZADA.");
        }
        if (this.quantidadeServicos == 0) {
            throw new OrdemVaziaException("Não é possível finalizar a ordem nº " + numero + " sem nenhum serviço cadastrado.");
        }
        this.estado = EstadoOrdem.FINALIZADA;
    }

    @Override
    public double aplicarDesconto(double percentual)
            throws ValorInvalidoException, OrdemFinalizadaException {
        if (this.estado == EstadoOrdem.FINALIZADA) {
            throw new OrdemFinalizadaException("Tentativa de alterar desconto em ordem FINALIZADA (nº " + numero + ").");
        }
        if (percentual < 0.0 || percentual > 100.0) {
            throw new ValorInvalidoException("O percentual de desconto deve estar entre 0 e 100 (inclusive). Valor informado: " + percentual);
        }
        this.percentualDesconto = percentual;
        return calcularTotal();
    }

    public void exibirResumo() {
        System.out.println("==================================================");
        System.out.printf("ORDEM DE SERVIÇO Nº %d | Estado: %s%n", numero, estado);
        System.out.printf("Cliente: %s%n", nomeCliente);
        System.out.print("Veículo: ");
        veiculo.exibirInformacoes();
        System.out.println("Serviços prestados:");
        if (quantidadeServicos == 0) {
            System.out.println("  (Nenhum serviço cadastrado)");
        } else {
            for (int i = 0; i < quantidadeServicos; i++) {
                servicos[i].exibirInformacoes();
            }
        }
        double subtotal = calcularSubtotal();
        double valorDesc = calcularValorDesconto();
        double total = calcularTotal();
        System.out.println("--------------------------------------------------");
        System.out.printf("Subtotal: R$ %.2f%n", subtotal);
        System.out.printf("Desconto: %.2f%% (-R$ %.2f)%n", percentualDesconto, valorDesc);
        System.out.printf("TOTAL:    R$ %.2f%n", total);
        System.out.println("==================================================");
    }

    public int getNumero() {
        return numero;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Servico[] getServicos() {
        return servicos;
    }

    public int getQuantidadeServicos() {
        return quantidadeServicos;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public EstadoOrdem getEstado() {
        return estado;
    }

    public int getCapacidadeMaxima() {
        return servicos.length;
    }

    public boolean isFinalizada() {
        return estado == EstadoOrdem.FINALIZADA;
    }
}

