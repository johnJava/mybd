<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>dnf地下城与勇士装备抢单器</title> 
  <link rel="stylesheet" type="text/css" href="css/button.css" /> 
  <link rel="stylesheet" type="text/css" href="css/toolstip.css" /> 
  <link rel="stylesheet" type="text/css" href="css/icon.css" /> 
  <link rel="shortcut icon" href="http://images.5173cdn.com/images/img/ico.ico" /> 
  <link rel="stylesheet" type="text/css" href="css/common.css" /> 
  <link rel="stylesheet" type="text/css" href="css/fed.css" /> 
  <link rel="stylesheet" type="text/css" href="css/page_v1.css" /> 
  <link rel="stylesheet" type="text/css" href="css/tab_v1_1.css" /> 
  <link rel="stylesheet" type="text/css" href="css/searchlist_v3_7.css" /> 
  <link rel="stylesheet" type="text/css" href="css/s_ztc.css" /> 
  <link rel="stylesheet" type="text/css" href="css/include.css" /> 
  <style class="firebugResetStyles" type="text/css" charset="utf-8">/* See license.txt for terms of usage */
	
	.tb_head .info, .pdlist .pdlist_info {
		padding-left: 12px;
		text-align: center;
		width: 303px;
	}
	
	.tb_head .ensure {
		text-align: center;
		text-indent: 10px;
	}
	.tb_head .ensure, .pdlist .pdlist_ensure {
		width: 90px;
	}
	.pdlistbox .sin_pdlbox ul.pdlist_ensure {
		height: auto;
		min-height: 22px;
		width: 90px;
	}
	
	label{
		font-family: Tahoma;
		font-size: 12px;
		vertical-align: middle;
		height: 28px;
		width: 60px;
	}
	.clearfix tr{
		height: 25px;
	}
	.pdlistbox .sin_pdlbox ul.pdlist_info li.tt a { 
		font-size: 12px;
	}
	.pdlistbox .sin_pdlbox ul.pdlist_time {
		width:80px;
	}
	.pdlistbox .sin_pdlbox ul.pdlist_delivery {
		width:165px;
	}
	.header_box {
		
		padding:20px 0px 0px 0px;
		
	}
