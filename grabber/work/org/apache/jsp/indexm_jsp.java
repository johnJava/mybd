/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.57
 * Generated at: 2015-01-03 07:05:08 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class indexm_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("    <!DOCTYPE HTML>  \n");
      out.write("    <html>  \n");
      out.write("    <head>  \n");
      out.write("    <title>WebSocket demo</title>  \n");
      out.write("    <style>  \n");
      out.write("    body {padding: 40px;}  \n");
      out.write("    #outputPanel {margin: 10px;padding:10px;background: #eee;border: 1px solid #000;min-height: 300px;}  \n");
      out.write("    </style>  \n");
      out.write("    </head>  \n");
      out.write("    <body>  \n");
      out.write("    <input type=\"text\" id=\"messagebox\" size=\"60\" />  \n");
      out.write("    <input type=\"button\" id=\"buttonSend\" value=\"Send Message\" />  \n");
      out.write("    <input type=\"button\" id=\"buttonConnect\" value=\"Connect to server\" />  \n");
      out.write("    <input type=\"button\" id=\"buttonClose\" value=\"Disconnect\" />  \n");
      out.write("    <p id=\"query\"></p>\n");
      out.write("    <p id=\"avrtime\"></p>\n");
      out.write("    <br>  \n");
      out.write("    ");
 out.println("Session ID = " + session.getId()); 
      out.write("  \n");
      out.write("    <div id=\"outputPanel\"></div>  \n");
      out.write("    </body>  \n");
      out.write("    <script type=\"text/javascript\">  \n");
      out.write("        var infoPanel = document.getElementById('outputPanel'); // è¾åºç»æé¢æ¿  \n");
      out.write("        var query = document.getElementById('query'); \n");
      out.write("        var avrtime = document.getElementById('avrtime'); \n");
      out.write("        var textBox = document.getElementById('messagebox');    // æ¶æ¯è¾å¥æ¡  \n");
      out.write("        var sendButton = document.getElementById('buttonSend'); // åéæ¶æ¯æé®  \n");
      out.write("        var connButton = document.getElementById('buttonConnect');// å»ºç«è¿æ¥æé®  \n");
      out.write("        var discButton = document.getElementById('buttonClose');// æ­å¼è¿æ¥æé®  \n");
      out.write("           // æ§å¶å°è¾åºå¯¹è±¡  \n");
      out.write("        var console = {log : function(text) {\n");
      out.write("        \t    if(text.indexOf(\"query\") != -1){\n");
      out.write("        \t    \tquery.innerHTML = text.substring(text.indexOf(\"query\")+\"query\".length);\n");
      out.write("        \t    }else if(text.indexOf(\"avrtime\") != -1){\n");
      out.write("        \t    \tavrtime.innerHTML = text.substring(text.indexOf(\"avrtime\")+\"avrtime\".length);\n");
      out.write("        \t    }else{\n");
      out.write("            \t\tinfoPanel.innerHTML = text + \"<br>\"+infoPanel.innerHTML ;\n");
      out.write("        \t    }\n");
      out.write("        \t}};  \n");
      out.write("        // WebSocketæ¼ç¤ºå¯¹è±¡  \n");
      out.write("        var demo = {  \n");
      out.write("            socket : null,  // WebSocketè¿æ¥å¯¹è±¡  \n");
      out.write("            host : '',      // WebSocketè¿æ¥ url  \n");
      out.write("            connect : function() {  // è¿æ¥æå¡å¨  \n");
      out.write("                window.WebSocket = window.WebSocket || window.MozWebSocket;  \n");
      out.write("                if (!window.WebSocket) {    // æ£æµæµè§å¨æ¯æ  \n");
      out.write("                    console.log('Error: WebSocket is not supported .');  \n");
      out.write("                    return;  \n");
      out.write("                }  \n");
      out.write("                this.socket = new WebSocket(this.host); // åå»ºè¿æ¥å¹¶æ³¨åååºå½æ°  \n");
      out.write("                this.socket.onopen = function(){console.log(\"websocket is opened .\");};  \n");
      out.write("                this.socket.onmessage = function(message) {console.log(message.data);};  \n");
      out.write("                this.socket.onclose = function(){  \n");
      out.write("                    console.log(\"websocket is closed .\");  \n");
      out.write("                    demo.socket = null; // æ¸ç  \n");
      out.write("                };  \n");
      out.write("            },  \n");
      out.write("            send : function(message) {  // åéæ¶æ¯æ¹æ³  \n");
      out.write("                if (this.socket) {  \n");
      out.write("                    this.socket.send(message);  \n");
      out.write("                    return true;  \n");
      out.write("                }  \n");
      out.write("                console.log('please connect to the server first !!!');  \n");
      out.write("                return false;  \n");
      out.write("            }  \n");
      out.write("        };  \n");
      out.write("        // åå§åWebSocketè¿æ¥ url  \n");
      out.write("        demo.host=(window.location.protocol == 'http:') ? 'ws://' : 'wss://' ;  \n");
      out.write("        demo.host += window.location.host + '/grabber/websocket/say';  \n");
      out.write("        // åå§åæé®ç¹å»äºä»¶å½æ°  \n");
      out.write("        sendButton.onclick = function() {  \n");
      out.write("            var message = textBox.value;  \n");
      out.write("            if (!message) return;  \n");
      out.write("            if (!demo.send(message)) return;  \n");
      out.write("            textBox.value = '';  \n");
      out.write("        };  \n");
      out.write("        connButton.onclick = function() {  \n");
      out.write("            if (!demo.socket)   demo.connect();  \n");
      out.write("            else console.log('websocket already exists .');  \n");
      out.write("        };  \n");
      out.write("        discButton.onclick = function() {  \n");
      out.write("            if (demo.socket) demo.socket.close();  \n");
      out.write("            else  console.log('websocket is not found .');  \n");
      out.write("        };  \n");
      out.write("    </script>  \n");
      out.write("    </html>  ");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
