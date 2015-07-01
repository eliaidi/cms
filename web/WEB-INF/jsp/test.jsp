<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base>

<title>Main</title>
<script type="text/javascript">
var RootPath = '<%=path%>';
</script>
<link rel="stylesheet" type="text/css"
	href="<%=path %>/ext/packages/ext-theme-neptune/build/resources/ext-theme-neptune-all.css">
<script type="text/javascript" src="<%=path %>/ext/build/ext-all.js"></script>
<script type="text/javascript"
	src="<%=path %>/ext/packages/ext-theme-neptune/build/ext-theme-neptune.js"></script>

<script type="text/javascript">
Ext.application({
    name   : 'MyApp',

    launch : function() {

       Ext.create('Ext.Panel', {
            renderTo     : Ext.getBody(),
       //     width        : 200,
       //     height       : 150,
            bodyPadding  : 5,
            title        : 'Hello World',
            items:[{
            	xtype:'datefield',
				format:'Y-m-d',
				fieldLabel: '撰写时间',
		        name: 'writeTime',
		        allowBlank: true
            },{
            	xtype: 'htmleditor',
				fieldLabel: '内容',
		        name: 'content',
		        height:350,
		        allowBlank: false
            }]
        });

    }
});
</script>
</head>

<body>


</body>
</html>
