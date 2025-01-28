package br.com.curriculo.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Years;
import org.primefaces.model.DualListModel;
import org.springframework.beans.BeanUtils;

import br.com.curriculo.arq.commons.utils.DateUtils;
import br.com.curriculo.arq.exception.ServiceBusinessException;
import br.com.curriculo.arq.persistence.Persistent;
import br.com.curriculo.arq.service.CrudServiceImpl;

public class Utils {

	private Utils() {
		// constructor not implement
	}

	public static String normalizarAcentos(String str) {
		/** Troca os caracteres acentuados por não acentuados **/
		String[][] caracteresAcento = { { "Á", "A" }, { "á", "a" }, { "É", "E" }, { "é", "e" }, { "Í", "I" },
				{ "í", "i" }, { "Ó", "O" }, { "ó", "o" }, { "Ú", "U" }, { "ú", "u" }, { "À", "A" }, { "à", "a" },
				{ "È", "E" }, { "è", "e" }, { "Ì", "I" }, { "ì", "i" }, { "Ò", "O" }, { "ò", "o" }, { "Ù", "U" },
				{ "ù", "u" }, { "Â", "A" }, { "â", "a" }, { "Ê", "E" }, { "ê", "e" }, { "Î", "I" }, { "î", "i" },
				{ "Ô", "O" }, { "ô", "o" }, { "Û", "U" }, { "û", "u" }, { "Ä", "A" }, { "ä", "a" }, { "Ë", "E" },
				{ "ë", "e" }, { "Ï", "I" }, { "ï", "i" }, { "Ö", "O" }, { "ö", "o" }, { "Ü", "U" }, { "ü", "u" },
				{ "Ã", "A" }, { "ã", "a" }, { "Õ", "O" }, { "õ", "o" }, { "Ç", "C" }, { "ç", "c" }, };

		for (int i = 0; i < caracteresAcento.length; i++) {
			str = str.replaceAll(caracteresAcento[i][0], caracteresAcento[i][1]);
		}

		/** Troca os espaços no início por "" **/
		str = str.replaceAll("^\\s+", "");
		/** Troca os espaços no início por "" **/
		str = str.replaceAll("\\s+$", "");
		/** Troca os espaços duplicados, tabulações e etc por " " **/
		str = str.replaceAll("\\s+", " ");

		return str;
	}

