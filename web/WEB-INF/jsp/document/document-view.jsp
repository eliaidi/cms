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
							<p><img alt="" title="${app.fileName }(大小：${app.fileSize })" width="570"
								src="${pageContext.request.contextPath }/file/app/${app.id}"
								class="img-rounded"></p>
						</c:when>
						<c:otherwise>
							<p><a href="${pageContext.request.contextPath }/file/app/${app.id}">${app.fileName }(大小：${app.fileSize })</a></p>
						</c:otherwise>
					</c:choose>

				</c:forEach>
			</div>
		</div>
		<!-- <div class="row">
			<div class="col-md-4">.col-md-4</div>
			<div class="col-md-4">.col-md-4</div>
			<div class="col-md-4">.col-md-4</div>
		</div>
		<div class="row">
			<div class="col-md-6">.col-md-6</div>
			<div class="col-md-6">.col-md-6</div>
		</div> -->
	</div>
</body>
</html>
