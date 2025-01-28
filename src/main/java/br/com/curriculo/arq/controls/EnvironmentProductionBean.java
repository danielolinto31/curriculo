package br.com.curriculo.arq.controls;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class EnvironmentProductionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msgAlertaAmbienteAplicacao;

	public void verificarAmbienteNoQualAplicacaoEstaRodando() {
		String appLocalHostName = "";
		InetAddress ip;

		try {
			ip = InetAddress.getLocalHost();
			appLocalHostName = ip.getHostName().trim().toLowerCase();
		} catch (UnknownHostException e) {
			setMsgAlertaAmbienteAplicacao(
					"Não foi possível identificar o servidor no qual está rodando a aplicação. Detalhes: "
							+ e.getMessage());
			return;
		}

		ResourceBundle config = ResourceBundle.getBundle("config");
		String dbLocalHostName = config.getString("db.host.name").trim().toLowerCase();

		ResourceBundle envProd = ResourceBundle.getBundle("env-prod");
		String prodAppHostName = envProd.getString("env.prod.app.host.name").trim().toLowerCase();
		String prodDbHostName = envProd.getString("env.prod.db.host.name").trim().toLowerCase();

		if (appLocalHostName.equals(prodAppHostName)) {
			if (!dbLocalHostName.equals(prodDbHostName)) {
				setMsgAlertaAmbienteAplicacao(String.format(
						"ATENÇÃO: O servidor de banco de dados utilizado (%s) não é o correto para o ambiente de produção (%s).",
						dbLocalHostName, appLocalHostName));
			}
		} else {
			if (dbLocalHostName.equals(prodDbHostName)) {
				setMsgAlertaAmbienteAplicacao(String.format(
						"ATENÇÃO: O servidor de banco de dados utilizado (%s) não é o correto para o ambiente de desenvolvimento (%s).",
						dbLocalHostName, appLocalHostName));
			}
		}
	}

	public String getMsgAlertaAmbienteAplicacao() {
		return msgAlertaAmbienteAplicacao;
	}

	public void setMsgAlertaAmbienteAplicacao(String msgAlertaAmbienteAplicacao) {
		this.msgAlertaAmbienteAplicacao = msgAlertaAmbienteAplicacao;
	}

}
