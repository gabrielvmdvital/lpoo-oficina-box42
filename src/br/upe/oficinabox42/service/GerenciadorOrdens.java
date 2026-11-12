package br.upe.oficinabox42.service;

import br.upe.oficinabox42.exceptions.CapacidadeExcedidaException;
import br.upe.oficinabox42.exceptions.ValorInvalidoException;
import br.upe.oficinabox42.interfaces.Buscavel;
import br.upe.oficinabox42.model.OrdemServico;

public class GerenciadorOrdens implements Buscavel {
    private OrdemServico[] ordens;
    private int quantidadeOrdens;

    public GerenciadorOrdens(int capacidade) throws ValorInvalidoException {
        if (capacidade <= 0) {
            throw new ValorInvalidoException("A capacidade do gerenciador de ordens deve ser positiva.");
        }
        this.ordens = new OrdemServico[capacidade];
        this.quantidadeOrdens = 0;
    }

    public void adicionarOrdem(OrdemServico ordem)
            throws CapacidadeExcedidaException, ValorInvalidoException {
        if (ordem == null) {
            throw new ValorInvalidoException("A ordem de serviço não pode ser nula.");
        }
        if (buscarPorNumero(ordem.getNumero()) != null) {
            throw new ValorInvalidoException("Já existe uma ordem de serviço cadastrada com o número " + ordem.getNumero() + ".");
        }
        if (this.quantidadeOrdens >= this.ordens.length) {
            throw new CapacidadeExcedidaException("Capacidade máxima do gerenciador (" + this.ordens.length + " ordens) atingida.");
        }

        this.ordens[this.quantidadeOrdens] = ordem;
        this.quantidadeOrdens++;
    }

    public void listarOrdens() {
        if (quantidadeOrdens == 0) {
            System.out.println("Nenhuma ordem de serviço cadastrada no momento.");
            return;
        }
        System.out.println("\n--- LISTAGEM DE ORDENS DE SERVIÇO (" + quantidadeOrdens + "/" + ordens.length + ") ---");
        for (int i = 0; i < quantidadeOrdens; i++) {
            ordens[i].exibirResumo();
        }
    }

    public OrdemServico buscarPorNumero(int numero) {
        for (int i = 0; i < quantidadeOrdens; i++) {
            if (ordens[i].getNumero() == numero) {
                return ordens[i];
            }
        }
        return null;
    }

    public void buscarPorPlaca(String placa) throws ValorInvalidoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new ValorInvalidoException("A placa informada para busca não pode ser vazia.");
        }
        String placaFormatada = placa.trim();
        int encontradas = 0;
        System.out.println("\n--- RESULTADO DA BUSCA POR PLACA: \"" + placaFormatada + "\" ---");
        for (int i = 0; i < quantidadeOrdens; i++) {
            if (ordens[i].getVeiculo().temPlaca(placaFormatada)) {
                ordens[i].exibirResumo();
                encontradas++;
            }
        }
        if (encontradas == 0) {
            System.out.println("Nenhuma ordem encontrada para o veículo com a placa: " + placaFormatada);
        } else {
            System.out.println("Total de ordens encontradas: " + encontradas);
        }
    }

    @Override
    public void buscarPorInicioDoNome(String inicio) throws ValorInvalidoException {
        if (inicio == null || inicio.trim().isEmpty()) {
            throw new ValorInvalidoException("O texto para busca por início do nome não pode ser vazio.");
        }
        String prefixo = inicio.trim().toLowerCase();
        int encontradas = 0;
        System.out.println("\n--- RESULTADO DA BUSCA POR INÍCIO DO NOME: \"" + inicio.trim() + "\" ---");
        for (int i = 0; i < quantidadeOrdens; i++) {
            String nomeCliente = ordens[i].getNomeCliente().trim().toLowerCase();
            if (nomeCliente.startsWith(prefixo)) {
                ordens[i].exibirResumo();
                encontradas++;
            }
        }
        if (encontradas == 0) {
            System.out.println("Nenhuma ordem encontrada para clientes com nome iniciando por: \"" + inicio.trim() + "\"");
        } else {
            System.out.println("Total de ordens encontradas: " + encontradas);
        }
    }

    public int getQuantidadeOrdens() {
        return quantidadeOrdens;
    }

    public int getCapacidade() {
        return ordens.length;
    }

    public OrdemServico[] getOrdens() {
        return ordens;
    }
}

