/**
 * 商品列表页js
 */
$(document).ready(function() {
	// 判断页面元素是否存在
	if($("#dvMoneyMall").length <= 0){
		return;
	}
	
	// 获取栏目商品列表
	var request = {};
	request.currentUrl = (function(){
		var currentUrl = window.location.href;
		// 去除多余的参数，防止后台解析不正常编码字符串出现异常
		if (currentUrl.indexOf("?keyword=") != -1) {
			currentUrl = currentUrl.substring(0, currentUrl.indexOf("?keyword="));
		}
		return currentUrl;
	})();
//	request.currentUrl = "http://s.5173.com/wow-q2jm41-890-943-0-ou5epo-0-0-0-a-a-a-a-a-0-moneyaverageprice_asc-0-0.shtml";
//	request.currentUrl = "http://s.5173.com/bns-0-000txe-jfl1qs-iwys5h-0-0-0-0-a-a-a-a-a-0-0-0-0.shtml";
	
	$.ajax({
		type: "GET",
		url: baseServiceUrl + "services/goods/selectgoods",
		data: request,
		contentType: "application/json; charset=UTF-8",
		dataType:'jsonp',
		jsonp:'callback',
		success: function(resp) {
			var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
            	var goodsList = resp.goodsList;
            	buildGoodsList(goodsList);
            }
		}
	});
});

// 组装商品列表页面
function buildGoodsList(goodsList){
	if(isNull(goodsList)){
		return;
	}
	
	var html = "<div class='hot_recommen_tt'> <em></em>";
	html += "<div class='hot_buy_title'>";
	html += "<div class='hot_buy_w1'>发货速度</div>";
	html += "<div class='hot_buy_w2'>商品单价</div>";
	html += "<div class='hot_buy_w3'>购买数量</div>";
	html += "<div class='hot_buy_w4'>付款金额</div>";
	html += "</div><strong><a id='yxbguanggao' href='http://yxbmall.5173.com/applyseller.html' target='_blank'>月销百万，立即免费入驻！</a></strong></div>";
	html += "<div class='hot_recobox'>";
	html += "<ul class='hot_buy_list'>";
	for (var int = 0; int < goodsList.length; int++) {
		var goodsInfo = goodsList[int];
		if(isNull(goodsInfo) || goodsInfo.isDeleted == true){
			continue;
		}
		
		var goodsInfoJson = $.toJSON(goodsInfo);
		goodsInfoJson = goodsInfoJson.replace("\\","/");
		var count = 5000;
		if(goodsInfo.gameName=="剑灵"){
			count = 10000;
			}
		if(goodsInfo.gameName=="魔兽世界(国服)"){
			count = 20000;
			}
		var image = isNull(goodsInfo.imageUrls)?"":buildImageUrl(goodsInfo.imageUrls,"55x55");
		var deliveryTime = isNull(goodsInfo.deliveryTime)?"":goodsInfo.deliveryTime;
		var title = isNull(goodsInfo.title)?"":goodsInfo.title;
		var price = toDecimal2(1/parseFloat(goodsInfo.unitPrice)); // 1元对应多少金
		var totalPrice = toDecimal2(parseFloat(goodsInfo.unitPrice) * count);
		var totalPriceArray = totalPrice.toString().split(".");
		var displayGameInfo = goodsInfo.region+"/"+goodsInfo.server+"/"+(isNull(goodsInfo.gameRace)?"":goodsInfo.gameRace);
		var moneyName = isNull(goodsInfo.moneyName)?"金":goodsInfo.moneyName;
		var itemhtml = "<li id='goodsId_"+goodsInfo.id+"' goodsInfo='"+goodsInfoJson+"'>";
		itemhtml += "<div class='hot_buy_detail_w0'><div class='hot_img'><img src='"+image+"' width='55' height='55'></div></div>";
		// itemhtml += "<div class='hot_buy_detail_w1'><p class='hot_time'><strong>"+deliveryTime+"</strong>分钟发货</p><p class='hot_service'>"+displayGameInfo+"</p></div>";
		itemhtml += "<div class='hot_buy_detail_w1'><p class='hot_time'><strong>"+title+"</strong></p><p class='hot_service'>"+displayGameInfo+"</p></div>";
		itemhtml += "<div class='hot_buy_detail_w2'><p class='hot_price'>1元=<span>"+price+"</span>"+moneyName+"</p></div>";
		itemhtml += "<div class='hot_buy_detail_w3'><div class='select_list_input' onclick='selectGoldList("+goodsInfo.id+")'>" +
				"<input id='"+goodsInfo.id+"' class='input_select_list_number' value="+count+"><span class='select_list_unit'>"+moneyName+"</span></div></div>";
		itemhtml += "<div class='hot_buy_detail_w4'><span class='hot_price_account_buy'><strong>"+totalPriceArray[0]+"</strong>."+totalPriceArray[1]+"<em>元</em></span></div>";
		itemhtml += "<div class='hot_buy_detail_w5'><a href='javascript:buynowHotList("+goodsInfo.id+");' class='a_buy_now'>立即购买</a></div>";
		itemhtml += "</li>";
		
		html += itemhtml;
	}
	
	html += "</ul></div>";
	$("#dvMoneyMall").attr("class","hot_recommend");
	$("#dvMoneyMall").html(html);

	// 入驻广告闪动
	setInterval(function(){changeColor()}, 300);
	
	// 实时监控，input金币数的变化，以及时生成价格
	$('.input_select_list_number').bind('input propertychange', function() {
		// 当前购买金币数
		var currentGoldCount = $(this).val();
		
		if(isNull(currentGoldCount)){
			currentGoldCount = 0;	
		}
		
		if(!isNumber(currentGoldCount)){
			alert("请输入数字");
			return;
		}
		
		// 数量不超过99999999
		if(parseInt(currentGoldCount)>=99999999){
			currentGoldCount = 99999999;
			$(this).val(99999999);
		}
		
		// 数量不小于0
		if(parseInt(currentGoldCount)<0){
			currentGoldCount = 0;
			$(this).val(0);
		}
		
	    var goodsInfoJson = $("#goodsId_"+$(this).attr("id")).attr("goodsInfo");
		var goodsInfo = $.evalJSON(goodsInfoJson);
		var disCountList = goodsInfo.discountList;
		
		var discount = 1;
		if(!isNull(disCountList)){
			for(var i=0; i<disCountList.length; i++){
				if(currentGoldCount >= disCountList[i].goldCount){
					discount = disCountList[i].discount;
				}
			}
		}
		var totalPrice = toDecimal2(parseFloat(goodsInfo.unitPrice) * parseInt(currentGoldCount) * parseFloat(discount));
		var totalPriceArray = totalPrice.toString().split(".");
		$("#goodsId_"+$(this).attr("id")+" .hot_price_account_buy").html("<strong>"
					+totalPriceArray[0]+"</strong>."+totalPriceArray[1]+"<em>元</em>");
	});
	
	// 文档初始加载时，默认商品金币数5000，触发事件，计算折扣
	$(".input_select_list_number").trigger("input");
	
}

