package com.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GenericUtil {
	public static SimpleDateFormat dateformat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final String MUSIC_FILE = new GenericUtil().getClass()
			.getResource("/").getPath()
			+ "\\alarm.wav";
	@SuppressWarnings("serial")
	public static List<String> creditlist = new ArrayList<String>() {

		{
			add("ico_xx_1");
			add("ico_xx_2");
			add("ico_xx_3");
			add("ico_xx_4");
			add("ico_xx_5");
		}
	};

	public static String cookieFormat(HashMap<String, String> cookieMap) {
		if (cookieMap.size() == 0)
			return "";
		StringBuffer buff = new StringBuffer(2000);
		for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
			String key = entry.getKey().trim();
			LogUtil.debugPrintf("begin==" + key);
			if (key.equalsIgnoreCase("domain")
					|| key.equalsIgnoreCase("HttpOnly")
					|| key.equalsIgnoreCase("path")) {
				LogUtil.debugPrintf("ignore==" + key);
				continue;
			}
			buff.append(entry.toString() + ";");
		}
		buff.deleteCharAt(buff.lastIndexOf(";"));
		return buff.toString();
	}

	public static void playAlarm() throws LineUnavailableException,
			UnsupportedAudioFileException, IOException {

		// 获取音频输入流a
		AudioInputStream audioInputStream = AudioSystem
				.getAudioInputStream(new File(MUSIC_FILE));
		// 获取音频编码对象
		AudioFormat audioFormat = audioInputStream.getFormat();

		// 设置数据输入
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
				audioFormat, AudioSystem.NOT_SPECIFIED);
		SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem
				.getLine(dataLineInfo);
		sourceDataLine.open(audioFormat);
		sourceDataLine.start();

		/*
		 * 从输入流中读取数据发送到混音器
		 */
		int count;
		byte tempBuffer[] = new byte[1024];
		while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
			if (count > 0) {
				sourceDataLine.write(tempBuffer, 0, count);
			}
		}

		// 清空数据缓冲,并关闭输入
		sourceDataLine.drain();
		sourceDataLine.close();
	}
}
