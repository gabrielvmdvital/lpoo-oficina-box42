package br.upe.oficinabox42.model;

import br.upe.oficinabox42.exceptions.ValorInvalidoException;

public class ServicoPorHora extends Servico {
    private double horasTrabalhadas;
    private double valorHora;

    public ServicoPorHora(String descricao, double valorBase, double horasTrabalhadas, double valorHora)
            throws ValorInvalidoException {
        super(descricao, valorBase);
        setHorasTrabalhadas(horasTrabalhadas);
        setValorHora(valorHora);
    }

    public double getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(double horasTrabalhadas) throws ValorInvalidoException {
        if (horasTrabalhadas <= 0) {
            throw new ValorInvalidoException("A quantidade de horas trabalhadas deve ser maior que zero.");
        }
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public double getValorHora() {
        return valorHora;
    }

    public void setValorHora(double valorHora) throws ValorInvalidoException {
        if (valorHora <= 0) {
            throw new ValorInvalidoException("O valor por hora deve ser maior que zero.");
        }
        this.valorHora = valorHora;
    }

    @Override
    public double calcularPrecoFinal() {
        return getValorBase() + (horasTrabalhadas * valorHora);
    }

    @Override
    public void exibirInformacoes() {
        System.out.printf("  - [Por hora] %s | Base: R$ %.2f | Horas: %.1fh x R$ %.2f | Preço: R$ %.2f%n",
                getDescricao(), getValorBase(), horasTrabalhadas, valorHora, calcularPrecoFinal());
    }

    @Override
    public String toString() {
        return String.format("[Por hora] %s (Base: R$ %.2f, %.1fh x R$ %.2f) = R$ %.2f",
                getDescricao(), getValorBase(), horasTrabalhadas, valorHora, calcularPrecoFinal());
    }
}

