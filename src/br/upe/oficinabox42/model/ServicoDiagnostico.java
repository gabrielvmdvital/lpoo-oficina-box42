package br.upe.oficinabox42.model;

import br.upe.oficinabox42.exceptions.ValorInvalidoException;

public class ServicoDiagnostico extends Servico {
    private boolean possuiReparo;

    public ServicoDiagnostico(String descricao, double valorBase, boolean possuiReparo)
            throws ValorInvalidoException {
        super(descricao, valorBase);
        this.possuiReparo = possuiReparo;
    }

    public boolean isPossuiReparo() {
        return possuiReparo;
    }

    public void setPossuiReparo(boolean possuiReparo) {
        this.possuiReparo = possuiReparo;
    }

    @Override
    public double calcularPrecoFinal() {
        if (possuiReparo) {
            return getValorBase() * 0.5;
        }
        return getValorBase();
    }

    @Override
    public void exibirInformacoes() {
        System.out.printf("  - [Diagnóstico] %s | Base: R$ %.2f | Com reparo posterior: %s | Preço: R$ %.2f%n",
                getDescricao(), getValorBase(), possuiReparo ? "Sim (desconto 50%)" : "Não", calcularPrecoFinal());
    }

    @Override
    public String toString() {
        return String.format("[Diagnóstico] %s (Base: R$ %.2f, Reparo: %s) = R$ %.2f",
                getDescricao(), getValorBase(), possuiReparo ? "Sim (-50%)" : "Não", calcularPrecoFinal());
    }
}

