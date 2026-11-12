package br.upe.oficinabox42;

import br.upe.oficinabox42.exceptions.CapacidadeExcedidaException;
import br.upe.oficinabox42.exceptions.OrdemFinalizadaException;
import br.upe.oficinabox42.exceptions.OrdemVaziaException;
import br.upe.oficinabox42.exceptions.ValorInvalidoException;
import br.upe.oficinabox42.model.OrdemServico;
import br.upe.oficinabox42.model.Servico;
import br.upe.oficinabox42.model.ServicoComPecas;
import br.upe.oficinabox42.model.ServicoDiagnostico;
import br.upe.oficinabox42.model.ServicoPorHora;
import br.upe.oficinabox42.model.Veiculo;
import br.upe.oficinabox42.service.GerenciadorOrdens;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static GerenciadorOrdens gerenciador;

    public static void main(String[] args) {
        try {
            gerenciador = new GerenciadorOrdens(20);
        } catch (ValorInvalidoException e) {
            System.err.println("Erro ao inicializar o sistema: " + e.getMessage());
            return;
        }

        boolean executando = true;
        while (executando) {
            exibirMenu();
            int opcao = lerInteiro("Escolha uma opção: ");
            System.out.println();

            switch (opcao) {
                case 1:
                    criarOrdem();
                    break;
                case 2:
                    adicionarServico();
                    break;
                case 3:
                    listarOrdens();
                    break;
                case 4:
                    buscarOrdensPorCliente();
                    break;
                case 5:
                    buscarOrdensPorPlaca();
                    break;
                case 6:
                    aplicarDesconto();
                    break;
                case 7:
                    finalizarOrdem();
                    break;
                case 8:
                    System.out.println("Encerrando o sistema Oficina Box 42. Até logo!");
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida! Escolha um valor entre 1 e 8.");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("=== OFICINA BOX 42 ===");
        System.out.println("1 - Criar ordem de serviço");
        System.out.println("2 - Adicionar serviço à ordem");
        System.out.println("3 - Listar ordens");
        System.out.println("4 - Buscar ordens por cliente");
        System.out.println("5 - Buscar ordens por placa");
        System.out.println("6 - Aplicar desconto");
        System.out.println("7 - Finalizar ordem");
        System.out.println("8 - Sair");
    }

    private static void criarOrdem() {
        System.out.println("--- Criar Ordem de Serviço ---");
        try {
            int numero = lerInteiro("Número da ordem: ");
            if (gerenciador.buscarPorNumero(numero) != null) {
                System.out.println("Erro: Já existe uma ordem cadastrada com o número " + numero + ".");
                return;
            }

            String cliente = lerTexto("Nome do cliente: ");
            String placa = lerTexto("Placa do veículo: ");
            String modelo = lerTexto("Modelo do veículo: ");
            int ano = lerInteiro("Ano do veículo: ");
            int capacidadeServicos = lerInteiro("Capacidade máxima de serviços para esta ordem (ex: 5): ");

            Veiculo veiculo = new Veiculo(placa, modelo, ano);
            OrdemServico novaOrdem = new OrdemServico(numero, cliente, veiculo, capacidadeServicos);
            gerenciador.adicionarOrdem(novaOrdem);

            System.out.println(">> Sucesso: Ordem de serviço nº " + numero + " criada com sucesso!");
        } catch (ValorInvalidoException | CapacidadeExcedidaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void adicionarServico() {
        System.out.println("--- Adicionar Serviço à Ordem ---");
        int numero = lerInteiro("Informe o número da ordem de serviço: ");
        OrdemServico ordem = gerenciador.buscarPorNumero(numero);

        if (ordem == null) {
            System.out.println("Erro: Ordem de serviço nº " + numero + " não encontrada.");
            return;
        }

        if (ordem.isFinalizada()) {
            System.out.println("Erro: A ordem nº " + numero + " está FINALIZADA e não pode receber novos serviços.");
            return;
        }

        System.out.println("Tipo de serviço:");
        System.out.println("1 — Com peças");
        System.out.println("2 — Por hora");
        System.out.println("3 — Diagnóstico");
        int tipo = lerInteiro("Escolha o tipo de serviço (1-3): ");

        try {
            Servico servico = null;
            switch (tipo) {
                case 1: {
                    String desc = lerTexto("Descrição do serviço: ");
                    double valorBase = lerDouble("Valor base (R$): ");
                    double valorPecas = lerDouble("Custo das peças (R$): ");
                    servico = new ServicoComPecas(desc, valorBase, valorPecas);
                    break;
                }
                case 2: {
                    String desc = lerTexto("Descrição do serviço: ");
                    double valorBase = lerDouble("Valor base (R$): ");
                    double horas = lerDouble("Horas trabalhadas: ");
                    double valorHora = lerDouble("Valor por hora (R$): ");
                    servico = new ServicoPorHora(desc, valorBase, horas, valorHora);
                    break;
                }
                case 3: {
                    String desc = lerTexto("Descrição do serviço: ");
                    double valorBase = lerDouble("Valor base (R$): ");
                    boolean possuiReparo = lerBooleano("Houve contratação de reparo posterior? (S/N): ");
                    servico = new ServicoDiagnostico(desc, valorBase, possuiReparo);
                    break;
                }
                default:
                    System.out.println("Erro: Tipo de serviço inválido!");
                    return;
            }

            ordem.adicionarServico(servico);
            System.out.printf(">> Sucesso: Serviço adicionado à ordem nº %d com sucesso! Preço calculado: R$ %.2f%n",
                    numero, servico.calcularPrecoFinal());

        } catch (ValorInvalidoException | CapacidadeExcedidaException | OrdemFinalizadaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarOrdens() {
        gerenciador.listarOrdens();
    }

    private static void buscarOrdensPorCliente() {
        System.out.println("--- Buscar Ordens por Cliente ---");
        try {
            String inicio = lerTexto("Informe o início do nome do cliente: ");
            gerenciador.buscarPorInicioDoNome(inicio);
        } catch (ValorInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void buscarOrdensPorPlaca() {
        System.out.println("--- Buscar Ordens por Placa ---");
        try {
            String placa = lerTexto("Informe a placa do veículo: ");
            gerenciador.buscarPorPlaca(placa);
        } catch (ValorInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void aplicarDesconto() {
        System.out.println("--- Aplicar Desconto ---");
        int numero = lerInteiro("Informe o número da ordem de serviço: ");
        OrdemServico ordem = gerenciador.buscarPorNumero(numero);

        if (ordem == null) {
            System.out.println("Erro: Ordem de serviço nº " + numero + " não encontrada.");
            return;
        }

        try {
            double percentual = lerDouble("Informe o percentual de desconto (0 a 100): ");
            double novoTotal = ordem.aplicarDesconto(percentual);
            System.out.printf(">> Sucesso: Desconto de %.2f%% aplicado com sucesso! Novo total: R$ %.2f%n",
                    percentual, novoTotal);
        } catch (ValorInvalidoException | OrdemFinalizadaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void finalizarOrdem() {
        System.out.println("--- Finalizar Ordem de Serviço ---");
        int numero = lerInteiro("Informe o número da ordem de serviço: ");
        OrdemServico ordem = gerenciador.buscarPorNumero(numero);

        if (ordem == null) {
            System.out.println("Erro: Ordem de serviço nº " + numero + " não encontrada.");
            return;
        }

        try {
            ordem.finalizar();
            System.out.println(">> Sucesso: Ordem de serviço nº " + numero + " FINALIZADA com sucesso!");
            System.out.println("Resumo final da ordem:");
            ordem.exibirResumo();
        } catch (OrdemVaziaException | OrdemFinalizadaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // Métodos auxiliares robustos para leitura segura do console
    private static int lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(linha);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um valor numérico válido.");
            }
        }
    }

    private static String lerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static boolean lerBooleano(String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = scanner.nextLine().trim().toUpperCase();
            if (entrada.equals("S") || entrada.equals("SIM") || entrada.equals("1") || entrada.equals("TRUE")) {
                return true;
            }
            if (entrada.equals("N") || entrada.equals("NAO") || entrada.equals("NÃO") || entrada.equals("0") || entrada.equals("FALSE")) {
                return false;
            }
            System.out.println("Entrada inválida! Digite 'S' para sim ou 'N' para não.");
        }
    }
}