// 展开折扣金币数列表
function selectGoldList(goodsId){
	// 判断当前折扣是否已展开
	if($("#goodsId_"+goodsId+" .select_list_popup").length > 0){
		// 删除全部下拉
		$(".select_list_popup").remove();
		return;
	}
	
	// 当前未展开
	// 删除其他全部下拉
	$(".select_list_popup").remove();
	
	// 展现当前下拉
	var goodsInfoJson = $("#goodsId_"+goodsId).attr("goodsInfo");
	var goodsInfo = $.evalJSON(goodsInfoJson);
	var disCountList = goodsInfo.discountList;
	
	// 商品未配置折扣数
	if(isNull(disCountList)){
		return;
	}
	
	var html = "<div class='select_list_popup'><ul>";
	
	for(var i=0; i<disCountList.length; i++){
		var disCountInfo = disCountList[i];
		var displayDiscount = toDecimal2(parseFloat(disCountInfo.discount)*10);
		var itemHtml = "<li goldCount="+disCountInfo.goldCount+"><a href='javascript:void(0)'>"+disCountInfo.goldCount+"<span>"
			+displayDiscount+"折</span></a></li>";
		html += itemHtml;
	}
	
	html += "</ul></div>";
	
	$("#goodsId_"+goodsId+" .select_list_unit").after(html);
	
	// 选择下拉绑定事件
	$(".select_list_popup li").click(function(){
		$("#goodsId_"+goodsId+" .input_select_list_number").val($(this).attr("goldCount"));
		// 触发事件
		$("#goodsId_"+goodsId+" .input_select_list_number").trigger("input");
	});
}

// 立即购买
function buynowHotList(goodsId){
	// 跳转创建订单页
	var goldCount = $("#goodsId_"+goodsId+" .input_select_list_number").val();
	if(isNull(goldCount) || goldCount == 0){
		alert("请输入需要购买的游戏币数量");
		return;
	}
	if(!isNumber(goldCount)){
		alert("请输入数字");
		return;
	}
	var goodsInfoJson = $("#goodsId_"+goodsId).attr("goodsInfo");
	var goodsInfo = $.evalJSON(goodsInfoJson);
	
	if(parseInt($("#goodsId_"+goodsId+" .hot_price_account_buy" + " strong").html()) < 20){
		alert("交易额不得少于20元");
		return;
	}
	var gameName = escape(goodsInfo.gameName);
	var gameRegion = escape(goodsInfo.region);
	var gameServer = escape(goodsInfo.server);
	var gameRace = isNull(goodsInfo.gameRace)?"":escape(goodsInfo.gameRace);
	var gameId = isNull(goodsInfo.gameId)?"":escape(goodsInfo.gameId);
	var regionId = isNull(goodsInfo.regionId)?"":escape(goodsInfo.regionId);
	var serverId = isNull(goodsInfo.serverId)?"":escape(goodsInfo.serverId);
	var raceId = isNull(goodsInfo.raceId)?"":escape(goodsInfo.raceId);
	var goodsCat = escape(goodsInfo.goodsCat);
	
	window.open(baseHtmlUrl + "createorder.html?gameId="+gameId+"&regionId="+
			regionId+"&serverId="+serverId+"&raceId="+raceId+"&gameName="+gameName+"&gameRegion="+
			gameRegion+"&gameServer="+gameServer+"&gameRace="+gameRace+
				"&goodsCat="+goodsCat+"&goldCount="+goldCount+"&", "_blank");

}

// 更改入驻文字颜色
var i = 0;
function changeColor(){
	$("#yxbguanggao").css("color",i==0?"#06c":"#f60");
	i==2?i=0:i++;
	
}
