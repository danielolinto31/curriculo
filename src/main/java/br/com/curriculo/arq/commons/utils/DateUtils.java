package br.com.curriculo.arq.commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	public static final String MASK_SQLSERVER = "yyyyMMdd";
	public static final String MASK_DDMMYY = "dd/MM/yy";
	public static final String MASK_DDMMYYYY = "dd/MM/yyyy";

	public static int getCurrentYear() {
		return getYear(getCurrentDate());
	}

	public static int getCurrentMonth() {
		return getMonth(getCurrentDate());
	}

	public static int getCurrentDay() {
		return getDay(getCurrentDate());
	}

	public static int getCurrentTrimester() {
		return getTrimester(getCurrentDate());
	}

	public static int getYear(Date data) {
		DateFormat df = new SimpleDateFormat("yyyy");
		df.setLenient(false);
		return Integer.valueOf(df.format(data)).intValue();
	}

	public static int getMonth(Date data) {
		DateFormat df = new SimpleDateFormat("MM");
		df.setLenient(false);
		return Integer.valueOf(df.format(data)).intValue();
	}

	public static int getDay(Date data) {
		DateFormat df = new SimpleDateFormat("dd");
		df.setLenient(false);
		return Integer.valueOf(df.format(data)).intValue();
	}

	public static int getTrimester(Date data) {
		DateFormat df = new SimpleDateFormat("MM");
		df.setLenient(false);
		Integer valor = Integer.valueOf(df.format(data)).intValue();

		if ((valor >= 1) && (valor <= 3)) {
			return 1;
		} else if ((valor >= 4) && (valor <= 6)) {
			return 2;
		} else if ((valor >= 7) && (valor <= 9)) {
			return 3;
		} else {
			return 4;
		}
	}

	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	public static Date truncate(Date dateToTrunk) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateToTrunk);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getDateStartOfTheDay(Date dateDayStart) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateDayStart);
		cal.set(Calendar.HOUR, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
	}

	public static Date getDateEndOfTheDay(Date dateDayEnd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateDayEnd);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static String formatCurrentDate() {
		return formatDate(getCurrentDate(), null);
	}

	public static String formatCurrentDate(String mask) {
		return formatDate(getCurrentDate(), mask);
	}

	public static String formatDate(Date date) {
		return formatDate(date, null);
	}

	public static String formatDate(Date date, String mask) {
		if (date == null) {
			return null;
		}

		if (StringUtils.checkNull(mask).equals("")) {
			mask = MASK_DDMMYYYY;
		}
		try {
			DateFormat df = new SimpleDateFormat(mask);
			return df.format(date);
		} catch (Exception exc) {
			return "#ERRO#";
		}
	}

	public static String formatDate(String date) {
		if (date == null) {
			return null;
		}
		if (!isValidDate(date)) {
			return null;
		}
		return formatDate(stringToDate(date), null);
	}

	public static String formatDate(String date, String mask) {
		if (date == null) {
			return null;
		}
		if (StringUtils.checkNull(mask).equals("")) {
			mask = MASK_DDMMYYYY;
		}
		if (!isValidDate(date, mask)) {
			return null;
		}
		return formatDate(stringToDate(date, mask), mask);
	}

	public static boolean isValidDate(String date) {
		return isValidDate(date, null);
	}

	public static boolean isValidDate(String date, String mask) {
		return (date != null) && (stringToDate(date, mask) != null);
	}

	public static Date stringToDate(String data) {
		return stringToDate(data, null);
	}

	public static Date stringToDate(String data, String mask) {
		if (StringUtils.checkNull(data).equals("")) {
			return null;
		}

		if ((data.length() != 8) && (data.length() != 10)) {
			return null;
		}

		try {
			if (StringUtils.checkNull(mask).equals("")) {
				if (data.length() == 8) {
					mask = MASK_DDMMYY;
				} else {
					mask = MASK_DDMMYYYY;
				}
			}
			DateFormat df = new SimpleDateFormat(mask);
			df.setLenient(false);
			return df.parse(data);
		} catch (Exception exc) {
			return null;
		}
	}

	public static String formatDateSQLServer(String data) {
		if (!StringUtils.checkNull(data).equals("")) {
			Date dataFMT = null;
			if (data.length() == 8) {
				dataFMT = DateUtils.stringToDate(data, DateUtils.MASK_SQLSERVER);
			} else {
				try {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					dataFMT = df.parse(data.substring(0, 10));
				} catch (Exception exc) {
					return data;
				}
			}
			return DateUtils.formatDate(dataFMT);
		} else {
			return data;
		}
	}

	public static Date dateAdd(Date data, DateAddEnum tipo, int qtde) {

		Date tstHoje;
		tstHoje = data;
		GregorianCalendar gregCalendar = new GregorianCalendar();
		gregCalendar.setTime(tstHoje);

		if (tipo.equals(DateAddEnum.DATEADD_WEEK)) {
			gregCalendar.add(Calendar.WEEK_OF_MONTH, qtde);
		} else if (tipo.equals(DateAddEnum.DATEADD_DAY)) {
			gregCalendar.add(Calendar.DAY_OF_MONTH, qtde);
		} else if (tipo.equals(DateAddEnum.DATEADD_MONTH)) {
			gregCalendar.add(Calendar.MONTH, qtde);
		} else if (tipo.equals(DateAddEnum.DATEADD_YEAR)) {
			gregCalendar.add(Calendar.YEAR, qtde);
		}

		return gregCalendar.getTime();
	}

	public static Date concatHour(Date data, String hour) {
		String sData = DateUtils.formatDate(data, "yyyy-MM-dd " + hour);
		Date dataComHoraZerada = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			dataComHoraZerada = dateFormat.parse(sData);
			return dataComHoraZerada;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getHourFromDate(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinutesFromDate(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.MINUTE);
	}

	public static Date getNextUtilDate(Date data) {
		boolean continuar = true;
		while (continuar) {
			Date nextDate = DateUtils.dateAdd(data, DateAddEnum.DATEADD_DAY, 1);
			GregorianCalendar gregCal = new GregorianCalendar();
			gregCal.setTime(nextDate);
			int day = gregCal.get(Calendar.DAY_OF_WEEK);
			if ((day > 1) && (day < 7)) {
				return nextDate;
			}
			data = nextDate;
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static Date getFirstOrLastDate(int year, int month) {
		Date result = null;
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.AM_PM, 0);
			result = cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date getFirstDate(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1, 0, 0, 0);
		return cal.getTime();
	}

	public static Date getLastDate(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}

	public static void main(String[] args) {
		Date dataIni = DateUtils.getFirstDate(2010, 8);
		Date dataFim = DateUtils.getLastDate(2010, 8);
		System.out.println(dataIni + " = " + DateUtils.formatDate(dataIni, "dd/MM/yyyy HH:mm:ss"));
		System.out.println(dataFim + " = " + DateUtils.formatDate(dataFim, "dd/MM/yyyy HH:mm:ss"));
	}

}
