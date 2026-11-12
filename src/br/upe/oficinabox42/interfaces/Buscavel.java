package br.upe.oficinabox42.interfaces;

import br.upe.oficinabox42.exceptions.ValorInvalidoException;

public interface Buscavel {
    void buscarPorInicioDoNome(String inicio) throws ValorInvalidoException;
}

