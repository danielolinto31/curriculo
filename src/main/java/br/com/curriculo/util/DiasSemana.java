package br.com.curriculo.util;

public enum DiasSemana {

    TODOS("TODOS"), DOMINGO("Domingo"), SEGUNDA("Segunda-feira"), TERCA("Terça-feira"), QUARTA("Quarta-feira"), QUINTA("Quinta-feira"), SEXTA("Sexta-feira"), SABADO(
            "Sábado");

    private String descricao;

    private DiasSemana(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return getDescricao();
    }

}
