<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.index.title")}[#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
<meta name="author" content="progr1mmer" />
<link href="${base}/admin/css/common.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.input .powered {
	font-size: 11px;
	text-align: center;
	color: #cccccc;
}
</style>
</head>
<body>
	<div class="path">
		${message("admin.index.title")}
	</div>
	<table class="input">
		<tr>
			<td colspan="4">
				&nbsp;
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.index.marketableProductCount")}:
			</th>
			<td>
				${marketableProductCount}
			</td>
			<th>
				${message("admin.index.notMarketableProductCount")}:
			</th>
			<td>
				${notMarketableProductCount}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.index.stockAlertProductCount")}:
			</th>
			<td>
				${stockAlertProductCount}
			</td>
			<th>
				${message("admin.index.outOfStockProductCount")}:
			</th>
			<td>
				${outOfStockProductCount}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.index.waitingPaymentOrderCount")}:
			</th>
			<td>
				${waitingPaymentOrderCount}
			</td>
			<th>
				${message("admin.index.waitingShippingOrderCount")}:
			</th>
			<td>
				${waitingShippingOrderCount}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.index.memberCount")}:
			</th>
			<td>
				${memberCount}
			</td>
			<th>
				${message("admin.index.unreadMessageCount")}:
			</th>
			<td>
				${unreadMessageCount}
			</td>
		</tr>
		<tr>
			[#if setting.isShowCopyright]
                <td class="powered" colspan="4">
                    ${setting.copyright}
                </td>
			[/#if]
		</tr>
	</table>
</body>
</html>
