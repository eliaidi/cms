<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title><%=request.getParameter("label") %></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript"
	src="<%=path%>/static/cms/ckeditor/ckeditor.js"></script>
</head>

<body>

	<textarea name="editor1" id="editor1" rows="10" cols="80">
                This is my textarea to be replaced with CKEditor.
            </textarea>
	<script>

		
		
		window.onload = function(e){
			
			var label = '<%=request.getParameter("label")==null?"":new String(request.getParameter("label").getBytes("ISO-8859-1"),"UTF-8")%>';
			if(label){
				document.getElementById("editor1").value = '请输入'+label+'！';
			}
			CKEDITOR.replace('editor1',{
				height:'<%=request.getParameter("height")%>'
			});
		}
	</script>
</body>
</html>
