<div class="span6">
	<div class="info">
		<div class="top"></div>
		<div class="content">
			<p>${message("shop.member.navigation.welcome", member.username)}</p>
			<p>
				${message("shop.member.navigation.memberRank")}:
				<span class="red">${member.memberRank.name}</span>
			</p>
		</div>
		<div class="bottom"></div>
	</div>
	<div class="menu">
		<div class="title">
			<a href="${base}/member/index.html">${message("shop.member.navigation.title")}</a>
		</div>
		<div class="content">
			<dl>
				<dt>${message("shop.member.navigation.order")}</dt>
				<dd>
					<a href="${base}/member/order/list.html"[#if current == "orderList"] class="current"[/#if]>${message("shop.member.order.list")}</a>
				</dd>
				<dd>
					<a href="${base}/member/coupon_code/list.html"[#if current == "couponCodeList"] class="current"[/#if]>${message("shop.member.couponCode.list")}</a>
				</dd>
				<dd>
					<a href="${base}/member/coupon_code/exchange.html"[#if current == "couponCodeExchange"] class="current"[/#if]>${message("shop.member.couponCode.exchange")}</a>
				</dd>
			</dl>
			<dl>
				<dt>${message("shop.member.navigation.favorite")}</dt>
				<dd>
					<a href="${base}/member/favorite/list.html"[#if current == "favoriteList"] class="current"[/#if]>${message("shop.member.favorite.list")}</a>
				</dd>
				<dd>
					<a href="${base}/member/product_notify/list.html"[#if current == "productNotifyList"] class="current"[/#if]>${message("shop.member.productNotify.list")}</a>
				</dd>
				<dd>
					<a href="${base}/member/review/list.html"[#if current == "reviewList"] class="current"[/#if]>${message("shop.member.review.list")}</a>
				</dd>
				<dd>
					<a href="${base}/member/consultation/list.html"[#if current == "consultationList"] class="current"[/#if]>${message("shop.member.consultation.list")}</a>
				</dd>
			</dl>
			<dl>
				<dt>${message("shop.member.navigation.message")}</dt>
				<dd>
					<a href="${base}/member/message/send.html"[#if current == "messageSend"] class="current"[/#if]>${message("shop.member.message.send")}</a>
				</dd>
				<dd>
					<a href="${base}/member/message/list.html"[#if current == "messageList"] class="current"[/#if]>${message("shop.member.message.list")}</a>
				</dd>
				<dd>
					<a href="${base}/member/message/draft.html"[#if current == "messageDraft"] class="current"[/#if]>${message("shop.member.message.draft")}</a>
				</dd>
			</dl>
			<dl>
				<dt>${message("shop.member.navigation.profile")}</dt>
				<dd>
					<a href="${base}/member/profile/edit.html"[#if current == "profileEdit"] class="current"[/#if]>${message("shop.member.profile.edit")}</a>
				</dd>
				<dd>
					<a href="${base}/member/password/edit.html"[#if current == "passwordEdit"] class="current"[/#if]>${message("shop.member.password.edit")}</a>
				</dd>
				<dd>
					<a href="${base}/member/receiver/list.html"[#if current == "receiverList"] class="current"[/#if]>${message("shop.member.receiver.list")}</a>
				</dd>
			</dl>
			<dl>
				<dt>${message("shop.member.navigation.deposit")}</dt>
				<dd>
					<a href="${base}/member/deposit/recharge.html"[#if current == "depositRecharge"] class="current"[/#if]>${message("shop.member.deposit.recharge")}</a>
				</dd>
				<dd>
					<a href="${base}/member/deposit/list.html"[#if current == "depositList"] class="current"[/#if]>${message("shop.member.deposit.list")}</a>
				</dd>
			</dl>
		</div>
		<div class="bottom"></div>
	</div>
</div>