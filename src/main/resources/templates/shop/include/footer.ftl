<div class="footer">
	<div class="container-fluid">
        <div class="navigation">
            [@navigation_list position = "bottom"]
                [#list navigations as navigation]
                    <a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>
                        [#if navigation_has_next]|[/#if]
                [/#list]
            [/@navigation_list]
        </div>
		<div class="copyright">
			<span>${message("shop.footer.copyright", setting.siteName)}</span>
		</div>
	</div>
</div>