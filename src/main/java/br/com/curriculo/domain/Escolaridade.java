package br.com.curriculo.domain;

import lombok.Getter;

@Getter
public enum Escolaridade {
	ENSINO_FUNDAMENTAL("Ensino Fundamental"),
	ENSINO_MEDIO("Ensino Médio"),
	GRADUACAO_INCOMPLETO("Graduação Incompleto"),
	GRADUACAO_COMPLETO("Graduação Completo"),
	POS_GRADUACAO_INCOMPLETO("Pós-graduação Incompleto"),
	POS_GRADUACAO_COMPLETO("Pós-graduação Completo"),
	MESTRADO_INCOMPLETO("Mestrado Incompleto"),
	MESTRADO_COMPLETO("Mestrado Completo"),
	DOUTORADO_INCOMPLETO("Doutorado Incompleto"),
	DOUTORADO_COMPLETO("Doutorado Completo");

    private final String descricao;

    Escolaridade(String descricao) {
        this.descricao = descricao;
    }

}
