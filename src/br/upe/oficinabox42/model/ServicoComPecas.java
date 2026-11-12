package br.upe.oficinabox42.model;

import br.upe.oficinabox42.exceptions.ValorInvalidoException;

public class ServicoComPecas extends Servico {
    private double valorPecas;

    public ServicoComPecas(String descricao, double valorBase, double valorPecas) throws ValorInvalidoException {
        super(descricao, valorBase);
        setValorPecas(valorPecas);
    }

    public double getValorPecas() {
        return valorPecas;
    }

    public void setValorPecas(double valorPecas) throws ValorInvalidoException {
        if (valorPecas < 0) {
            throw new ValorInvalidoException("O valor das peças não pode ser negativo.");
        }
        this.valorPecas = valorPecas;
    }

    @Override
    public double calcularPrecoFinal() {
        return getValorBase() + valorPecas;
    }

    @Override
    public void exibirInformacoes() {
        System.out.printf("  - [Com peças] %s | Base: R$ %.2f | Peças: R$ %.2f | Preço: R$ %.2f%n",
                getDescricao(), getValorBase(), valorPecas, calcularPrecoFinal());
    }

    @Override
    public String toString() {
        return String.format("[Com peças] %s (Base: R$ %.2f, Peças: R$ %.2f) = R$ %.2f",
                getDescricao(), getValorBase(), valorPecas, calcularPrecoFinal());
    }
}

