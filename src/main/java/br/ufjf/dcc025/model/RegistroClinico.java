package br.ufjf.dcc025.model;

import java.time.LocalDateTime;

public interface RegistroClinico {
    LocalDateTime getDataRegistro();
    String getTipoRegistroClinico();
    String getDescricao();
}
