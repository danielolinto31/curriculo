package br.com.curriculo.arq.exception;

public class Mensagem {

	public static final int ERRO=0;
	public static final int ERRO_FATAL=1;
	public static final int INFO=2;
	public static final int WARNING=3;
	
	private int severidade;
	
	private String mensagem;
	
	public Mensagem(int severidade, String mensagem){
		this.severidade = severidade;
		this.mensagem = mensagem;
	}

	public int getSeveridade() {
		return severidade;
	}

	public void setSeveridade(int severidade) {
		this.severidade = severidade;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}
