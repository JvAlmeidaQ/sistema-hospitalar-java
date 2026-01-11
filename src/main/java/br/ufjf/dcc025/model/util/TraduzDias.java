package br.ufjf.dcc025.model.util;

import br.ufjf.dcc025.model.DiasDaSemana;

import java.time.DayOfWeek;

public class TraduzDias {

    public static DiasDaSemana traduzDias(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiasDaSemana.SEGUNDA;
            case TUESDAY -> DiasDaSemana.TERCA;
            case WEDNESDAY -> DiasDaSemana.QUARTA;
            case THURSDAY -> DiasDaSemana.QUINTA;
            case FRIDAY -> DiasDaSemana.SEXTA;
            default -> null;
        };
    }
}
