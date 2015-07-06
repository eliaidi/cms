<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <%@include file="../inc/common.jsp" %>

  </head>
  
  <body>
    
    <script type="text/javascript">
    	
    $(function(){
    	
    	<%-- var p = {
    		a1:1,
    		a2:'aaa',
    		a3:{
    			aa1:2,
    			aa2:{
    				b1:333,
    				b2:true
    			}
    		}
    	};
    	
    	p = {
    			a3['b1']:'212121',
    			a3['b2']:3232
    	}
    	$.post('<%=path%>/test',p,function(data){
    		
    	},"JSON") --%>
    });
    </script>
    
    <form action="">
    	<input name="m['b1']">
    	<input name="m['b2']">
    	<input name="a">
    	<input type='submit'>
    </form>
  </body>
</html>