	public enum UtilsTime {
		DAYS, MONTHS, YEARS
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void validateUniqueness(Persistent p, CrudServiceImpl service, String... campos)
			throws ServiceBusinessException {
		PropertyDescriptor[] propriedadesObjeto = BeanUtils.getPropertyDescriptors(p.getClass());
		Persistent objetoPesquisado = BeanUtils.instantiate(p.getClass());
		List<String> inclusos = new ArrayList<>();
		for (String c : campos) {
			inclusos.add(c);
		}
		List<String> excluidos = new ArrayList<>();
		for (PropertyDescriptor pp : propriedadesObjeto) {
			if (!inclusos.contains(pp.getName())) {
				excluidos.add(pp.getName());
			}
		}
		BeanUtils.copyProperties(p, objetoPesquisado, excluidos.toArray(new String[excluidos.size()]));
		List<Persistent> lista = service.findByAttributes(objetoPesquisado);
		try {
			if (null != lista && !lista.isEmpty()) {
				for (Persistent persistent : lista) {
					Map<String, Object> caracteres = new HashMap<>();
					PropertyDescriptor[] po = BeanUtils.getPropertyDescriptors(persistent.getClass());
					for (PropertyDescriptor pp : po) {
						if (inclusos.contains(pp.getName())) {
							Object property = PropertyUtils.getProperty(persistent, pp.getName());
							caracteres.put(pp.getName(), property);
						}
					}
					if (pesquisarIgualdade(caracteres, propriedadesObjeto, persistent.getId(), p.getId(), p)) {
						throw new ServiceBusinessException(
								"A operação não pode ser realizada por violar a integridade dos dados");
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceBusinessException("A operação não pode ser realizada por violar a integridade dos dados");
		}
	}

	private static boolean pesquisarIgualdade(Map<String, Object> caracteres, PropertyDescriptor[] propriedadesObjeto,
			Integer pesquisado, Integer entidadePesquisada, Persistent persistent)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<Boolean> resultado = new ArrayList<>();
		for (int i = 0; i < propriedadesObjeto.length; i++) {
			PropertyDescriptor pd = propriedadesObjeto[i];
			if (caracteres.containsKey(pd.getName())) {
				Object property = PropertyUtils.getProperty(persistent, pd.getName());
				if (property.equals(caracteres.get(pd.getName()))) {
					resultado.add(true);
				} else {
					resultado.add(false);
				}
			}
		}
		if (resultado.contains(false)) {
			return false;
		} else {
			if (pesquisado.equals(entidadePesquisada)) {
				return false;
			}
			return true;
		}
	}

	public static int compareTimeOfDate(Date date1, Date date2) {
		Calendar calendar1 = new GregorianCalendar();
		Calendar calendar2 = new GregorianCalendar();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		if (calendar2.get(Calendar.HOUR_OF_DAY) > calendar1.get(Calendar.HOUR_OF_DAY)) {
			return 1;
		} else if ((compare(calendar1.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.HOUR_OF_DAY)))
				&& calendar2.get(Calendar.MINUTE) > calendar1.get(Calendar.MINUTE)) {
			return 1;
		} else if ((compare(calendar1.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.HOUR_OF_DAY))
				&& compare(calendar1.get(Calendar.MINUTE), calendar2.get(Calendar.MINUTE)))
				&& calendar2.get(Calendar.SECOND) > calendar1.get(Calendar.SECOND)) {
			return 1;
		} else if (calendar2.get(Calendar.HOUR_OF_DAY) == calendar1.get(Calendar.HOUR_OF_DAY)
				&& calendar2.get(Calendar.MINUTE) == calendar1.get(Calendar.MINUTE)
				&& calendar2.get(Calendar.SECOND) == calendar1.get(Calendar.SECOND)) {
			return 0;
		} else {
			return -1;
		}
	}

	private static boolean compare(int number1, int number2) {
		return number2 > number1 || number2 == number1;
	}

	public static boolean compareDates(Date var1, Date var2) {
		boolean resultado = false;
		Calendar c1 = new GregorianCalendar();
		Calendar c2 = new GregorianCalendar();
		c1.setTime(var1);
		c2.setTime(var2);
		if (c1.get(Calendar.YEAR) <= c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) <= c2.get(Calendar.MONTH)
				&& c1.get(Calendar.DAY_OF_MONTH) <= c2.get(Calendar.DAY_OF_MONTH)) {
			resultado = true;
		}
		return resultado;
	}

	public static void trimValues(Persistent entity, String... strings) throws ServiceBusinessException {
		try {
			for (String string : strings) {
				Object ob = PropertyUtils.getProperty(entity, string);
				if ((ob instanceof String) && !((String) ob).isEmpty()) {
					String stOb = (String) ob;
					PropertyUtils.setProperty(entity, string, stOb.trim());
				}
			}
		} catch (Exception e) {
			throw new ServiceBusinessException("Não foi possível realizar esta operação");
		}
	}

	public static int dateDiff(Date dataInicial, Date dataFinal) {
		return dateDiff(dataInicial, dataFinal, UtilsTime.DAYS) + 1;
	}

	public static int dateDiffLocalDate(LocalDate dataInicial, LocalDate dataFinal) {
		return Days
				.daysBetween(new DateTime(dataInicial.toDate().getTime()), new DateTime(dataFinal.toDate().getTime()))
				.getDays();
	}

	public static int dateDiff(Date dataInicial, Date dataFinal, UtilsTime utilsTime) {
		switch (utilsTime) {
		case DAYS:
			return (Days.daysBetween(new DateTime(dataInicial.getTime()), new DateTime(dataFinal.getTime())).getDays());

		case MONTHS:
			return (Months.monthsBetween(new DateTime(dataInicial.getTime()), new DateTime(dataFinal.getTime()))
					.getMonths());

		case YEARS:
			return Years.yearsBetween(new DateTime(dataInicial.getTime()), new DateTime(dataFinal.getTime()))
					.getYears();

		default:
			return (Days.daysBetween(new DateTime(dataInicial.getTime()), new DateTime(dataFinal.getTime())).getDays());
		}
	}

