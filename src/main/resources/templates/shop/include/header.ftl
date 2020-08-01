<script type="text/javascript">
  
$().ready(function() {

	var $headerLogin = $("#headerLogin");
	var $headerRegister = $("#headerRegister");
	var $headerUsername = $("#headerUsername");
	var $headerLogout = $("#headerLogout");
	var $productSearchForm = $("#productSearchForm");
	var $keyword = $("#productSearchForm input");
	var defaultKeyword = "${message("shop.header.keyword")}";
	
	var username = getCookie("username");
	if (username != null) {
		$headerUsername.text("${message("shop.header.welcome")}, " + username).show();
		$headerLogout.show();
	} else {
		$headerLogin.show();
		$headerRegister.show();
	}
	
	$keyword.focus(function() {
		if ($keyword.val() == defaultKeyword) {
			$keyword.val("");
		}
	});
	
	$keyword.blur(function() {
		if ($keyword.val() == "") {
			$keyword.val(defaultKeyword);
		}
	});
	
	$productSearchForm.submit(function() {
		if ($.trim($keyword.val()) == "" || $keyword.val() == defaultKeyword) {
			return false;
		}
	});

});
</script>
<div class="container header">
	<div class="span25">
		<div class="topNav clearfix">
			<ul>
				<li id="headerLogin" class="headerLogin">
					<a href="${base}/login.html">${message("shop.header.login")}</a>|
				</li>
				<li id="headerRegister" class="headerRegister">
					<a href="${base}/register.html">${message("shop.header.register")}</a>|
				</li>
				<li id="headerUsername" class="headerUsername"></li>
				<li id="headerLogout" class="headerLogout">
					<a href="${base}/logout.html">[${message("shop.header.logout")}]</a>|
				</li>
				<li>
				 	<div class="collection">
						<img src="${base}/shop/images/collection.jpg" alt="收藏" />
					</div>
					<a href="http://localhost:8080/shop_yinong/logout.html">收藏</a>|
				</li>
				[@navigation_list position = "top"]
					[#list navigations as navigation]
						<li>
							<a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>
							[#if navigation_has_next][/#if]
						</li>
					[/#list]
				[/@navigation_list]
				<li>
					<div class="collection">
						<img src="${base}/shop/images/phone.jpg" alt="电话" />
					</div>
					[#if setting.phone??]
						${message("shop.header.phone")}:<strong>${setting.phone}</strong>
					[/#if]
				</li>
			</ul>
		</div>
	</div>
	<div class="span28">
		<div class="span4">
			<p>&nbsp;</p>
		</div>
		<div class="logo"> 
			<a href="${base}/"> <img src="${base}/shop/images/logo.gif" alt="" /> </a>
		</div>
		<div class="hotSearch">
			[#if setting.hotSearches?has_content]
					${message("shop.header.hotSearch")}:
				[#list setting.hotSearches as hotSearch]
					<a href="${base}/product/hotsearch.html?keyword=${hotSearch?url}">${hotSearch}</a>
				[/#list]
			[/#if]
		</div>
		<div class="search">
			<form id="productSearchForm" action="${base}/product/search.html" method="get">
				<input name="keyword" class="keyword" value="${productKeyword!message("shop.header.keyword")}" maxlength="30" height="40" />
				<button type="submit"><img src="${base}/shop/images/button.jpg" /></button>
			</form>	
		</div>
		<div class="cart">
			<a href="${base}/cart/list.html"><img src="${base}/shop/images/cart.jpg" /></a>
		</div>
	</div>
	<div class="span26">
		<ul class="mainNav">
				<div class="span4">
					<p>&nbsp;</p>
				</div>
				[@navigation_list position = "middle"]
					[#list navigations as navigation]
						<li[#if navigation.url = url] class="current"[/#if]>
							<a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>
								[#if navigation_has_next]|[/#if]
						</li>
					[/#list]
				[/@navigation_list]
		</ul>
	</div>
</div>