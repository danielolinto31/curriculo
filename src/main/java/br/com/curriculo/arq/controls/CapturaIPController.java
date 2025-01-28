package br.com.curriculo.arq.controls;

import javax.servlet.http.HttpServletRequest;

public class CapturaIPController {

    public String capturaIp(HttpServletRequest request) {
        String ipCliente = request.getRemoteAddr(); // Captura o IP do cliente

        String ipReal = request.getHeader("X-Forwarded-For");
        if (ipReal != null && !ipReal.isEmpty()) {
            ipCliente = ipReal.split(",")[0]; // Pega o primeiro IP na lista
        }

        return ipCliente;
    }
}
