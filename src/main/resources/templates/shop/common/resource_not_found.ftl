<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="author" content="progr1mmer" />
    <title>${message("shop.resourceNotFound.title")}[#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
    <link href="${base}/shop/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${base}/shop/css/error.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="container-fluid">
        <div class="error">
			<div class="main">
				<dl>
					${message("shop.resourceNotFound.message")}
					<dd>
						<a href="javascript:" onclick="window.history.back(); return false;">&gt;&gt; ${message("shop.resourceNotFound.back")}</a>
					</dd>
				</dl>
			</div>
		</div>
	</div>
	[#include "/shop/include/footer.ftl" /]
    <script type="text/javascript" src="${base}/shop/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/bootstrap.min.js"></script>
</body>
</html>