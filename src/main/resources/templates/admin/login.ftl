
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.login.title")}[#if setting.isShowPowered] - ${setting.powered}[/#if]</title>
<meta name="author" content="progr1mmer" />
<link href="${base}/admin/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/admin/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/admin/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/admin/js/prng4.js"></script>
<script type="text/javascript" src="${base}/admin/js/rng.js"></script>
<script type="text/javascript" src="${base}/admin/js/rsa.js"></script>
<script type="text/javascript" src="${base}/admin/js/base64.js"></script>
<script type="text/javascript" src="${base}/admin/js/common.js"></script>

<script type="text/javascript">
[@shiro.authenticated]
    location.href = '${base}/admin/common/main.html';
[/@shiro.authenticated]
$().ready( function() {

    [#if shiroLoginFailure??]
        $.message('error', '${message(shiroLoginFailure, setting.accountLockCount)}');
    [/#if]

    var $loginForm = $("#loginForm");
    var $enPassword = $("#enPassword");
    var $username = $("#username");
    var $password = $("#password");
    var $captcha = $("#captcha");
    var $captchaImage = $("#captchaImage");
    var $isRememberUsername = $("#isRememberUsername");

    // 记住用户名
    if(getCookie("adminUsername") != null) {
        $isRememberUsername.prop("checked", true);
        $username.val(getCookie("adminUsername"));
        $password.focus();
    } else {
        $isRememberUsername.prop("checked", false);
        $username.focus();
    }

    // 更换验证码
    $captchaImage.click( function() {
        $captchaImage.attr("src", "${base}/admin/common/captcha.html?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
    });

    $loginForm.submit( function() {
        if ($username.val() == "") {
            $.message('warn', '${message("admin.login.usernameRequired")}');
            return false;
        }
        if ($password.val() == "") {
            $.message('warn', '${message("admin.login.passwordRequired")}');
            return false;
        }
        if ($captcha.val() == "") {
            $.message('warn', '${message("admin.login.captchaRequired")}');
            return false;
        }

        if ($isRememberUsername.prop("checked")) {
            addCookie("adminUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
        } else {
            removeCookie("adminUsername");
        }
        $.ajax({
            url: "${base}/common/public_key",
            type: "GET",
            dataType: "json",
            cache: false,
            async: false,
            success: function(data) {
                var rsaKey = new RSAKey();
                rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                var enPassword = hex2b64(rsaKey.encrypt($password.val()));
                $enPassword.val(enPassword);
            }
        });
    });

});
</script>
</head>
<body>
<div class="login">
    <form id="loginForm" action="login.html" method="post">
        <input type="hidden" id="enPassword" name="enPassword" />
        [#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
            <input type="hidden" name="captchaId" value="${captchaId}" />
        [/#if]
        <table>
            <tr>
                <th width="80px">
                    &nbsp;
                </th>
                <th>
                    ${message("admin.login.username")}:
                </th>
                <td>
                    <input type="text" id="username" name="username" class="text" maxlength="20" />
                </td>
            </tr>
            <tr>
                <th>
                    &nbsp;
                </th>
                <th>
                    ${message("admin.login.password")}:
                </th>
                <td>
                    <input type="password" id="password" class="text" maxlength="20" autocomplete="off" />
                </td>
            </tr>
            [#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
                <tr>
                    <th>
                        &nbsp;
                    </th>
                    <th>
                        ${message("admin.captcha.name")}:
                    </th>
                    <td>
                        <input type="text" id="captcha" name="captcha" class="text captcha" maxlength="4" autocomplete="off" /><img id="captchaImage" class="captchaImage" src="${base}/admin/common/captcha.html?captchaId=${captchaId}" title="${message("admin.captcha.imageTitle")}" />
                    </td>
                </tr>
            [/#if]
            <tr>
                <th>
                    &nbsp;
                </th>
                <th>
                    &nbsp;
                </th>
                <td>
                    <label>
                        ${message("admin.login.rememberUsername")}:
                        <input type="checkbox" id="isRememberUsername" value="true" />
                    </label>
                </td>
            </tr>
            <tr>
                <th>
                    &nbsp;
                </th>
                <th>
                    &nbsp;
                </th>
                <td>
                    <input type="button" class="homeButton" value="" onclick="location.href='${base}/'" /><input type="submit" class="loginButton" value="${message("admin.login.login")}" />
                </td>
            </tr>
        </table>
    </form>
    [#if setting.isShowCopyright]
        <div class="powered">${setting.copyright}</div>
    [/#if]
</div>
</body>
</html>