.pdlistbox .sin_pdlbox ul.pdlist_ensure li {
    text-align: center;
    width: 65px;
}

	</style> 
  <script type="text/javascript" src="js/jquery-1.5.2.min.js" charset="utf-8"></script> 
  <script language="JavaScript" type="text/javascript" src="js/s-ztc.js"></script> 
  <script type="text/javascript">document.domain = "5173.com";</script> 
  <script src="http://urchin.5173.com/urchin.js" type="text/javascript"></script> 
 </head> 
 <body> 
  <div class="header"> 
   <!-- 顶部登陆条+快捷菜单条 -->
   <!--网站Logo+四级联动搜索条 border:1px solid #FF88C2; --> 
   <div class="header_box clearfix"> 
    <form id="gsForm" method="get" class="clearfix"> 
     <div id="userinfo" style="font-size: 12px;"> 
      <table width="947" border="0"> 
       <tbody>
        <tr> 
         <td>5173帐号：</td> 
         <td>选择游戏：</td> 
         <td>收货角色：</td> 
         <td>选择客服：</td> 
         <td><input type="button" id="startscan" value="开始扫描" style="width:240px;" /></td> 
        </tr> 
        <tr> 
         <td><input type="text" id="gameusername" style="width:160px;" /></td> 
         <td> <select name="gametype" style="width:160px;"> <option value="dnf">dnf</option> </select> </td> 
         <td><input type="text" id="txtGameRole" style="width:160px;" /></td> 
         <td> <select id="customer" style="width:160px;"> <option value="Star1">一星</option> <option value="Star5">五星</option> </select> </td> 
         <td><input type="button" id="stopscan" value="停止扫描" style="width:240px;" /></td> 
        </tr> 
        <tr> 
         <td>5173密码：</td> 
         <td>选择区：</td> 
         <td>角色等级：</td> 
         <td>价格大于等于：</td> 
         <td rowspan="7"><textarea id="outputPanel" style="width:240px;height:175px;font-size: 10px"></textarea></td> 
        </tr> 
        <tr> 
         <td><input type="password" id="password" style="width:160px;" /></td> 
         <td> <select name="gamedistict" style="width:160px;"> <option value="alldis">全部区</option> </select> </td> 
         <td><input type="text" id="rolelevel" style="width:160px;" /></td> 
         <td><input type="text" id="minprice" style="width:160px;" /></td> 
        </tr> 
        <tr> 
         <td><input type="button" id="logingame" value="登录5173" style="width:160px;" /></td> 
         <td>选择服：</td> 
         <td>联系电话：</td> 
         <td>信誉小于等于：</td> 
        </tr> 
        <tr> 
         <td> <input type="checkbox" id="radio" />开启新单声音提醒 </td> 
         <td> <select name="gameservice" style="width:160px;"> <option value="alldis">全部服</option> </select> </td> 
         <td><input type="text" id="txtBuyerTel" style="width:160px;" /></td> 
         <td><select name="creditlevel" style="width:160px;"> <option value="3">三星</option> </select></td> 
        </tr> 
        <tr> 
         <td> <input type="checkbox" id="toshop" />下单后自动打开付款界面 </td> 
         <td>选择问题;</td> 
         <td>问题答案：</td> 
         <td>联系QQ;</td> 
        </tr> 
        <tr> 
         <td> <input type="checkbox" id="allauto" />开启一键付款功能（需开通） </td> 
         <td> <select name="selectprob" style="width:160px;"> <option value="fathername">您父亲的名字是？</option> </select> </td> 
         <td><input type="text" id="ans" style="width:160px;" /></td> 
         <td><input type="text" id="txtBuyerQQ" style="width:160px;" /></td> 
        </tr> 
         <tr> 
         <td> <label id="query" style="width:160px;"></label> </td> 
         <td> <label id="avrtime" style="width:160px;"></label> </td> 
         <td colspan=3> <label  style="width:160px;"></label> </td> 
        </tr> 
       </tbody>
      </table> 
     </div> 
    </form> 
   </div> 
   <script type="text/javascript">

    function Login(loginType) {
        if (loginType == "common")
            window.location = "https://passport.5173.com/?UnDecode=1&returnUrl=" + escape(window.location.href);
        else if (loginType == "qq")
            window.location = "https://passport.5173.com/Partner/LoginFrom?UnDecode=1&appNo=qq&returnUrl=" + escape(window.location.href);
    }

