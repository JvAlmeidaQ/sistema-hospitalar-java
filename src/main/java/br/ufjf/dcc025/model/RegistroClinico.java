//Gustavo Bersan Moreira Campos 202435019
//João Vitor Almeida Queiroz 202435007

package br.ufjf.dcc025.model;

import java.time.LocalDateTime;

public interface RegistroClinico {
    LocalDateTime getDataRegistro();
    String getTipoRegistroClinico();
    String getDescricao();
}
