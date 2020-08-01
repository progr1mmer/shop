<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    [@seo type = "productContent"]
        <title>[#if product.seoTitle??]${product.seoTitle}[#elseif seo.title??][@seo.title?interpret /][/#if][#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
        <meta name="author" content="progr1mmer"/>
        [#if product.seoKeywords??]
            <meta name="keywords" content="${product.seoKeywords}"/>
        [#elseif seo.keywords??]
            <meta name="keywords" content="[@seo.keywords?interpret /]"/>
        [/#if]
        [#if product.seoDescription??]
            <meta name="description" content="${product.seoDescription}"/>
        [#elseif seo.description??]
            <meta name="description" content="[@seo.description?interpret /]"/>
        [/#if]
    [/@seo]
    <link href="${base}/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/libs/sweetalert2/css/sweetalert2.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/product.css" rel="stylesheet" type="text/css"/>
</head>
<body>
[#assign productCategory = product.productCategory /]
<div class="container-fluid">
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">Brand</a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li><a href="${base}/cart/list.html">${message("shop.cart.title")}<span class="sr-only">(current)</span></a></li>
                    <li><a href="#">${message("shop.order.info")}</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="image">
        <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
            [#if product.productImages?has_content]
                <!-- Indicators -->
                <ol class="carousel-indicators">
                    [#list product.productImages as productImage]
                        <li data-target="#carousel-example-generic" data-slide-to="${productImage_index}" [#if productImage_index == 0] class="active" [/#if]></li>
                    [/#list]
                </ol>
                <!-- Wrapper for slides -->
                <div class="carousel-inner" role="listbox">
                    [#list product.productImages as productImage]
                        <div class="item [#if productImage_index == 0] active [/#if]">
                            <img src="${productImage.large}" title="${productImage.title}" alt="${setting.siteName}" width="100%"/>
                        </div>
                    [/#list]
                </div>
            [#else]
                <!-- Indicators -->
                <ol class="carousel-indicators">
                    <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                </ol>
                <!-- Wrapper for slides -->
                <div class="carousel-inner" role="listbox">
                    <div class="item active">
                        <img src="${setting.defaultMediumProductImage}" alt="${setting.siteName}" width="100%" />
                    </div>
                </div>
            [/#if]

            <!-- Controls -->
            <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="basic">
            <div class="col-xs-12">
                <div class="price">
                    <small style="color: #ef0101">￥</small><span>${currency(product.price, true)}</span>
                    [#if setting.isShowMarketPrice]
                        <small>￥</small><del>${currency(product.marketPrice, true)}</del>
                    [/#if]
                </div>
                <div class="promotion">
                    [#if product.validPromotions?has_content]
                        [#list product.validPromotions as promotion]
                            <a href="#" [#if promotion.beginDate?? || promotion.endDate??]
                               title="${promotion.beginDate} ~ ${promotion.endDate}" [/#if]>${promotion.name}</a>
                            <a href="#" [#if promotion.beginDate?? || promotion.endDate??]
                               title="${promotion.beginDate} ~ ${promotion.endDate}" [/#if]>${promotion.name}</a>
                        [/#list]
                    [/#if]
                </div>
            </div>
            <div class="col-xs-12">
                <div class="name">
                    [#if product.brand?has_content]
                        <span>${product.brand.name} | ${product.name}</span>
                    [#else]
                        <span>${product.name}</span>
                    [/#if]
                </div>
                [#--<div class="name">${product.name}[#if product.isGift] [${message("shop.product.gifts")}][/#if]</div>--]
                <div class="sn">
                    <div>${message("Product.sn")}: ${product.sn}</div>
                    [#if product.scoreCount > 0]
                        <div>${message("Product.score")}:</div>
                        <div class="score${(product.score * 2)?string("0")}"></div>
                        <div>${product.score?string("0.0")} (${message("Product.scoreCount")}: ${product.scoreCount})</div>
                    [/#if]
                </div>
            </div>
        </div>

        [#if !product.isGift]
            <div class="action">
                <div class="col-xs-12">
                    [#if product.specifications?has_content]
                        <div id="specification" class="specification">
                            [#assign specificationValues = product.goods.specificationValues /]
                            [#list product.specifications as specification]
                                <dl>
                                    <dt>
                                        <span title="${specification.name}">${abbreviate(specification.name, 8)}</span>
                                    </dt>
                                    [#list specification.specificationValues as specificationValue]
                                        [#if specificationValues?seq_contains(specificationValue)]
                                            <dd>
                                                <a href="javascript:" class="${specification.type}[#if product.specificationValues?seq_contains(specificationValue)] selected[/#if]" val="${specificationValue.id}">
                                                    [#if specification.type == "text"]
                                                        ${specificationValue.name}
                                                    [#else]
                                                        <img src="${specificationValue.image}" alt="${specificationValue.name}" title="${specificationValue.name}"/>
                                                    [/#if]
                                                    [#--<span title="${message("shop.product.selected")}">&nbsp;</span>--]
                                                </a>
                                            </dd>
                                        [/#if]
                                    [/#list]
                                </dl>
                            [/#list]
                        </div>
                    [/#if]
                    [#if product.isOutOfStock]
                        <form id="productNotifyForm" action="${base}/product_notify/save" method="post">
                            <dl id="productNotify" class="productNotify">
                                <dt>${message("shop.product.productNotifyEmail")}:</dt>
                                <dd>
                                    <input type="text" name="email" maxlength="200"/>
                                </dd>
                            </dl>
                        </form>
                    [#else]
                        <div class="buy">
                            <div class="col-xs-6">
                                <button type="button" id="addCart" class="btn btn-primary">${message("shop.product.addCart")}</button>
                            </div>
                            <div class="col-xs-6">
                                <button type="button" id="buyNow" class="btn btn-success">${message("shop.product.buyNow")}</button>
                            </div>
                        [#--<input type="button" id="addCart" class="addCart" value=""/>
                        <input type="button" id="buyNow" class="buys" value=" "/>--]
                        </div>
                    [/#if]
                </div>
            </div>
        [/#if]

        [#if product.parameterValue?has_content]
            <div id="parameter" name="parameter" class="parameter">
                <div class="col-xs-12">
                    <div class="title">
                        <strong>${message("shop.product.parameter")}</strong>
                    </div>
                    <table class="table">
                        [#list productCategory.parameterGroups as parameterGroups]
                            <tr>
                                <th class="group" colspan="2">${parameterGroups.name}</th>
                            </tr>
                            [#list parameterGroups.parameters as parameter]
                                [#if product.parameterValue.get(parameter)??]
                                    <tr>
                                        <th>${parameter.name}</th>
                                        <td>${product.parameterValue.get(parameter)}</td>
                                    </tr>
                                [/#if]
                            [/#list]
                        [/#list]
                    </table>
                </div>
            </div>
        [/#if]

        [#if product.introduction?has_content]
            <div id="introduction" name="introduction" class="introduction">
                <div class="col-xs-12">
                    <div class="title">
                        <strong>${message("shop.product.introduction")}</strong>
                    </div>
                    <div>
                        ${product.introduction}
                    </div>
                </div>
            </div>
        [/#if]
    </div>
</div>
[#include "/shop/include/footer.ftl" /]
[#--<nav class="navbar navbar-default navbar-fixed-bottom">
    <button type="button" class="btn btn-warning">加入购物车</button><button type="button" class="btn btn-danger">立即购买</button>
</nav>--]
[#--<script type="text/javascript" src="${base}/libs/jquery/jquery.min.js"></script>--]
<script type="text/javascript" src="${base}/libs/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${base}/libs/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/libs/sweetalert2/js/sweetalert2.min.js"></script>
<script type="text/javascript" src="${base}/shop/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/shop/js/jquery.jqzoom.js"></script>
<script type="text/javascript" src="${base}/shop/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/shop/js/common.js"></script>
<script type="text/javascript">
    $().ready(function () {

        var $historyProduct = $("#historyProduct ul");
        var $clearHistoryProduct = $("#clearHistoryProduct");
        var $zoom = $("#zoom");
        var $scrollable = $("#scrollable");
        var $thumbnail = $("#scrollable a");
        var $specification = $("#specification dl");
        var $specificationTitle = $("#specification div");
        var $specificationValue = $("#specification a");
        var $productNotifyForm = $("#productNotifyForm");
        var $productNotify = $("#productNotify");
        var $productNotifyEmail = $("#productNotify input");
        var $addProductNotify = $("#addProductNotify");
        var $quantity = $("#quantity");
        var $increase = $("#increase");
        var $decrease = $("#decrease");
        var $addCart = $("#addCart");
        var $buyNow = $("#buyNow");
        var $addFavorite = $("#addFavorite");
        var $window = $(window);
        var $bar = $("#bar ul");
        var $introductionTab = $("#introductionTab");
        var $parameterTab = $("#parameterTab");
        var $reviewTab = $("#reviewTab");
        var $consultationTab = $("#consultationTab");
        var $introduction = $("#introduction");
        var $parameter = $("#parameter");
        var $review = $("#review");
        var $addReview = $("#addReview");
        var $consultation = $("#consultation");
        var $addConsultation = $("#addConsultation");
        //var barTop = $bar.offset().top;
        var productMap = {};
    [@compress single_line = true]
        productMap[${product.id}] = {
            path: null,
            specificationValues: [
                [#list product.specificationValues as specificationValue]
                    "${specificationValue.id}"[#if specificationValue_has_next],[/#if]
                [/#list]]
        };
        [#list product.siblings as product]
            productMap[${product.id}] = {
                path: "${product.path}",
                specificationValues: [
                    [#list product.specificationValues as specificationValue]
                        "${specificationValue.id}"[#if specificationValue_has_next],[/#if]
                    [/#list]]
            };
        [/#list]
    [/@compress]

        // 锁定规格值
        lockSpecificationValue();

        // 商品图片放大镜
        /*$zoom.jqzoom({
            zoomWidth: 368,
            zoomHeight: 368,
            title: false,
            showPreload: false,
            preloadImages: false
        });*/

        // 商品缩略图滚动
        $scrollable.scrollable();

        $thumbnail.hover(function () {
            var $this = $(this);
            if ($this.hasClass("current")) {
                return false;
            } else {
                $thumbnail.removeClass("current");
                $this.addClass("current").click();
            }
        });

        // 规格值选择
        $specificationValue.click(function () {
            var $this = $(this);
            if ($this.hasClass("locked")) {
                return false;
            }
            $this.toggleClass("selected").parent().siblings().children("a").removeClass("selected");
            var selectedIds = new Array();
            $specificationValue.filter(".selected").each(function (i) {
                selectedIds[i] = $(this).attr("val");
            });
            var locked = true;
            $.each(productMap, function (i, product) {
                if (product.specificationValues.length == selectedIds.length && contains(product.specificationValues, selectedIds)) {
                    if (product.path != null) {
                        location.href = "${base}" + product.path;
                        locked = false;
                    }
                    return false;
                }
            });
            if (locked) {
                lockSpecificationValue();
            }
            $specificationTitle.hide();
            return false;
        });

        // 锁定规格值
        function lockSpecificationValue() {
            var selectedIds = new Array();
            $specificationValue.filter(".selected").each(function (i) {
                selectedIds[i] = $(this).attr("val");
            });
            $specification.each(function () {
                var $this = $(this);
                var selectedId = $this.find("a.selected").attr("val");
                var otherIds = $.grep(selectedIds, function (n, i) {
                    return n != selectedId;
                });
                $this.find("a").each(function () {
                    var $this = $(this);
                    otherIds.push($this.attr("val"));
                    var locked = true;
                    $.each(productMap, function (i, product) {
                        if (contains(product.specificationValues, otherIds)) {
                            locked = false;
                            return false;
                        }
                    });
                    if (locked) {
                        $this.addClass("locked");
                    } else {
                        $this.removeClass("locked");
                    }
                    otherIds.pop();
                });
            });
        }

        // 判断是否包含
        function contains(array, values) {
            var contains = true;
            for (i in values) {
                if ($.inArray(values[i], array) < 0) {
                    contains = false;
                    break;
                }
            }
            return contains;
        }

        // 到货通知
        $addProductNotify.click(function () {
        [#if product.specifications?has_content]
            var specificationValueIds = new Array();
            $specificationValue.filter(".selected").each(function (i) {
                specificationValueIds[i] = $(this).attr("val");
            });
            if (specificationValueIds.length != ${product.specificationValues?size}) {
                $specificationTitle.show();
                return false;
            }
        [/#if]
            if ($addProductNotify.val() == "${message("shop.product.addProductNotify")}") {
                $addProductNotify.val("${message("shop.product.productNotifySubmit")}");
                $productNotify.show();
                $productNotifyEmail.focus();
                if ($.trim($productNotifyEmail.val()) == "") {
                    $.ajax({
                        url: "${base}/product_notify/email",
                        type: "GET",
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            $productNotifyEmail.val(data.email);
                        }
                    });
                }
            } else {
                $productNotifyForm.submit();
            }
            return false;
        });

        // 到货通知表单验证
        $productNotifyForm.validate({
            rules: {
                email: {
                    required: true,
                    email: true
                }
            },
            submitHandler: function (form) {
                $.ajax({
                    url: "${base}/product_notify/save",
                    type: "POST",
                    data: {productId: ${product.id}, email: $productNotifyEmail.val()},
                    dataType: "json",
                    cache: false,
                    beforeSend: function () {
                        $addProductNotify.prop("disabled", true);
                    },
                    success: function (data) {
                        if (data.message.type == "success") {
                            $addProductNotify.val("${message("shop.product.addProductNotify")}");
                            $productNotify.hide();
                        }
                        $.message(data.message);
                    },
                    complete: function () {
                        $addProductNotify.prop("disabled", false);
                    }
                });
            }
        });

        // 购买数量
        $quantity.keypress(function (event) {
            var key = event.keyCode ? event.keyCode : event.which;
            if ((key >= 48 && key <= 57) || key == 8) {
                return true;
            } else {
                return false;
            }
        });

        // 增加购买数量
        $increase.click(function () {
            var quantity = $quantity.val();
            if (/^\d*[1-9]\d*$/.test(quantity)) {
                $quantity.val(parseInt(quantity) + 1);
            } else {
                $quantity.val(1);
            }
        });

        // 减少购买数量
        $decrease.click(function () {
            var quantity = $quantity.val();
            if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 1) {
                $quantity.val(parseInt(quantity) - 1);
            } else {
                $quantity.val(1);
            }
        });

        // 加入购物车
        $addCart.click(function () {
        [#if product.specifications?has_content]
            var specificationValueIds = new Array();
            $specificationValue.filter(".selected").each(function (i) {
                specificationValueIds[i] = $(this).attr("val");
            });
            if (specificationValueIds.length != ${product.specificationValues?size}) {
                $specificationTitle.show();
                return false;
            }
        [/#if]
            $.ajax({
                url: "${base}/cart/add",
                type: "POST",
                data: {id: ${product.id}, quantity: 1},
                dataType: "json",
                cache: false,
                success: function (message) {
                    swal({
                        position: 'center',
                        type: 'success',
                        title: message.content,
                        showConfirmButton: false,
                        timer: 3000,
                        toast: true
                    });
                }
            });
        });

        // 立即购买
        $buyNow.click(function () {
        [#if product.specifications?has_content]
            var specificationValueIds = new Array();
            $specificationValue.filter(".selected").each(function (i) {
                specificationValueIds[i] = $(this).attr("val");
            });
            if (specificationValueIds.length != ${product.specificationValues?size}) {
                $specificationTitle.show();
                return false;
            }
        [/#if]
            var quantity = $quantity.val();
            if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 0) {
                $.ajax({
                    url: "${base}/cart/add",
                    type: "POST",
                    data: {id: ${product.id}, quantity: quantity},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        window.open("${base}/cart/list.html");
                    }
                });
            } else {
                $.message("warn", "${message("shop.product.quantityPositive")}");
            }
        });

        // 添加商品收藏
        $addFavorite.click(function () {
            $.ajax({
                url: "${base}/member/favorite/add?id=${product.id}",
                type: "POST",
                dataType: "json",
                cache: false,
                success: function (message) {
                    $.message(message);
                }
            });
            return false;
        });

    [#if setting.isReviewEnabled && setting.reviewAuthority != "anyone"]
        // 发表商品评论
        $addReview.click(function () {
            if ($.checkLogin()) {
                return true;
            } else {
                $.redirectLogin("${base}/review/add/${product.id}.html", "${message("shop.product.addReviewNotAllowed")}");
                return false;
            }
        });
    [/#if]

    [#if setting.isConsultationEnabled && setting.consultationAuthority != "anyone"]
        // 发表商品咨询
        $addConsultation.click(function () {
            if ($.checkLogin()) {
                return true;
            } else {
                $.redirectLogin("${base}/consultation/add/${product.id}.html", "${message("shop.product.addConsultationNotAllowed")}");
                return false;
            }
        });
    [/#if]

        // 浏览记录
        var historyProduct = getCookie("historyProduct");
        var historyProductIds = historyProduct != null ? historyProduct.split(",") : new Array();
        for (var i = 0; i < historyProductIds.length; i++) {
            if (historyProductIds[i] == "${product.id}") {
                historyProductIds.splice(i, 1);
                break;
            }
        }
        historyProductIds.unshift("${product.id}");
        if (historyProductIds.length > 6) {
            historyProductIds.pop();
        }
        addCookie("historyProduct", historyProductIds.join(","), {path: "${base}/"});
        $.ajax({
            url: "${base}/product/history",
            type: "GET",
            data: {ids: historyProductIds},
            dataType: "json",
            traditional: true,
            cache: false,
            success: function (data) {
                $.each(data, function (index, product) {
                    var thumbnail = product.thumbnail != null ? product.thumbnail : "${setting.defaultThumbnailProductImage}";
                    $historyProduct.append('<li><img src="' + thumbnail + '" \/><a href="${base}' + product.path + '">' + product.name + '<\/a><span>' + currency(product.price, true) + '<\/span><\/li>');
                });
            }
        });

        // 清空浏览记录
        $clearHistoryProduct.click(function () {
            $historyProduct.empty();
            $(this).text("${message("shop.product.noHistoryProduct")}");
            removeCookie("historyProduct", {path: "${base}/"});
        });

        // 点击数
        $.ajax({
            url: "${base}/product/hits/${product.id}",
            type: "GET"
        });

        $('.carousel').carousel({
            interval: 2000
        })

    });
</script>
</body>
</html>