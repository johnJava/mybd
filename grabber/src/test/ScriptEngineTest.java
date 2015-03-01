package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.json.JSONObject;

import com.common.GenericUtil;
import com.common.LogUtil;

/** * Java调用并执行js文件，传递参数，并活动返回值 * * @author manjushri */

public class ScriptEngineTest {

	public static void main(String[] args) throws Exception {
		encodeuricomponent();//getPasswordKey();
	}
	public static void encodeuricomponent() throws ScriptException, NoSuchMethodException{
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		Invocable invoke = (Invocable) engine; // 调用merge方法，并传入两个参数
		String c = (String) invoke.invokeFunction("encodeuricomponent","OBUu8{line}wTWq8{plus}l6Is3khDXY9{line}xm48pH7KQtm4RMSvVSVaKA8eNie4UQ{equel}{equel}");
	}
	
	public static void toMd5() throws Exception{
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		String jsFileName = "md5.js"; // 读取js文件
		FileReader reader = new FileReader(jsFileName); // 执行指定脚本
		engine.eval(reader);
		String PasswordKey="umpvqt";//getPasswordKey();
		int counti=1;
		while(counti-->0){
			if (engine instanceof Invocable) {
				String pwd="";
				Invocable invoke = (Invocable) engine; // 调用merge方法，并传入两个参数
				String c = (String) invoke.invokeFunction("hex_md5","54054110");
				System.out.println("c = " + c);
				String toer=c.substring(8, 24) + PasswordKey;
				System.out.println("toer = " + toer);
				pwd=(String) invoke.invokeFunction("hex_md5",toer);
				System.out.println("pwd = " + pwd);
			}
		}
		reader.close();
	}
	public static String getPasswordKey() throws ParserException{
		String PasswordKey="";
		String url = "https://passport.5173.com";
		long startTime = System.currentTimeMillis();
		LogUtil.debugPrintf("请求[" + url + "]开始[" + GenericUtil.dateformat.format(startTime) + "]");
		URL urlPage;
		HttpURLConnection conn = null;
		try {
			urlPage = new URL(url);
			conn = (HttpURLConnection) urlPage.openConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e1) {
			e1.printStackTrace();
		}
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		conn.setRequestProperty("If-Modified-Since", "0");
		Parser parser = new Parser(conn);
		parser.setEncoding("UTF-8");
		Map<String, List<String>> head = conn.getHeaderFields();
		//System.out.println("head===" + head.toString());
		long endTime = System.currentTimeMillis();
		LogUtil.infoPrintf("请求结束[" + GenericUtil.dateformat.format(endTime) + "]耗时[" + (endTime - startTime) + "毫秒]");
		NodeList list =parser.parse(new HasAttributeFilter("type", "text/javascript"));
		//System.out.println(list.toHtml());
		ScriptTag jstag = null;
		for (int i = 0; i < list.size(); i++) {
			jstag = (ScriptTag) list.elementAt(i);
			if(jstag==null)continue;
			String scrstr=jstag.toPlainTextString();
			if(scrstr.contains("$.fost.LoginParams")){
				int beginindex=scrstr.indexOf("{");
				int endindex=scrstr.indexOf("};");
				System.out.println(beginindex+":"+endindex);
				scrstr=scrstr.substring(beginindex, endindex+1);
				System.out.println("jstag=="+scrstr);
				JSONObject jsonobj = new JSONObject(scrstr);
				System.out.println("SecurityToken=="+jsonobj.get("SecurityToken"));
				System.out.println("PasswordKey=="+jsonobj.get("PasswordKey"));
//				PasswordKey=scrstr.substring(scrstr.indexOf("\"")+1, scrstr.lastIndexOf("\""));
//				System.out.println("jstag=="+PasswordKey);
			}
		}
		return PasswordKey;
	}
}