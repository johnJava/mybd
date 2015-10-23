package cn.gyee.appsoft.jrt.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.gyee.appsoft.jrt.model.PointData;

public class EdnaApiHelper {

	public EdnaApiHelper() {
	}

	public static PointData buildPointData(String pointId, Double value, Integer utcTime) {
		return new PointData(pointId, value, utcTime, Integer.valueOf(0), Short.valueOf((short) 3));
	}

	public static PointData buildPointData(String pointId, Double value) {
		PointData pd = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Integer utcTime = Integer.valueOf(parseDateStringToUTC(format.format(new Date())));
			pd = new PointData(pointId, value, utcTime, Integer.valueOf(0), Short.valueOf((short) 3));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return pd;
	}

	public static int parseDateStringToUTC(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if ((date != null) && (!"".equals(date))) {
			return (int) (format.parse(date).getTime() / 1000L);
		}
		return (int) (format.parse(date).getTime() / 1000L);
	}
	public static String parseUTCLongToDate(long date) {
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date dt = new Date(date * 1000L);
	    return format.format(dt);
	  }

}
