package br.com.curriculo.arq.commons.utils;

public enum DateAddEnum {

	MASK_SQLSERVER("yyyyMMdd"), MASK_DDMMYY("dd/MM/yy"), MASK_DDMMYYYY("dd/MM/yyyy"),

	DATEADD_WEEK("W"), DATEADD_DAY("D"), DATEADD_MONTH("M"), DATEADD_YEAR("Y");

	private String value;

	private DateAddEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
