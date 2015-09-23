<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base>

<title>Main</title>
<script type="text/javascript">
var RootPath = '<%=path%>',BasePath = '<%=basePath%>',ScreenWidth = window.screen.availWidth,ScreenHeight = window.screen.availHeight;
</script>
<script type="text/javascript" src="<%=path %>/static/cms/urls.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/My97DatePicker/WdatePicker.js"></script>

<script id="microloader" type="text/javascript" src="<%=path %>/static/cms/bootstrap.js"></script>

</head>

<body>
	

</body>
</html>
