<div class="footer">
	<div class="container-fluid">
		<div class="span26">
			<div align="center">
					[@navigation_list position = "middle"]
						[#list navigations as navigation]
								<a href="${navigation.url}"[#if navigation.isBlankTarget] target="_blank"[/#if]>${navigation.name}</a>
									[#if navigation_has_next]|[/#if]
						[/#list]
					[/@navigation_list]
			</div>
		</div>
		<div style="text-align: center">
			<div class="copyright">${message("shop.footer.copyright", setting.siteName)}</div>
		</div>
	</div>
</div>