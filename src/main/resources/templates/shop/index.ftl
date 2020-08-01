<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
[@seo type = "index"]
	<title>[#if seo.title??][@seo.title?interpret /][#else]${message("shop.index.title")}[/#if][#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
	<meta name="author" content="progr1mmer" />
	[#if seo.keywords??]
		<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if seo.description??]
		<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
[/@seo]
<meta http-equiv="pragma" content="no-cache" />
<link rel="icon" href="${base}/favicon.ico" type="image/x-icon" />
<link href="${base}/shop/slider/slider.css" rel="stylesheet" type="text/css" />
<link href="${base}/shop/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/shop/css/index.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/shop/js/jquery.js"></script>
<script type="text/javascript" src="${base}/shop/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/shop/slider/slider.js"></script>
<script type="text/javascript" src="${base}/shop/js/common.js"></script>
<script type="text/javascript">

// 加入购物车	
function addCart(path) {
	window.open("${base}" + path);
}

$().ready(function() {

	var $slider = $("#slider");  
	var $newArticleTab = $("#newArticle .tab");
	var $promotionProductTab = $("#promotionProduct .tab");
	var $promotionProductInfo = $("#promotionProduct .info");
	var $hotProductTab = $("#hotProduct .tab");
	var $newProductTab = $("#newProduct .tab");
	
	$slider.nivoSlider({
		effect: "random",
		animSpeed: 1000,
		pauseTime: 6000,
		controlNav: true,
		keyboardNav: false,
		captionOpacity: 0.4
	});
	
	$newArticleTab.tabs("#newArticle .tabContent", {
		tabs: "li",
		event: "mouseover",
		initialIndex: 1
	});
	
	$promotionProductTab.tabs("#promotionProduct .tabContent", {
		tabs: "li",
		event: "mouseover"
	});
	
	$hotProductTab.tabs("#hotProduct .tabContent", {
		tabs: "li",
		event: "mouseover"
	});
	
	$newProductTab.tabs("#newProduct .tabContent", {
		tabs: "li",
		event: "mouseover"
	});
	
	function promotionInfo() {
		$promotionProductInfo.each(function() {
			var $this = $(this);
			var beginDate = $this.attr("beginTimeStamp") != null ? new Date(parseFloat($this.attr("beginTimeStamp"))) : null;
			var endDate = $this.attr("endTimeStamp") != null ? new Date(parseFloat($this.attr("endTimeStamp"))) : null;
			if (beginDate == null || beginDate <= new Date()) {
				if (endDate != null && endDate >= new Date()) {
					var time = (endDate - new Date()) / 1000;
					$this.html("${message("shop.index.remain")}:<strong>" + Math.floor(time / (24 * 3600)) + "<\/strong> ${message("shop.index.day")} <strong>" + Math.floor((time % (24 * 3600)) / 3600) + "<\/strong> ${message("shop.index.hour")} <strong>" + Math.floor((time % 3600) / 60) + "<\/strong> ${message("shop.index.minute")}");
				} else if (endDate != null && endDate < new Date()) {
					$this.html("${message("shop.index.ended")}");
				} else {
					$this.html("${message("shop.index.going")}");
				}
			}
		});
	}
	
	promotionInfo();
	setInterval(promotionInfo, 60 * 1000);

});
function prom(){
	$("#mobileRecharge").show();
}
function mobileRecharge(){
	var phoneNo=$("#phoneNo").val();
	var amount=$("#amount").val();
	var money=$('input:radio:checked').val();
	if(amount==""){
		amount=money;
	}
	/* alert(phoneNo);
	alert(amount);
	alert(money); */
	var type=isPhoneNum (phoneNo);
	/*alert(type);*/
	var YDUrl="http://shop.10086.cn/i/?f=rechargecredit&mobileNo="+phoneNo+"&amount="+amount;
	var LTUrl="https://upay.10010.com/npfweb/npfcellweb/phone_recharge_fill.htm?menuId=000200010001";
	var DXUrl="http://www.189.cn/chongzhi/";
	switch(type){
	case 1:
		window.open(YDUrl);
		break;
	case 2:
		window.open(LTUrl);
		break;
	case 3:
		window.open(DXUrl);
		break;
	case 4:
		alert('不确定该手机号码！');
		break;
		default:
			alert('这不是手机号码！');
	}
	
	/*var AjaxURL= "shop/mobileRecharge/recharge.html";
	 $.post(AjaxURL,
			{
			 phoneNo:phoneNo,
			 amount:amount},
			function(data){
	}); */
}
/*
 * 判断是否是正确的手机号，以及手机的运营商
 * @param {String} num
 * 
 * 返回值:
 *      0 不是手机号码
 *      1 移动
 *      2 联通
 *      3 电信
 *      4 不确定
 */
function isPhoneNum (num) {
    var flag = 0;
    var phoneRe = /^1\d{10}$/;  
    var dx = [133,153,180,181,189]; /*电信*/
    var lt = [130,131,132,145,155,156,185,186];/*联通*/
    var yd = [134,135,136,137,138,139,147,150,151,152,157,158,159,182,183,184,187,188];/*移动*/
     
    function inArray(val,arr){
        for(i in arr){
            if(val == arr[i]) return true;
        }
        return false;
    }   
 
    if(phoneRe.test(num)){
        var temp = num.slice(0,3);
        if(inArray(temp,yd)) return 1;
        if(inArray(temp,lt)) return 2;
        if(inArray(temp,dx)) return 3;
        return 4;
    }
    return flag;    
}
</script>



</head>
<body>
	[#include "/shop/include/header.ftl" /]
	<div class="container index">
		<div class="span30">
			[@ad_position id = 3 /]
		</div>
		<div class="span6 last">
			<div id="newArticle" class="newArticle">
				<div id="newArticle" class="newArticle">
					<a href="#"><img src="${base}/shop/images/tuangou.jpg" /></a>
				</div>
			</div>
		</div>
		<div class="span30">
			<div id="promotionProduct" class="promotionProduct">
				[@promotion_list hasEnded = false count = 4]
					[#list promotions as promotion]
						<ul class="tabContent">
							[@product_list promotionId = promotion.id count = 4]
								[#list products as product]
									<li>
										<div class="product">
											<a href="${base}${product.path}" title="${product.name}" target="_blank">
												<img src="[#if product.image??]${product.image}[#else]${setting.defaultThumbnailProductImage}[/#if]" alt="${product.name}" />
											</a>
										</div>
									</li>
								[/#list]
							[/@product_list]
						</ul>
					[/#list]
				[/@promotion_list]
			</div>
		</div>
		
		<div class="span31 last">
			<div id="easyLife" class="easyLife">
				<div id="easyLifeBackground" class="backGround">
					<div class="util"></div>
					<div class="detail"><img onclick="prom();" src="${base}/shop/images/phoneBill.jpg" /><a href="#" onclick="prom();">话费充值</a></div>
					<div class="detail"><img src="${base}/shop/images/electricityBill.jpg" /><a href="#">缴纳电费</a></div>
					<div class="detail"><img src="${base}/shop/images/waterBill.jpg" /><a href="#">缴交水费</a></div>
					<div class="detail"><img src="${base}/shop/images/netBill.jpg" /><a href="#">缴宽带费</a></div>
					<div class="detail"><img src="${base}/shop/images/flyBill.jpg" /><a href="#">预订机票</a></div>
					<div class="detail"><img src="${base}/shop/images/trainBill.jpg" /><a href="#">订火车票</a></div>
					<div class="detail"><img src="${base}/shop/images/insuranceBill.jpg" /><a href="#">购买保险</a></div>
					<div id="mobileRecharge" style="
						width:600px;
						height:400px;
						position: fixed;
						left:20%;
						top:30%;
						display:none;
						color:black;
						font-size:24px;
						text-align:center;
						background-color: green;
						opacity:0.9;
						filter:alpha(opacity=90); 
						z-index:10;
					">
						<form action="" style="margin-top:20%;margin-left:20%;text-align: center;">
							<table class="input">
							<tr>
								<th>
									${message("shop.index.phoneNo")}:
								</th>
								<td>
									<input id="phoneNo" type="tel" min="1" max="11" />
								</td>
							</tr>
							<tr>
								<th>
									${message("shop.index.amount")}:
								</th>
								<td>
									<label for="money1">
									<input id="money1"  type="radio" name="amount" 
									value="30" checked="checked" />30</label>
									<label for="money2">
									<input id="money2" type="radio" name="amount" 
									value="50" />50</label>
									<label for="money3">
									<input id="money3" type="radio" name="amount" 
									value="100" />100</label><br/>
									<label for="money4">
									<input id="money4" type="radio" name="amount" 
									value="300" />300</label>
									<label for="money5">
									<input id="money5" type="radio" name="amount" 
									value="500" />500</label>
									&nbsp;&nbsp;<input id="amount" onfocus="javascript:$('input:radio').prop('checked',false);" style="width:40px;" type="number"/>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<input class="button" type="button" onclick="mobileRecharge();javascript:$('#mobileRecharge').hide();" value="立即充值"/>
									<input class="button" onclick="javascript:$('#mobileRecharge').hide();" type="button" value="取消"/>
								</td>
							</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="span32">
				<div id="hotProduct" class="hotProduct clearfix">
					[@product_category_root_list count = 8]
					<div class="title">
						<strong>${message("shop.index.hotProduct")}</strong>
						<a href="${base}/product/list.html?tagIds=1" target="_blank"></a>
					</div>
					<p>&nbsp;</p>
					<p>&nbsp;</p>
					<p>&nbsp;</p>
					<p>&nbsp;</p>
						[#list productCategories as productCategory]
						<ul class="tabContent">
							[@product_list productCategoryId = productCategory.id tagIds = 1 count = 4]
								[#list products as product]
									<li>
										<a href="${base}${product.path}" title="${product.name}" target="_blank"><img src="[#if product.image??]${product.image}[#else]${setting.defaultThumbnailProductImage}[/#if]" alt="${product.name}" /></a>
										<strong>￥${product.price}</strong> <br />
										<br />
										<div>简介:</div>${product.introduction} 
										<div>&nbsp;&nbsp;关键词:${product.keyword} </div>
										<br />
										<br />
										<br />
										已有[#if product.review != null]${product.review}[#else]0[/#if]评论	<br />
										<input type="button" class="indexCollect" value="" /> 
										<input type="button" class="indexAddCart" value="" onclick="addCart('${product.path}')" />
									</li>
								[/#list]
							[/@product_list]
						</ul>
						[/#list]
					[/@product_category_root_list]
				</div>
		</div>
		<div class="span32">
			<div id="newProduct" class="newProduct clearfix">
				[@product_category_root_list count = 8]
					<div class="title">
						<strong>${message("shop.index.newProduct")}</strong>
						<a href="${base}/product/list.html?tagIds=2" target="_blank"></a>
					</div>
					<p>&nbsp;</p>
					<p>&nbsp;</p>
					<p>&nbsp;</p>
					<p>&nbsp;</p>
					[#list productCategories as productCategory]
						<ul class="tabContent">
							[@product_list productCategoryId = productCategory.id tagIds = 2 count = 4]
								[#list products as product]
									<li>
										<a href="${base}${product.path}" title="${product.name}" target="_blank"><img src="[#if product.image??]${product.image}[#else]${setting.defaultThumbnailProductImage}[/#if]" alt="${product.name}" /></a>
										<strong>￥${product.price}</strong> <br />
										<br />
										<div>简介:</div>${product.introduction} 
										<div>&nbsp;&nbsp;关键词:${product.keyword} </div>
										<br />
										<br />
										<br />
										已有[#if product.review != null]${product.review}[#else]0[/#if]评论   <br /> 
										<input type="button" class="indexCollect" value="" /> 
										<input type="button" class="indexAddCart" value="" onclick="addCart('${product.path}')" />
									</li>
								[/#list]
							[/@product_list]
						</ul>
					[/#list]
				[/@product_category_root_list]
			</div>
		</div>
		<div class="span29">
			<div class="friendLink">
					<div align="center">
						<img src="${base}/shop/images/zheng.jpg"  />
						<img src="${base}/shop/images/zheng2.jpg" />
						<img src="${base}/shop/images/kuaidi.jpg" />
						<img src="${base}/shop/images/kuaidi2.jpg" />
						<img src="${base}/shop/images/shouhou.jpg" />
						<img src="${base}/shop/images/shouhou2.jpg" />
						<img src="${base}/shop/images/guanyu.jpg" />
						<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=123456789&site=qq&menu=yes"><img src="${base}/shop/images/guanyu2.jpg" /></a>
					</div>
			</div>
		</div>
	</div>
	[#include "/shop/include/footer.ftl" /]
</body>
</html>