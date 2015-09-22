<%@page import="com.wk.cms.model.FieldValue"%>
<%@page import="com.wk.cms.model.Document"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>

<%@include file="../inc/common.jsp"%>

</head>

<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<h1 class="text-center">${obj.title }</h1>
			</div>
		</div>
		<div class="row">
			<div class="col-md-3 text-right">
				<strong>作者：</strong>
			</div>
			<div class="col-md-3 text-left">${obj.author }</div>
			<div class="col-md-3 text-right">
				<strong>撰写时间：</strong>
			</div>
			<div class="col-md-3 text-left">${obj.writeTime }</div>
		</div>
		<div class="row">
			<div class="col-md-3 text-right">
				<strong>所属站点：</strong>
			</div>
			<div class="col-md-3 text-left">${obj.site.name }</div>
			<div class="col-md-3 text-right">
				<strong>所属栏目：</strong>
			</div>
			<div class="col-md-3 text-left">${obj.channel.name }</div>
		</div>
		<div class="row">
			<div class="col-md-3 text-right">
				<strong>创建人：</strong>
			</div>
			<div class="col-md-3 text-left">${obj.crUser.name }</div>
			<div class="col-md-3 text-right">
				<strong>创建时间：</strong>
			</div>
			<div class="col-md-3 text-left">${obj.crTime }</div>
		</div>
		<div class="row">
			<div class="col-md-3 text-right">
				<strong>摘要：</strong>
			</div>
			<div class="col-md-9 text-left">${obj.abst }</div>
		</div>
		<div class="row">
			<div class="col-md-3 text-right">
				<strong>正文：</strong>
			</div>
			<div class="col-md-9 text-left">${obj.content }</div>
		</div>
		<div class="row">
			<div class="col-md-3 text-right">
				<strong>附件：</strong>
			</div>
			<div class="col-md-9 text-left">
				<c:forEach items="${obj.appendixs }" var="app" >
					<c:choose>
						<c:when test="${app.type==1 }">
							<p><img alt="" title="${app.name }(名称：${app.file.fileName }，大小：${app.file.fileSize })" width="570"
								src="${pageContext.request.contextPath }/file/app/${app.file.id}"
								class="img-rounded"></p>
						</c:when>
						<c:otherwise>
							<p><a href="${pageContext.request.contextPath }/file/app/${app.file.id}">${app.Name }(名称：${app.file.fileName }，大小：${app.file.fileSize })</a></p>
						</c:otherwise>
					</c:choose>

				</c:forEach>
			</div>
		</div>
		<div class="row">
			<div class="col-md-3 text-right">
				<strong>扩展字段：</strong>
			</div>
			<div class="col-md-9 text-left">
				
				<%
					Document document = (Document)request.getAttribute("obj");
					
					List<FieldValue> fieldValues = document.getFieldValues();
					Map<String,Object> renderVal = new HashMap<String,Object>();
					if(!fieldValues.isEmpty()){
						
						for(FieldValue fv : fieldValues){
							
							if(!fv.getExtField().getField().isCustom()){
								renderVal.put(fv.getExtField().getName(), fv.getValue());
							}else{
								String k = fv.getExtField().getName()+"-"+fv.getGroup();
								Object v = renderVal.get(k);
								if(v==null){
									v = new HashMap<String,Object>();
								}
								((Map)v).put(fv.getField().getName(), fv.getValue());
								renderVal.put(k, v);
							}
						}
					}
					
					for(String fn : renderVal.keySet()){
						Object val = renderVal.get(fn);
						out.println("<p>");
						out.println("<span><b>"+fn+"：</b>"+val+"</span>::::");
						if(val==null) continue;
						if(Map.class.isAssignableFrom(val.getClass())){
							Map<String,Object> v = (Map<String,Object>)val;
							for(String n : v.keySet()){
								out.println("<span><b>"+n+"：</b>"+v.get(n)+"</span>");
							}
							
						}
						out.println("</p>");
					}
				%>
			</div>
		</div>
	</div>
</body>
</html>