</script> 
     <!--<p id="query"></p>
    <p id="avrtime"></p>
    <p id="session"> <%="Session ID = " + session.getId() %> </p>
    <div id="outputPanel"></div>   -->
   <div class="search-body clearfix"> 
    <div class="layout"> 
     <div class="tab_v1_1 v2"></div> 
     <div class="mainbody"> 
      <div class="sort_box"></div> 
      <script type="text/javascript" src="http://img01.5173cdn.com/fed/build/1.00/js/fed.share.js"></script> 
      <div class="pdlist equipment_list"> 
       <ul id="headerForEquipment" class="tb_head equipment_head sort_bottom1"> 
        <li class="info">标题</li> 
        <li class="price">价格</li> 
        <li class="ensure" style=" width: 85px;">下单</li> 
        <li class="num" style="width:100px">区服</li> 
        <li class="ensure" style="width:80px">卖家信用</li> 
        <li class="delivery" style="width: 150px;">订单编号</li> 
        <li class="delivery" style="width: 120px;">更新时间</li> 
       </ul> 
       <div class="pdlistbox" id="resultlist"> 
        
       </div> 
      </div> 
     </div> 
    </div> 
   </div> 
   <div class="blank10"></div> 
  </div> 
  <script type="text/javascript" src="js/fed-min.js"></script> 
  <script type="text/javascript" src="js/fed.follow.js"></script> 
  <script type="text/javascript" src="js/search/search_v36a.js" charset="utf-8"></script> 
  <script type="text/javascript" src="js/fed-std/layer/v1/popup_layer.js" charset="utf-8"></script> 
  <!-- WiseMedia检测代码 --> 
  <script type="text/javascript" src="http://aw.kejet.net/t?p=K0&amp;c=vp&amp;er=1&amp;kd=1&amp;sid=1730880509812150273&amp;zid=1730880509812150272"></script> 
  <script type="text/javascript" src="js/jquery.json-2.4.min.js"></script> 
  <script type="text/javascript" src="js/common.js"></script> 
  <script type="text/javascript" src="js/goodslist.js"></script>   
  <script type="text/javascript">  
        var infoPanel = document.getElementById('resultlist'); // 输出结果面板  
        var outputPanel = document.getElementById('outputPanel'); // 输出结果面板  
        var query = document.getElementById('query'); 
        var avrtime = document.getElementById('avrtime'); 
        var sendButton = document.getElementById('startscan');// 建立连接按钮  
        //var connButton = document.getElementById('loginsoft');// 建立连接按钮  
        var stopButton = document.getElementById('stopscan');// 建立连接按钮  
        var loginButton = document.getElementById('logingame');// 建立连接按钮  
           // 控制台输出对象  
        var console = {log : function(text) {
        	    if(text.indexOf("grabber_query") != -1){
        	    	query.innerHTML = text.substring(text.indexOf("grabber_query")+"grabber_query".length);
        	    }else if(text.indexOf("grabber_avrtime") != -1){
        	    	avrtime.innerHTML = text.substring(text.indexOf("grabber_avrtime")+"grabber_avrtime".length);
        	    }else if(text.indexOf("grabber_node") != -1){
        	    	infoPanel.innerHTML = text.substring(text.indexOf("grabber_node")+"grabber_node".length) + "<br>"+infoPanel.innerHTML ;
        	    }else{
        	    	outputPanel.innerHTML = text+ "\n"+outputPanel.innerHTML;
        	    }
        	}};  
        // WebSocket演示对象  
        var demo = {  
            socket : null,  // WebSocket连接对象  
            host : '',      // WebSocket连接 url  
            connect : function() {  // 连接服务器  
                window.WebSocket = window.WebSocket || window.MozWebSocket;  
                if (!window.WebSocket) {    // 检测浏览器支持  
                    console.log('Error: WebSocket is not supported .');  
                    return;  
                }  
                this.socket = new WebSocket(this.host); // 创建连接并注册响应函数  
                this.socket.onopen = function(){
                	console.log("登录成功");
                	logingame();
                };  
                this.socket.onmessage = function(message) {
                	console.log(message.data);
                	};  
                this.socket.onclose = function(){  
                    console.log("server is closed .");  
                    demo.socket = null; // 清理  
                };  
            },  
            send : function(message) {  // 发送消息方法  
                if (this.socket) {  
                    this.socket.send(message);  
                    return true;  
                }  
                console.log('please connect to the server first !!!');  
                return false;  
            }  
        };  
        // 初始化WebSocket连接 url  
        demo.host=(window.location.protocol == 'http:') ? 'ws://' : 'wss://' ;  
        demo.host += window.location.host + '/grabber/websocket/say';  
        
       /* connButton.onclick = function() {  
            if (!demo.socket)   demo.connect();  
            else console.log('websocket already exists .');  
        };*/ 
     // 初始化按钮点击事件函数  
        sendButton.onclick = function() {
            var message ="{action:start,msg:{creditlevel:3}}";  
            if (!message) return;  
            if (!demo.send(message)) return;  
        }; 
        stopButton.onclick = function() {
            var message ="{action:stop,msg:{creditlevel:3}}";  
            if (!message) return;  
            if (!demo.send(message)) return;  
        };  
        loginButton.onclick = function() {
        	autoConn();
        };  
        function logingame(){
        	var message ={}; 
        	var params={};
        	//params.price=document.getElementById('price').value;
			params.txtGameRole=document.getElementById('txtGameRole').value;
			params.rolelevel=document.getElementById('rolelevel').value;
			params.txtBuyerTel=document.getElementById('txtBuyerTel').value;
			params.txtBuyerQQ=document.getElementById('txtBuyerQQ').value;
			params.customer=document.getElementById('customer').value;
			params.username=document.getElementById('gameusername').value;
			params.password=document.getElementById('password').value;
			message.action="login";
			message.msg=params;
			//alert(JSON.stringify(jsObj));
			//alert(JSON.stringify(message));
			if (!message) return;  
            if (!demo.send(JSON.stringify(message))) return; 
        }
        function shop(params) {
        	var message ={}; 
        	message.action="shop";
        	message.msg=params;
            if (!message) return;  
            if (!demo.send(JSON.stringify(message))) return;  
        };  
        function autoConn(){  
            if (!demo.socket)   demo.connect();  
            else{
            	//console.log('websocket already exists .');  
            	logingame();
            } 
        }; 
        
    </script>  
 </body>
</html>