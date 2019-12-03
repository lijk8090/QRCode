<%@ page language="java" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>QRCode</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

	<link rel="stylesheet" href="css/jquery-ui.min.css">

	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.base64.js"></script>
</head>

<body>
    <h1>QRCode</h1>
	<div>
		<img id="codeID" src="code.do" width="256px" height="256px"/>
	</div>
	<div>
		<img id="qrcodeID" width="256px" height="256px"/>
	</div>

    <script type="text/javascript">

		function qrCodeRefresh() {
			$.ajaxSettings.async = false;
			$.getJSON("qrcode.do", function(data, status, xhr) {
				console.log(data.qrcode);
				$('#qrcodeID').attr('src', data.qrcode);
			});
		}

		$(document).ready(function(){
			return qrCodeRefresh();
		});

		$('#qrcodeID').click(function() {
			return qrCodeRefresh();
		});

		$('#codeID').click(function() {
			window.location.reload();
		});

    </script>
</body>
</html>