	public static int conversaoDiasParaPeriodo(int dias, UtilsTime utilsTime) {
		switch (utilsTime) {
		case DAYS:
			return (dias - (conversaoDiasParaPeriodo(dias, UtilsTime.MONTHS) * 30)
					- (conversaoDiasParaPeriodo(dias, UtilsTime.YEARS) * 365));
		case MONTHS:
			return (dias - (conversaoDiasParaPeriodo(dias, UtilsTime.YEARS) * 365)) / 30;
		case YEARS:
			return (dias / 365);
		}
		return 0;
	}

	public static List<Date> retornaDatasPorPeriodo(Date periodoInicial, Date periodoFinal) {
		List<Date> datasPeriodo = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(periodoInicial);
		for (Date dataAtual = periodoInicial; dataAtual.compareTo(periodoFinal) <= 0;) {
			datasPeriodo.add(dataAtual);
			calendar.add(Calendar.DATE, +1);
			dataAtual = calendar.getTime();
		}
		return datasPeriodo;
	}

	public static String recuperarDiaSemana(Date dataSemana) {
		String nomeDiaSemana = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataSemana);
		int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		switch (diaSemana) {
		case 1:
			nomeDiaSemana = DiasSemana.DOMINGO.toString();
			break;
		case 2:
			nomeDiaSemana = DiasSemana.SEGUNDA.toString();
			break;
		case 3:
			nomeDiaSemana = DiasSemana.TERCA.toString();
			break;
		case 4:
			nomeDiaSemana = DiasSemana.QUARTA.toString();
			break;
		case 5:
			nomeDiaSemana = DiasSemana.QUINTA.toString();
			break;
		case 6:
			nomeDiaSemana = DiasSemana.SEXTA.toString();
			break;
		case 7:
			nomeDiaSemana = DiasSemana.SABADO.toString();
			break;
		default:
			throw new IllegalStateException();
		}
		return nomeDiaSemana;
	}

	public static String getMensagem(String codigo) {
		ResourceBundle resource = ResourceBundle.getBundle("mensagens");
		return resource.getString(codigo);
	}

	public static String getMensagem(String codigo, String... strings) {
		String texto = getMensagem(codigo);
		MessageFormat mf = new MessageFormat(texto);
		return mf.format(strings, new StringBuffer(), null).toString();
	}

	public static boolean verificaDataFinalImediatamenteConsecutivaDataInicial(Date dataInicial, Date dataFinal) {
		GregorianCalendar calendarInicial = new GregorianCalendar();
		calendarInicial.setTime(dataInicial);
		calendarInicial.add(Calendar.DAY_OF_MONTH, 1);

		GregorianCalendar calendarFinal = new GregorianCalendar();
		calendarFinal.setTime(dataFinal);

		calendarInicial.set(Calendar.HOUR, 0);
		calendarInicial.set(Calendar.MINUTE, 0);
		calendarInicial.set(Calendar.SECOND, 0);
		calendarInicial.set(Calendar.MILLISECOND, 0);

		calendarFinal.set(Calendar.HOUR, 0);
		calendarFinal.set(Calendar.MINUTE, 0);
		calendarFinal.set(Calendar.SECOND, 0);
		calendarFinal.set(Calendar.MILLISECOND, 0);

		return calendarInicial.getTime().equals(calendarFinal.getTime());
	}

	public static int quantidadeAnosEntreDatas(Date dataInicial, Date dataFinal) {
		return dateDiff(dataInicial, dataFinal, UtilsTime.YEARS);
	}

	public static boolean compareDatesEquals(Date dataInicial, Date dataFinal) {
		boolean resultado = false;
		Calendar calInicial = new GregorianCalendar();
		Calendar calFinal = new GregorianCalendar();
		calInicial.setTime(dataInicial);
		calFinal.setTime(dataFinal);
		if (calInicial.get(Calendar.YEAR) == calFinal.get(Calendar.YEAR)
				&& calInicial.get(Calendar.MONTH) == calFinal.get(Calendar.MONTH)
				&& calInicial.get(Calendar.DAY_OF_MONTH) == calFinal.get(Calendar.DAY_OF_MONTH)) {
			resultado = true;
		}
		return resultado;
	}

	public static String casaDecimalEmInteiro(String resultado) {
		if (null != resultado && !resultado.isEmpty() && resultado.length() > 3) {
			switch (resultado.length()) {
			case 4:
				resultado = resultado.substring(0, 1) + "." + resultado.substring(1, resultado.length());
				break;
			case 5:
				resultado = resultado.substring(0, 2) + "." + resultado.substring(2, resultado.length());
				break;
			case 6:
				resultado = resultado.substring(0, 3) + "." + resultado.substring(3, resultado.length());
				break;
			case 7:
				resultado = resultado.substring(0, 1) + "." + resultado.substring(1, 4) + "."
						+ resultado.substring(4, resultado.length());
				break;
			case 8:
				resultado = resultado.substring(0, 2) + "." + resultado.substring(2, 5) + "."
						+ resultado.substring(5, resultado.length());
				break;
			case 9:
				resultado = resultado.substring(0, 3) + "." + resultado.substring(3, 6) + "."
						+ resultado.substring(6, resultado.length());
				break;
			default:
				throw new IllegalStateException();
			}
		}
		return resultado;
	}

	public static Date formatarDatasZerandoTime(Date data) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	public static boolean compararDatasEntreDuasDatas(Date dataComparada, Date dataInicial, Date dataFinal) {
		boolean resultado = false;
		if (null != dataComparada && null != dataInicial && null != dataFinal) {

			Date dataComparadaFormatada = formatarDatasZerandoTime(dataComparada);
			Date dataInicialFormatada = formatarDatasZerandoTime(dataInicial);
			Date dataFinalFormatada = formatarDatasZerandoTime(dataFinal);

			if ((dataComparadaFormatada.after(dataInicialFormatada)
					&& dataComparadaFormatada.before(dataFinalFormatada))
					|| dataComparadaFormatada.equals(dataInicialFormatada)
					|| dataComparadaFormatada.equals(dataFinalFormatada)) {
				resultado = true;
			}
		}
		return resultado;
	}

	public static Date ajustarData(Date data, Integer dia, Integer mes, Integer ano, Integer hora, Integer minuto,
			Integer segundo) {
		return Utils.ajustarData(data, dia, mes, ano, hora, minuto, segundo, null);
	}

	public static Date ajustarData(Date data, Integer dia, Integer mes, Integer ano, Integer hora, Integer minuto,
			Integer segundo, Integer milesimo) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		if (dia != null)
			calendar.set(Calendar.DAY_OF_MONTH, dia);
		if (mes != null)
			calendar.set(Calendar.MONTH, mes);
		if (ano != null)
			calendar.set(Calendar.YEAR, ano);
		if (hora != null)
			calendar.set(Calendar.HOUR, hora);
		if (minuto != null)
			calendar.set(Calendar.MINUTE, minuto);
		if (segundo != null)
			calendar.set(Calendar.SECOND, segundo);
		if (null != milesimo)
			calendar.set(Calendar.MILLISECOND, milesimo);
		return calendar.getTime();
	}

	public static Date incrementarData(Date data, int campo, Integer qtdTempo) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.add(campo, qtdTempo);
		return calendar.getTime();
	}

	public static Date incrementarData(Date data, Integer dia, Integer mes, Integer ano, Integer hora, Integer minuto,
			Integer segundo) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		if (dia != null)
			calendar.add(Calendar.DAY_OF_MONTH, dia);
		if (mes != null)
			calendar.add(Calendar.MONTH, mes);
		if (ano != null)
			calendar.add(Calendar.YEAR, ano);
		if (hora != null)
			calendar.add(Calendar.HOUR, hora);
		if (minuto != null)
			calendar.add(Calendar.MINUTE, minuto);
		if (segundo != null)
			calendar.add(Calendar.SECOND, segundo);
		return calendar.getTime();
	}

	public static Integer conversaoDateDiffParaPeriodo(Date dataInicio, Date dataFim, UtilsTime utilsTime) {
		int dias = dateDiff(dataInicio, dataFim);
		return conversaoDiasParaPeriodo(dias, utilsTime);
	}

	public static Date formatarDataFaltandoUmSegundoParaFimDoDia(Date data) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 59);
		return calendar.getTime();
	}

	public static Date formatarDatasZerandoTime24Horas(Date data) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static boolean isDateValid(Date yourDate) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTime(yourDate);
		try {
			cal.getTime();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String converteDiasEmAnosMesesDias(Integer dias) {

		int anos = 0;
		int meses = 0;
		int diasRestantes = 0;

		if (dias > 0) {
			anos = dias / 365;
			int restoAnos = dias % 365;

			if (restoAnos > 0) {
				meses = restoAnos / 30;

				if (meses >= 12) {
					anos += 1;
					meses = 0;
				}

				int restoMeses = restoAnos % 30;
				if (restoMeses > 0) {
					diasRestantes = restoMeses;
				}

			}
			return String.format("%1d ano(s), %2d mês(es) e %3d dia(s)", anos, meses, diasRestantes);
		} else if (dias == 0) {
			return String.format("%1d ano(s), %2d mês(es) e %3d dia(s)", anos, meses, diasRestantes);
		} else {
			return String.format("Ocorreu um problema no calculo de tempo (%d dia(s))", dias);
		}
	}

	public static String getConfig(String key) {
		Logger logger = Logger.getLogger(Utils.class.getName());
		ResourceBundle resource = ResourceBundle.getBundle("config");
		String envValue = null;
		try {
			String envVar = resource.getString(key);
			envValue = envVar.matches("\\$\\{[A-Za-z0-9_]+\\}") ? getSystemEnv(envVar.substring(2, envVar.length() - 1))
					: envVar;

			if (envValue == null) {
				logger.warning("Variável de ambiente inexistente: " + envVar);
				envValue = envVar; // Mantém o valor original se a variável de ambiente não existir
			}
		} catch (MissingResourceException e) {
			logger.warning(e.getMessage());
			logger.warning("Propriedade inexistente: " + key);
		}
		return envValue;
	}

	public static String getSystemEnv(String property) {
		return System.getenv(property);
	}

	public static int round(double value, int precision) {
		return (int) roundDouble(value, precision);
	}

	public static double roundDouble(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	public static String normalizar(String str) {

		/** Troca os caracteres acentuados por não acentuados **/
		String[][] caracteresAcento = { { "Á", "A" }, { "á", "a" }, { "É", "E" }, { "é", "e" }, { "Í", "I" },
				{ "í", "i" }, { "Ó", "O" }, { "ó", "o" }, { "Ú", "U" }, { "ú", "u" }, { "À", "A" }, { "à", "a" },
				{ "È", "E" }, { "è", "e" }, { "Ì", "I" }, { "ì", "i" }, { "Ò", "O" }, { "ò", "o" }, { "Ù", "U" },
				{ "ù", "u" }, { "Â", "A" }, { "â", "a" }, { "Ê", "E" }, { "ê", "e" }, { "Î", "I" }, { "î", "i" },
				{ "Ô", "O" }, { "ô", "o" }, { "Û", "U" }, { "û", "u" }, { "Ä", "A" }, { "ä", "a" }, { "Ë", "E" },
				{ "ë", "e" }, { "Ï", "I" }, { "ï", "i" }, { "Ö", "O" }, { "ö", "o" }, { "Ü", "U" }, { "ü", "u" },
				{ "Ã", "A" }, { "ã", "a" }, { "Õ", "O" }, { "õ", "o" }, { "Ç", "C" }, { "ç", "c" }, };

		for (int i = 0; i < caracteresAcento.length; i++) {
			str = str.replaceAll(caracteresAcento[i][0], caracteresAcento[i][1]);
		}

		/** Troca os caracteres especiais da string por "" **/
		String[] caracteresEspeciais = { "\\.", ",", "-", ":", "\\(", "\\)", "ª", "\\|", "\\\\", "°", "´", "`", "\\+",
				"\\=", "\\_", "\\{", "\\}", "\\[", "\\]", "\\?", "\\;", "\\^", "\\<", "\\>", "\\/", "~" };

		for (int i = 0; i < caracteresEspeciais.length; i++) {
			str = str.replaceAll(caracteresEspeciais[i], "");
		}

		/** Troca os espaços no início por "" **/
		str = str.replaceAll("^\\s+", "");
		/** Troca os espaços no início por "" **/
		str = str.replaceAll("\\s+$", "");
		/** Troca os espaços duplicados, tabulações e etc por " " **/
		str = str.replaceAll("\\s+", " ");

		return str;
	}

	public static Date getDateInicialMonth(int mes, int ano) {
		Calendar c = new GregorianCalendar(ano, mes - 1, 1);
		return c.getTime();
	}

	public static Date getLastDateMonth(int mes, int ano) {
		Calendar c = new GregorianCalendar(ano, mes - 1, 1);
		c.set(Calendar.DAY_OF_MONTH, c.getMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	public static Date getLastDayOfYear(int ano) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, ano);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date getFirstDayOfYear(int ano) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, ano);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static boolean verifyEndDateGreaterThanInitialDate(Date initialDate, Date endDate) {
		return endDate.compareTo(initialDate) < 0;
	}

	public static Date getDataNoAnoAtual(Date data) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.set(Calendar.YEAR, DateUtils.getCurrentYear());
		return calendar.getTime();
	}

	public static boolean isSameDay(Date dateInicial, Date dateFinal) {
		dateInicial = Utils.formatarDatasZerandoTime(dateInicial);
		dateFinal = Utils.formatarDatasZerandoTime(dateFinal);
		return dateInicial.equals(dateFinal);
	}

	public static boolean possuiDiferencaDeMaisDeUmDia(Date dateInicial, Date dateFinal) {
		long diferencaEmMilisegundos = Math.abs(dateInicial.getTime() - dateFinal.getTime());
		long umDiaEmMilisegundo = 24L * 60 * 60 * 1000;
		return diferencaEmMilisegundos >= umDiaEmMilisegundo;
	}

	public static boolean verificaInterseccaoPeriodos(Date dataInicialPrimeiroPeriodo, Date dataFinalPrimeiroPeriodo,
			Date dataInicialSegundoPeriodo, Date dataFinalSegundoPeriodo) {
		Calendar period1StartDate = Calendar.getInstance();
		period1StartDate.setTime(dataInicialPrimeiroPeriodo);
		Calendar period1EndDate = Calendar.getInstance();
		period1EndDate.setTime(dataFinalPrimeiroPeriodo);

		Calendar period2StartDate = Calendar.getInstance();
		period2StartDate.setTime(dataInicialSegundoPeriodo);
		Calendar period2EndDate = Calendar.getInstance();
		period2EndDate.setTime(dataFinalSegundoPeriodo);

		return (period1EndDate.after(period2StartDate) || period1EndDate.compareTo(period2StartDate) == 0);
	}

	public static boolean isDiferencaDeDatasMenorOuIgualUmAno(Date dataInicial, Date dataFinal) {
		if (dataInicial == null || dataFinal == null) {
			return false;
		}
		Calendar calendarInicial = Calendar.getInstance();
		Calendar calendarFinal = Calendar.getInstance();
		calendarInicial.setTime(dataInicial);
		calendarFinal.setTime(dataFinal);

		int diferencaEntreAnos = calendarFinal.get(Calendar.YEAR) - calendarInicial.get(Calendar.YEAR);

		if (diferencaEntreAnos > 1) {
			return false;
		} else if (diferencaEntreAnos == 1) {
			return calendarFinal.get(Calendar.DAY_OF_YEAR) <= calendarInicial.get(Calendar.DAY_OF_YEAR);
		}
		return true;
	}

	public static <T> DualListModel<T> criarDualListModelRemovendoValores(List<T> values, List<T> targetValues) {
		List<T> sourceList = new ArrayList<>(values);
		List<T> targetList = new ArrayList<>(targetValues);
		sourceList.removeAll(targetValues);
		return new DualListModel<>(sourceList, targetList);
	}
}