package br.com.curriculo.arq.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Classe utilitária para busca de resources.
 *
 */
public class ResourceUtils {

    public static URL locateAsURL(String resourcePath) {
        URL result = null;

        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            result = null;
        } else {
            result = ResourceUtils.class.getResource(resourcePath);

            if (result == null) {
                result = ResourceUtils.class.getClassLoader().getResource(
                        resourcePath);
            }
            if (result == null) {
                result = Thread.currentThread().getContextClassLoader()
                        .getResource(resourcePath);
            }
            
            /*
             * Se o usuário informou um caminho relativo (sem a "/" no começo)
             * mas na verdade queria informar um caminho absoluto, ainda é feita
             * uma busca adicionando a "/" na tentativa de localizar o
             * resource num caminho absoluto.
             */
            if (result == null && !resourcePath.startsWith("\\/")) {
                result = locateAsURL("/" + resourcePath);
            }
        }

        return result;
    }

    public static InputStream locateAsInputStream(String resourcePath) {
        InputStream result = null;
        try {
            URL url = locateAsURL(resourcePath);

            if (url != null) {
                result = url.openStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] locateAsByteArray(String resourcePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = locateAsInputStream(resourcePath);

        int read;
        byte[] buff = new byte[1024];
        try {
            while (null != is && (read = is.read(buff)) != -1) {
                baos.write(buff, 0, read);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

}
