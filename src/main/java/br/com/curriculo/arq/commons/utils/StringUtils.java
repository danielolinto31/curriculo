package br.com.curriculo.arq.commons.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.MaskFormatter;

public class StringUtils {
	
    
    /** Character   Description
     *  #           Qualquer numero valido.
     *
     *  '           Usado para não usar nenhum caracter especial na formatacao ("\n", "\t"....)
     *
     *  U           Qualquer caracter
     *  Todas as letras minusculas sao passadas para maiuscula.
     *
     *  L           Qualquer caracter
     * Todas as letras maiusculas sao passadas para minusculas
     *
     *  A          Qualquer caracter ou numero
     *  ( Character.isLetter or Character.isDigit )
     *
     *  ?           Qualquer caracter ( Character.isLetter ).
     *
     *  *           Qualquer Coisa.
     *
     *  H           Qualquer caracter hexa (0-9, a-f ou A-F).
     *
     * ====================================
     * ex:
     * value = "A1234B567Z"
     * mask = "A-AAAA-AAAA-A"
     * output : A-1234-B567-Z
     *
     * ====================================
     * 
     * @param string
     * @param mask
     * @return
     * @throws java.text.ParseException
     */
    public static String formatUsingMask(String string, String mask)
            throws java.text.ParseException {
        MaskFormatter mf = new MaskFormatter(mask);
        mf.setValueContainsLiteralCharacters(false);
        return mf.valueToString(string);
    }   
    
    
	 /**
     * retorna "" se a string for nula, vazia
     * ou possuir somente espaços. Senão, retorna
     * a propria string
     */
    public static String checkNull(String str) {
        //se for nula ou vazia
        return (str == null)||(str.trim().equals("")) ? "" : str;
    }
    /**
     * retorna "TRUE" se a string for nula, vazia
     * ou possuir somente espaços.
     */
    public static boolean isEmpty(String str) {
        //verifica a string informada
        String aux = checkNull(str);
        //retorna TRUE se ela for nula, vazia ou somente espaços
        return aux.equals("");
    }
    
    // TODO: Rever - Método criado por Eric no branch arq-0.7.0-Eric-AssinadorPje.
    /**
	 * Retorna TRUE se TODOS os objetos informados estão vazios. Utiliza a implementação de {@link ValidatorUtil#isEmpty(Object)}
	 * 
	 * @param objs
	 * @see ValidatorUtil#isEmpty(Object)
	 * @return
	 */
	public static final boolean isAllEmpty(String... objs) {
		
		if (objs == null)
			return true;
		
		for (String o : objs) {
			if (isEmpty(o) == false)
				return false;
		}
		
		return true;
	}
	
	// TODO: Rever - Método criado por Eric no branch arq-0.7.0-Eric-AssinadorPje.
	/**
	 * Retorna TRUE se todos os objetos estão populados. Utiliza a implementação de {@link ValidatorUtil#isEmpty(Object)}
	 *  
	 * @param objs
	 * @see ValidatorUtil#isEmpty(Object)
	 * @return
	 */
	public static final boolean isAllNotEmpty(String... objs) {
		
		if (objs == null)
			return false;
		
		for (String o : objs) {
			if (isEmpty(o) == true)
				return false;
		}
		
		return true;
	}

    /**
     * suubstitui os caracteres especiais da string informada
     */
    public static String retirarCaracteresEspeciais(String s){
    	String[] original = {"á", "Á"
    			            ,"é", "É"
    			            ,"í", "Í"
    			            ,"ó", "Ó"
    			            ,"ú", "Ú"
    			            ,"ã", "Ã"
    			            ,"õ", "Õ"
    			            ,"à", "À"
    			            ,"â", "Â"
    			            ,"ê", "Ê"
    			            ,"ô", "Ô"
    			            ,"ç", "Ç"
    			            ,"ª", "º"
    			            ,"ä", "Ä"
    			            ,"ë", "Ë"
    			            ,"ï", "Ï"
    			            ,"ö", "Ö"
    			            ,"ü", "Ü"
    			            };
    	String[] trocarPor = {
			                 "a", "A"
				            ,"e", "E"
				            ,"i", "I"
				            ,"o", "O"
				            ,"u", "U"
				            ,"a", "A"
				            ,"o", "O"
				            ,"a", "A"
    			            ,"a", "A"
    			            ,"e", "E"
    			            ,"o", "O"
				            ,"c", "C"
    			            ,"a", "o"
			                ,"a", "A"
				            ,"e", "E"
				            ,"i", "I"
				            ,"o", "O"
				            ,"u", "U"
        };

    	String retorno = new String(s);

    	for (int i = 0; i < trocarPor.length-1; i++) {
			retorno = retorno.replace(original[i], trocarPor[i]);
		}
    	return retorno;
    }

    /**
     * @param str
     * @return
     */
	public static String fristCharUpper(String str){
		String s = "";
		if (!isEmpty(str))  {
			s = str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		return s;
	}

	/**
	 * @param str
	 * @return
	 */
	public static String fristCharLower(String str){
		String s = "";
		if (!isEmpty(str))  {
			s = str.substring(0, 1).toLowerCase() + str.substring(1);
		}
		return s;
	}

	/**
	 * Método que retira acentos, tremas, crases , ç  e outros caracteres especiais
	 * de uma String. Método semelhante ao "retirarCaracteresEspeciais" acima.
	 * @param text
	 * @return
	 */
	public static String convertToASCII2(String text) {  
	     return text.replaceAll("[ãâàáä]", "a")  
		                   .replaceAll("[êèéë]", "e")  
		                   .replaceAll("[îìíï]", "i")  
		                   .replaceAll("[õôòóö]", "o")  
		                   .replaceAll("[ûúùü]", "u")  
		                   .replaceAll("[ÃÂÀÁÄ]", "A")  
		                   .replaceAll("[ÊÈÉË]", "E")  
		                   .replaceAll("[ÎÌÍÏ]", "I")  
		                   .replaceAll("[ÕÔÒÓÖ]", "O")  
		                   .replaceAll("[ÛÙÚÜ]", "U")  
		                   .replace('ç', 'c')  
		                   .replace('Ç', 'C')  
		                   .replace('ñ', 'n')  
		                   .replace('Ñ', 'N');  
	}
	
	/**
     * Converte uma String com itens separados por vírgula em uma lista de Strings.
     * Obs: Os espaços em branco entre as vírgulas serão desprezados, vide exemplo
     * abaixo.
     * 
     * Ex: "misael barreto, de, queiroz,     arquitetura"
     * Resultado: [misael barreto, de, queiroz, arquitetura ]
     * 
     * @param commaSeparatedString String com itens separados por vírgula
     * @return the list
     */
	public static List<String> convertCommaSeparatedStringToList(String commaSeparatedString) {
	    if (commaSeparatedString == null || commaSeparatedString.trim().length() == 0) {
	        return new ArrayList<String>();
	    } else {
	        return Arrays.asList(commaSeparatedString.split("\\s*,\\s*"));
	    }
	}
}
