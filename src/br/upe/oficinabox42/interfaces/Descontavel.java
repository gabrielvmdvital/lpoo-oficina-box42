package br.upe.oficinabox42.interfaces;

import br.upe.oficinabox42.exceptions.OrdemFinalizadaException;
import br.upe.oficinabox42.exceptions.ValorInvalidoException;

public interface Descontavel {
    double aplicarDesconto(double percentual) throws ValorInvalidoException, OrdemFinalizadaException;
}

