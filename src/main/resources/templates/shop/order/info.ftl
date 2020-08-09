<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="author" content="progr1mmer" />
    <title>${message("shop.order.info")}[#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
    <link href="${base}/shop/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/sweetalert2.min.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/shop/css/order.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="container-fluid">
		<div class="row">
            <div class="col-xs-12 col-sm-6 col-sm-offset-3">
                <form id="orderForm" action="create.html" method="post">
                    <input type="hidden" name="cartToken" value="${cartToken}" />

                    <div class="receiver">
                        <div class="form-group">
                            <label for="consignee">${message("shop.order.consignee")}&nbsp;<span class="requiredField">*</span></label>
                            <input type="text" class="form-control" id="consignee" name="consignee" maxlength="200" required />
                        </div>
                        <div class="form-group">
                            <label for="areaId">${message("shop.order.area")}&nbsp;<span class="requiredField">*</span></label>
                            <input type="hidden" class="form-control" id="areaId" name="areaId" />
                        </div>
                        <div class="form-group">
                            <label for="address">${message("shop.order.address")}&nbsp;<span class="requiredField">*</span></label>
                            <input type="text" class="form-control" id="address" name="address" maxlength="200" required />
                        </div>
                        <div class="form-group">
                            <label for="zipCode">${message("shop.order.zipCode")}&nbsp;<span class="requiredField">*</span></label>
                            <input type="number" class="form-control" id="zipCode" name="zipCode" maxlength="200" required />
                        </div>
                        <div class="form-group">
                            <label for="phone">${message("shop.order.phone")}&nbsp;<span class="requiredField">*</span></label>
                            <input type="number" class="form-control" id="phone" name="phone" maxlength="200" required />
                        </div>
                    </div>

                    <div class="settlement">
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

                        <div id="paymentMethod" class="form-group">
                            <label style="margin-bottom: unset">${message("shop.order.paymentMethod")}</label>
                            [#list paymentMethods as paymentMethod]
                                <div class="radio">
                                    <label for="paymentMethod_${paymentMethod.id}">
                                        <input type="radio" id="paymentMethod_${paymentMethod.id}" name="paymentMethodId" value="${paymentMethod.id}" />
                                        <span>
                                            [#if paymentMethod.icon??]
                                                <em style="border-right: none; background: url(${paymentMethod.icon}) center no-repeat;">&nbsp;</em>
                                            [/#if]
                                            <span>${paymentMethod.name}</span>
                                        </span>
                                        <span>${abbreviate(paymentMethod.description, 80, "...")}</span>
                                    </label>
                                </div>
                            [/#list]
                        </div>

                        <div id="shippingMethod" class="form-group">
                            <label style="margin-bottom: unset">${message("shop.order.shippingMethod")}</label>
                            [#list shippingMethods as shippingMethod]
                                <div class="radio">
                                    <label for="shippingMethod_${shippingMethod.id}">
                                        <input type="radio" id="shippingMethod_${shippingMethod.id}" name="shippingMethodId" value="${shippingMethod.id}" />
                                        <span>
                                            [#if shippingMethod.icon??]
                                                <em style="border-right: none; background: url(${shippingMethod.icon}) center no-repeat;">&nbsp;</em>
                                            [/#if]
                                            <span>${shippingMethod.name}</span>
                                        </span>
                                        <span>${abbreviate(shippingMethod.description, 80, "...")}</span>
                                    </label>
                                </div>
                            [/#list]
                        </div>

                        [#if setting.isInvoiceEnabled]
                            <div class="form-group">
                                <label style="margin-bottom: unset">${message("shop.order.invoiceInfo")}</label>
                                <div class="checkbox">
                                    ${message("shop.order.isInvoice")}:
                                    <label for="isInvoice">
                                        <input type="checkbox" id="isInvoice" name="isInvoice" />
                                        [#if setting.isTaxPriceEnabled](${message("shop.order.invoiceTax")}: ${setting.taxRate * 100}%)[/#if]
                                    </label>
                                </div>
                                <div id="invoiceTitleTr" class="form-group" style="display: none;">
                                    ${message("shop.order.invoiceTitle")}: <input type="text" id="invoiceTitle" name="invoiceTitle" [#if defaultReceiver??] value="${defaultReceiver.consignee}"[/#if] maxlength="200" />
                                </div>
                            </div>
                        [/#if]

                        <div class="form-group">
                            <label for="memo">${message("shop.order.memo")}</label>
                            <input type="text" class="form-control" id="memo" name="memo" maxlength="200" />
                        </div>

                        <div class="form-group coupon">
                            <label for="couponCode">${message("shop.order.coupon")}</label>
                            <input type="hidden" id="code" name="code" maxlength="200" />
                            <input type="text" class="form-control" id="couponCode" name="couponCode" maxlength="200" />
                            <span id="couponName">&nbsp;</span>
                            <button type="button" id="couponButton" style="display: none">${message("shop.order.codeConfirm")}</button>
                        </div>

                        <div class="statistic">
                            <ul>
                                <li>
                                    <span>
                                        ${message("shop.order.point")}: <em>${order.point}</em>
                                    </span>
                                </li>
                                <li>
                                    <span>
                                        ${message("shop.order.freight")}: <em id="freight">${currency(order.freight, 'true')}</em>
                                    </span>
                                    [#if setting.isInvoiceEnabled && setting.isTaxPriceEnabled]
                                        <span style="display: none">
                                            &nbsp;${message("shop.order.tax")}: <em id="tax">${currency(order.tax, 'true')}</em>
                                        </span>
                                    [/#if]
                                </li>
                                <li>
                                    <span[#if order.promotionDiscount == 0] class="hidden"[/#if]>
                                        ${message("shop.order.promotionDiscount")}: <em id="promotionDiscount">${currency(order.promotionDiscount, 'true')}</em>
                                    </span>
                                    <span[#if order.couponDiscount == 0] class="hidden"[/#if]>
                                        ${message("shop.order.couponDiscount")}: <em id="couponDiscount">${currency(order.couponDiscount, 'true')}</em>
                                    </span>
                                </li>
                                <li>
                                    <span>
                                        ${message("shop.order.amountPayable")}: <strong id="amountPayable">${currency(order.amountPayable, 'true')}</strong>
                                    </span>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <nav class="navbar navbar-default navbar-fixed-bottom">
                        <button type="button" id="submit" class="btn btn-danger">${message("shop.order.submit")}</button>
                    </nav>
                </form>
            </div>
		</div>
	</div>
	[#include "/shop/include/footer.ftl" /]
    <script type="text/javascript" src="${base}/shop/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/sweetalert2.min.js"></script>
    <script type="text/javascript" src="${base}/shop/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/shop/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/shop/js/common.js"></script>
    <script type="text/javascript">
        $().ready(function() {

            var $areaId = $("#areaId");
            var $orderForm = $("#orderForm");
            var $paymentMethodId = $("#paymentMethod :radio");
            var $shippingMethodId = $("#shippingMethod :radio");
            var $isInvoice = $("#isInvoice");
            var $invoiceTitleTr = $("#invoiceTitleTr");
            var $invoiceTitle = $("#invoiceTitle");
            var $code = $("#code");
            var $couponCode = $("#couponCode");
            var $couponName = $("#couponName");
            var $couponButton = $("#couponButton");
            var $freight = $("#freight");
            var $promotionDiscount = $("#promotionDiscount");
            var $couponDiscount = $("#couponDiscount");
            var $tax = $("#tax");
            var $amountPayable = $("#amountPayable");
            var $submit = $("#submit");
            var shippingMethodIds = {};

            [@compress single_line = true]
                [#list paymentMethods as paymentMethod]
                    shippingMethodIds["${paymentMethod.id}"] = [
                        [#list paymentMethod.shippingMethods as shippingMethod]
                            "${shippingMethod.id}"[#if shippingMethod_has_next],[/#if]
                        [/#list]
                    ];
                [/#list]
            [/@compress]


            // 地区选择
            $areaId.lSelect({
                url: "${base}/common/area"
            });

            // 计算
            function calculate() {
                $.ajax({
                    url: "calculate",
                    type: "POST",
                    data: $orderForm.serialize(),
                    dataType: "json",
                    cache: false,
                    success: function(data) {
                        if (data.message.type == "success") {
                            $freight.text(currency(data.freight, true));
                            if (data.promotionDiscount > 0) {
                                $promotionDiscount.text(currency(data.promotionDiscount, true));
                                $promotionDiscount.parent().show();
                            } else {
                                $promotionDiscount.parent().hide();
                            }
                            if (data.couponDiscount > 0) {
                                $couponDiscount.text(currency(data.couponDiscount, true));
                                $couponDiscount.parent().show();
                            } else {
                                $couponDiscount.parent().hide();
                            }
                            if (data.tax > 0) {
                                $tax.text(currency(data.tax, true));
                                $tax.parent().show();
                            } else {
                                $tax.parent().hide();
                            }
                            $amountPayable.text(currency(data.amountPayable, true));
                        } else {
                            $.message(data.message);
                            setTimeout(function() {
                                location.reload(true);
                            }, 3000);
                        }
                    }
                });
            }

            // 支付方式
            $paymentMethodId.click(function() {
                var $this = $(this);
                if ($this.prop("disabled")) {
                    return false;
                }
                var paymentMethodId = $this.val();
                $shippingMethodId.each(function() {
                    var $this = $(this);
                    if ($.inArray($this.val(), shippingMethodIds[paymentMethodId]) >= 0) {
                        $this.prop("disabled", false);
                    } else {
                        $this.prop("disabled", true).prop("checked", false);
                    }
                });
                calculate();
            });

            // 配送方式
            $shippingMethodId.click(function() {
                var $this = $(this);
                if ($this.prop("disabled")) {
                    return false;
                }
                calculate();
            });

            // 开据发票
            $isInvoice.click(function() {
                $invoiceTitleTr.toggle();
                calculate();
            });

            $invoiceTitleTr.change(function () {
                calculate();
            });

            // 优惠券
            $couponButton.click(function() {
                if ($code.val() == "") {
                    if ($.trim($couponCode.val()) == "") {
                        return false;
                    }
                    $.ajax({
                        url: "coupon_info",
                        type: "POST",
                        data: {code : $couponCode.val()},
                        dataType: "json",
                        cache: false,
                        beforeSend: function() {
                            $couponButton.prop("disabled", true);
                        },
                        success: function(data) {
                            if (data.message.type == "success") {
                                $code.val($couponCode.val());
                                $couponCode.hide();
                                $couponName.text(data.couponName).show();
                                $couponButton.text("${message("shop.order.codeCancel")}");
                                calculate();
                            } else {
                                $.message(data.message);
                            }
                        },
                        complete: function() {
                            $couponButton.prop("disabled", false);
                        }
                    });
                } else {
                    $code.val("");
                    $couponCode.show();
                    $couponName.hide();
                    $couponButton.text("${message("shop.order.codeConfirm")}");
                    calculate();
                }
            });

            // 订单提交
            $submit.click(function() {
                if (!$('#consignee').val()) {
                    $.message('warning', '${message("shop.order.consigneeRequired")}');
                    return false;
                }
                if (!$('[name="areaId_select"]:last').val()) {
                    $.message('warning', '${message("shop.order.areaIdRequired")}');
                    return false;
                }
                if (!$('#address').val()) {
                    $.message('warning', '${message("shop.order.addressRequired")}');
                    return false;
                }
                if (!$('#zipCode').val()) {
                    $.message('warning', '${message("shop.order.zipCodeRequired")}');
                    return false;
                }
                if (!$('#phone').val()) {
                    $.message('warning', '${message("shop.order.phoneRequired")}');
                    return false;
                }
                var $checkedPaymentMethodId = $paymentMethodId.filter(":checked");
                var $checkedShippingMethodId = $shippingMethodId.filter(":checked");
                if ($checkedPaymentMethodId.size() == 0) {
                    $.message('warning', '${message("shop.order.paymentMethodRequired")}');
                    return false;
                }
                if ($checkedShippingMethodId.size() == 0) {
                    $.message("warning", "${message("shop.order.shippingMethodRequired")}");
                    return false;
                }
                [#if setting.isInvoiceEnabled]
                    if ($isInvoice.prop("checked") && $.trim($invoiceTitle.val()) == "") {
                        $.message("warning", "${message("shop.order.invoiceTileRequired")}");
                        return false;
                    }
                [/#if]
                $.ajax({
                    url: "create",
                    type: "POST",
                    data: $orderForm.serialize(),
                    dataType: "json",
                    cache: false,
                    beforeSend: function() {
                        $submit.prop("disabled", true);
                    },
                    success: function(message) {
                        if (message.type == "success") {
                            location.href = "result.html?sn=" + message.content;
                        } else {
                            $.message(message);
                            setTimeout(function() {
                                location.reload(true);
                            }, 3000);
                        }
                    },
                    complete: function() {
                        $submit.prop("disabled", false);
                    }
                });
            });

            $paymentMethodId[0].click();
            $shippingMethodId.not(':disabled')[0].click();
        });
    </script>
</body>
</html>