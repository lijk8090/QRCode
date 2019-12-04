<%@ page language="java" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="cn.com.infosec.qrcode.*"%>
<!DOCTYPE html>
<html>
<head>
<title>QRCode</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/css/jquery-ui.min.css">

<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.base64.js"></script>
</head>

<body>
	<h1>QRCode</h1>
	<div>
		<img id="codeID" src="code.do" width="256px" height="256px" />
	</div>
	<div>
		<img id="qrcodeID" width="256px" height="256px" />
	</div>

	<div>
		<span>模拟APP登陆</span>
		<div>
			<span>账号:</span> <input type="text" id="username" name="username"
				value="root" />
		</div>
		<div>
			<span>密码:</span> <input type="text" id="password" name="password"
				value="11111111" />
		</div>
		<div>
			<span>UUID:</span> <input type="text" id="uuid" name=uuid
				value="123456" />
		</div>
		<div>
			<input type="button" id="appLogin" name=appLogin value="APP登陆" />
		</div>
	</div>

	<div>
		<span>模拟WEB登陆</span>
		<div>
			<span>UUID:</span> <input type="text" id="webUUID" name=webUUID
				value="123456" />
		</div>
		<div>
			<input type="button" id="webLogin" name=webLogin value="WEB登陆" />
		</div>
	</div>

	<script type="text/javascript">
		function qrCodeRefresh() {
			$.ajaxSettings.async = false;
			$.getJSON("qrcode.do", function(data, status, xhr) {
				console.log(data.type);
				console.log(data.base64);
				$('#qrcodeID').attr('src', data.type + data.base64);
			});
		}

		$(document).ready(function() {
			return qrCodeRefresh();
		});

		$('#qrcodeID').click(function() {
			return qrCodeRefresh();
		});

		$('#codeID').click(function() {
			window.location.reload();
		});

		$('#appLogin').click(function() {
			var data = {};
			data['username'] = $('#username').val();
			data['password'] = $('#password').val();
			data['uuid'] = $('#uuid').val();

			$.ajaxSettings.processData = true;
			$.post("appLogin.do", data, function(data, status, xhr) {
				console.log(data.isLogin);
				if (data.isLogin == 'true') {
					alert("APP登陆成功!");
					window.location.href = 'main.do';
					return true;
				} else {
					alert("APP登陆失败!");
					return false;
				}
			});
		});

		var loop = 0;
		var count = 3;
		function webLogin() {
			var data = {};
			data['webUUID'] = $('#webUUID').val();

			loop += 1;
			$.ajax({
				type : 'POST',
				url : 'webLogin.do',
				data : data,
				async : true,
				processData : true,
				success : function(data, status, xhr) {
					console.log(data.isLogin);
					if (data.isLogin == 'true') {
						alert("WEB登陆成功!");
						window.location.href = 'main.do';
						return true;
					} else {
						if (loop < count) {
							setTimeout(function() {
								webLogin();
							}, 1000);
						} else {
							alert("WEB登陆超时!");
							return false;
						}
						alert("WEB登陆失败!");
						return false;
					}
				},
				error : function(xhr, status, error) {
					if (loop < count) {
						setTimeout(function() {
							webLogin();
						}, 1000);
					} else {
						alert("WEB登陆超时!");
						return false;
					}
					alert("WEB登陆异常!");
					return false;
				}
			});
		}

		$('#webLogin').click(function() {

			return webLogin();
		});
	</script>
</body>
</html>
