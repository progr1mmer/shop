<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="author" content="progr1mmer" />
    [#if productCategory??]
        [@seo type = "productList"]
		    <title>[#if productCategory.seoTitle??]${productCategory.seoTitle}[#elseif seo.title??][@seo.title?interpret /][/#if][#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
            [#if productCategory.seoKeywords??]
			    <meta name="keywords" content="${productCategory.seoKeywords}" />
            [#elseif seo.keywords??]
			    <meta name="keywords" content="[@seo.keywords?interpret /]" />
            [/#if]
            [#if productCategory.seoDescription??]
			    <meta name="description" content="${productCategory.seoDescription}" />
            [#elseif seo.description??]
			    <meta name="description" content="[@seo.description?interpret /]" />
            [/#if]
        [/@seo]
    [#else]
	    <title>${message("shop.product.title")}[#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
    [/#if]
    <link href="${base}/shop/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/sweetalert2.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/product-complex.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="${base}/product/${product.sn}/list.html">
                    <i class="glyphicon glyphicon-menu-hamburger" aria-hidden="true"></i>
                </a>
            </div>
            <p class="navbar-text navbar-right">
                <a href="#" class="navbar-link"><i class="glyphicon glyphicon-list-alt" aria-hidden="true"></i></a>
                <a href="#" class="navbar-link"><i class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></i></a>
            </p>
        </div>
    </nav>

    <div class="list result">
        [#if page.content?has_content]
            [#list page.content?chunk(2) as row]
                <div class="line">
                    [#list row as product]
                        <a href="${base}${product.path}">
                            <img src="[#if product.image??]${product.image}[#else]${setting.defaultThumbnailProductImage}[/#if]" width="100%" height="100%"/>
                            <div class="text">
                                <p title="${product.name}">${abbreviate(product.name, 20)}</p>
                                <div class="price">
                                    <span class="salePrice">
                                        <small>${setting.currencySign}</small><span>${currency(product.price)}</span>
                                    </span>
                                    [#if setting.isShowMarketPrice]
                                        <span class="marketPrice">
                                            <small>${setting.currencySign}</small><del>${currency(product.marketPrice)}</del>
                                        </span>
                                    [/#if]
                                </div>
                            </div>
                        </a>
                        [#if !row_has_next]
                            <a href="javascript:" style="background-color: #f5f5f5"></a>
                        [/#if]
                    [/#list]
                </div>
            [/#list]
        [#else]
            ${message("shop.product.noListResult")}
        [/#if]
    </div>

    <nav class="navbar navbar-default navbar-fixed-bottom">
        <div class="listMenu">
            <a href="${base}/product/${product.sn}/index.html">
                <i class="glyphicon glyphicon-home" aria-hidden="true"></i><br/>
                <span>${message("shop.index.title")}</span>
            </a>
            <a href="#">
                <i class="glyphicon glyphicon-list-alt" aria-hidden="true"></i><br/>
                <span>订单追踪</span>
            </a>
            <a href="${base}/product/${product.sn}/list.html">
                <i class="glyphicon glyphicon-th-large" aria-hidden="true"></i><br/>
                <span>${message("shop.product.productCategory")}</span>
            </a>
            <a href="#">
                <i class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></i><br/>
                <span>${message("shop.cart.title")}</span>
            </a>
        </div>
    </nav>
    [#include "/shop/include/footer.ftl" /]
    <script type="text/javascript" src="${base}/shop/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/common.js"></script>
</body>
</html>