<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="author" content="progr1mmer" />
    <title>${message("shop.order.notify")}[#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
    <link href="${base}/shop/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${base}/shop/css/order.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript">
        history.pushState(null, null, '#');
        window.addEventListener("popstate", function(e) {
            location.href = "${base}${order.orderItems[0].product.path}";
        }, false);
    </script>
</head>
<body>
	[#assign current = "orderList" /]
	<div class="container-fluid">
        <div class="jumbotron">
            <h1>${message("shop.message.success")}</h1>
            <p>${message("shop.order.waitingProcess")}</p>
            <p><a class="btn btn-primary btn-lg" href="${base}${order.orderItems[0].product.path}" role="button">${message("shop.order.buyAgain")}</a></p>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">${message("shop.order.receiver")}</div>
            <div class="panel-body">
                <table class="table table-condensed">
                    <tr>
                        <th width="20%">
                            ${message("Order.consignee")}:
                        </th>
                        <td>
                            ${order.consignee}
                        </td>
                    </tr>
                    <tr>
                        <th width="20%">
                            ${message("Order.zipCode")}:
                        </th>
                        <td>
                            ${order.zipCode}
                        </td>
                    </tr>
                    <tr>
                        <th width="20%">
                            ${message("Order.address")}:
                        </th>
                        <td>
                            ${order.areaName}${order.address}
                        </td>
                    </tr>
                    <tr>
                        <th width="20%">
                            ${message("Order.phone")}:
                        </th>
                        <td>
                            ${order.phone}
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">${message("shop.order.info")}</div>
            <div class="panel-body">
                [#list order.orderItems as orderItem]
                    <div class="product">
                        <div class="image">
                            <img src="[#if orderItem.product.thumbnail??]${orderItem.product.thumbnail}[#else]${setting.defaultThumbnailProductImage}[/#if]" alt="${orderItem.product.name}" />
                        </div>
                        <div class="name">
                            <span style="font-size: 12px;">${abbreviate(orderItem.product.fullName, 80, "...")}</span>
                            [#if orderItem.isGift]
                                <span class="red">[${message("shop.order.gift")}]</span>
                            [/#if]
                        </div>
                        <div class="subTotal">
                            <span>${currency(orderItem.subtotal, 'true')}</span>
                            <span style="color: #999999">x${orderItem.quantity}</span>
                        </div>
                    </div>
                [/#list]
                <table class="table table-condensed">
                    <tr>
                        <th>
                            ${message("shop.common.createDate")}:
                        </th>
                        <td>
                            ${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${message("Order.paymentMethod")}:
                        </th>
                        <td>
                            ${order.paymentMethodName}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${message("Order.shippingMethod")}:
                        </th>
                        <td>
                            ${order.shippingMethodName}
                        </td>
                    </tr>
                    [#if order.point > 0]
                        <tr>
                            <th>
                                ${message("Order.point")}:
                            </th>
                            <td>
                                ${order.point}
                            </td>
                        </tr>
                    [/#if]
                    <tr>
                        <th>
                            ${message("Order.price")}:
                        </th>
                        <td>
                            ${currency(order.price, 'true')}
                        </td>
                    </tr>
                    [#if order.fee > 0]
                        <tr>
                            <th>
                                ${message("Order.fee")}:
                            </th>
                            <td>
                                ${currency(order.fee, 'true')}
                            </td>
                        </tr>
                    [/#if]
                    [#if order.freight > 0]
                        <tr>
                            <th>
                                ${message("Order.freight")}:
                            </th>
                            <td>
                                ${currency(order.freight, 'true')}
                            </td>
                        </tr>
                    [/#if]
                    [#if order.promotionDiscount > 0]
                        <tr>
                            <th>
                                ${message("Order.promotionDiscount")}:
                            </th>
                            <td>
                                ${currency(order.promotionDiscount, 'true')}
                            </td>
                        </tr>
                    [/#if]
                    [#if order.couponDiscount > 0]
                        <tr>
                            <th>
                                ${message("Order.couponDiscount")}:
                            </th>
                            <td>
                                ${currency(order.couponDiscount, 'true')}
                            </td>
                        </tr>
                    [/#if]
                    <tr>
                        <th>
                            ${message("Order.amount")}:
                        </th>
                        <td>
                            ${currency(order.amount, 'true')}
                        </td>
                    </tr>
                    [#if order.couponCode??]
                        <tr>
                            <th>
                                ${message("shop.member.order.coupon")}:
                            </th>
                            <td>
                                ${order.couponCode.coupon.name}
                            </td>
                        </tr>
                    [/#if]
                    [#if order.promotion??]
                        <tr>
                            <th>
                                ${message("Order.promotion")}:
                            </th>
                            <td>
                                ${order.promotion}
                            </td>
                        </tr>
                    [/#if]
                    <tr>
                        <th>
                            ${message("Order.memo")}:
                        </th>
                        <td>
                            ${order.memo}
                        </td>
                    </tr>
                    [#if order.isInvoice]
                        <tr>
                            <th>
                                ${message("Order.invoiceTitle")}:
                            </th>
                            <td>
                                ${order.invoiceTitle}
                            </td>
                        </tr>
                        <tr>
                            <th>
                                ${message("Order.tax")}:
                            </th>
                            <td>
                                ${currency(order.tax, 'true')}
                            </td>
                        </tr>
                    [/#if]
                </table>
            </div>
        </div>
	</div>
    [#include "/shop/include/footer.ftl" /]
    <script type="text/javascript" src="${base}/shop/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/common.js"></script>
</body>
</html>