<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="author" content="progr1mmer"/>
    [@seo type = "productContent"]
        <title>[#if product.seoTitle??]${product.seoTitle}[#elseif seo.title??][@seo.title?interpret /][/#if][#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
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
    <link href="${base}/shop/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/sweetalert2.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/product-single.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        function getQueryVariable(variable) {
            var query = window.location.search.substring(1);
            var vars = query.split("&");
            for (var i = 0; i < vars.length; i++) {
                var pair = vars[i].split("=");
                if (pair[0] == variable) {
                    return pair[1];
                }
            }
            return(false);
        }
        var b = getQueryVariable("ab");
    </script>
</head>
<body>
    [#assign productCategory = product.productCategory /]
    <div class="container-fluid">
        <div class="row">
            <div class="col-xs-12 col-sm-6 col-sm-offset-3">
                <div class="image" style="display: none">
                    <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                        [#if product.productImages?has_content]
                            <!-- Indicators -->
                            <ol class="carousel-indicators">
                                [#list product.productImages as productImage]
                                    <li data-target="#carousel-example-generic" data-slide-to="${productImage_index}" [#if productImage_index == 0] class="active" [/#if] side="[#if productImage.isB]b[#else]a[/#if]"></li>
                                [/#list]
                            </ol>
                            <!-- Wrapper for slides -->
                            <div class="carousel-inner" role="listbox">
                                [#list product.productImages as productImage]
                                    <div class="item [#if productImage_index == 0]active[/#if]" side="[#if productImage.isB]b[#else]a[/#if]">
                                        <img src="${productImage.large}" title="${productImage.title}" alt="${productImage.title}" width="100%"/>
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
                                    <img src="${setting.defaultLargeProductImage}" title="${setting.siteName}" alt="${setting.siteName}" width="100%" />
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

                <div class="basic">
                    <div class="price">
                        <span class="salePrice">
                            <small>${setting.currencySign}</small><B>${currency(product.price)}</B>
                        </span>
                        [#if setting.isShowMarketPrice]
                            <span class="marketPrice">
                                <small>${setting.currencySign}</small><del>${currency(product.marketPrice)}</del>
                            </span>
                        [/#if]
                    </div>
                    <div class="promotion">
                        [#if product.validPromotions?has_content]
                            [#list product.validPromotions as promotion]
                                <a href="#" [#if promotion.beginDate?? || promotion.endDate??]
                                   title="${promotion.beginDate} ~ ${promotion.endDate}" [/#if]>${promotion.name}</a>
                            [/#list]
                        [/#if]
                    </div>

                    <div class="name" style="display: none">
                        [#if product.brand?has_content]
                            <span side="a">${product.brand.name} | ${product.name}</span>
                            <span side="b">${product.brand.name} | ${product.nameB}</span>
                        [#else]
                            <span side="a">${product.name}</span>
                            <span side="b">${product.name}</span>
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

                    [#if !product.isGift]
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
                                                    </a>
                                                </dd>
                                            [/#if]
                                        [/#list]
                                    </dl>
                                [/#list]
                            </div>
                        [/#if]
                    [/#if]
                </div>

                [#if product.parameterValue?has_content]
                    <div id="parameter" name="parameter" class="parameter">
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
                [/#if]

                [#if product.introduction?has_content || product.introductionB?has_content]
                    <div class="details">
                        <div class="row">
                            <div class="col-xs-8 col-xs-offset-2">
                                <ul class="nav nav-tabs">
                                    <li role="presentation" class="title active"><a href="#introduction" aria-controls="introduction" role="tab" data-toggle="tab">${message("shop.product.introduction")}</a></li>
                                    <li role="presentation" class="title disabled"><a href="#consultation" aria-controls="consultation" role="tab" data-toggle="tab">${message("shop.product.consultation")}</a></li>
                                </ul>
                            </div>
                            <div class="col-xs-12">
                                <div class="tab-content">
                                    <div id="introduction" name="introduction" class="tab-pane fade in active" role="tabpanel" style="display: none">
                                        <div side="a">
                                            ${product.introduction}
                                        </div>
                                        <div side="b">
                                            ${product.introductionB}
                                        </div>
                                    </div>
                                    <div id="consultation" name="consultation" class="tab-pane fade in" role="tabpanel"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                [/#if]
            </div>
        </div>
    </div>

    <nav class="navbar navbar-default navbar-fixed-bottom">
        <button type="button" id="buyNow" class="btn btn-danger">${message("shop.product.buyNow")}</button>
    </nav>
    [#include "/shop/include/footer.ftl" /]
    <script type="text/javascript" src="${base}/shop/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/sweetalert2.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/shop/js/common.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $carouselIndicators = $('.carousel-indicators');
            if (b && b === 'b') {
                $('[side="a"]').hide();
                if ($carouselIndicators.find('li').length > $carouselIndicators.find('li[side="a"]').length) {
                    $('.image').toggle();
                }
            } else {
                $('[side="b"]').hide();
                if ($carouselIndicators.find('li').length > $carouselIndicators.find('li[side="b"]').length) {
                    $('.image').toggle();
                }
            }
            $('.name').toggle();
            $('#introduction').toggle();

            var $specification = $("#specification dl");
            var $specificationTitle = $("#specification div");
            var $specificationValue = $("#specification a");
            var $buyNow = $("#buyNow");
            var $a_consultation = $('a[href="#consultation"]');

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

            // 规格值选择
            $specificationValue.click(function () {
                var $this = $(this);
                if ($this.hasClass("locked")) {
                    return false;
                }
                $this.toggleClass("selected").parent().siblings().children("a").removeClass("selected");
                var selectedIds = [];
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
                var selectedIds = [];
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

            // 立即购买
            $buyNow.click(function () {
                [#if product.specifications?has_content]
                    var specificationValueIds = [];
                    $specificationValue.filter(".selected").each(function (i) {
                        specificationValueIds[i] = $(this).attr("val");
                    });
                    if (specificationValueIds.length != ${product.specificationValues?size}) {
                        $.message('error', '${message("shop.product.specificationTitle")}');
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
                        if (message.type == "success") {
                            location.href = "${base}/order/info.html";
                        } else {
                            $.message(message);
                        }
                    }
                });
            });

            // 点击数
            $.ajax({
                url: "${base}/product/hits/${product.id}",
                type: "GET"
            });

            $a_consultation.on('show.bs.tab', function(e) {
                if ($a_consultation.parent().hasClass('disabled')) {
                    e.preventDefault();
                }
            });

            $('.carousel').carousel({
                interval: 2000
            });
        });
    </script>
</body>
</html>