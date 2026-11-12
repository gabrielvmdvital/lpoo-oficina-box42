package br.upe.oficinabox42.model;

import br.upe.oficinabox42.exceptions.ValorInvalidoException;
import java.time.Year;

public class Veiculo {
    private String placa;
    private String modelo;
    private int ano;

    public Veiculo(String placa, String modelo, int ano) throws ValorInvalidoException {
        setPlaca(placa);
        setModelo(modelo);
        setAno(ano);
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) throws ValorInvalidoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new ValorInvalidoException("A placa do veículo não pode ser vazia.");
        }
        this.placa = placa.trim();
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) throws ValorInvalidoException {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new ValorInvalidoException("O modelo do veículo não pode ser vazio.");
        }
        this.modelo = modelo.trim();
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) throws ValorInvalidoException {
        int anoAtual = Year.now().getValue();
        if (ano <= 0) {
            throw new ValorInvalidoException("O ano do veículo deve ser positivo.");
        }
        if (ano > anoAtual) {
            throw new ValorInvalidoException("O ano do veículo (" + ano + ") não pode ser superior ao ano atual (" + anoAtual + ").");
        }
        this.ano = ano;
    }

    public boolean temPlaca(String placaConsulta) {
        if (placaConsulta == null) {
            return false;
        }
        return this.placa.equalsIgnoreCase(placaConsulta.trim());
    }

    public void exibirInformacoes() {
        System.out.printf("Placa: %s | Modelo: %s | Ano: %d%n", placa, modelo, ano);
    }

    @Override
    public String toString() {
        return String.format("%s (%s - %d)", modelo, placa, ano);
    }
}
