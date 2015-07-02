<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>

<%@include file="inc/common.jsp"%>

<style type="text/css">
.modal {
	position: relative;
	top: auto;
	right: auto;
	bottom: auto;
	left: auto;
	z-index: 1;
	display: block;
}
</style>
</head>

<body>
	<div class="container">
		<div class="modal ">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<!-- <button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button> -->
						<h4 class="modal-title">发生错误</h4>
					</div>
					<div class="modal-body">
						<p>${error.message }</p>
						<hr>
						<p>
						
						<c:forEach items="${error.stackTrace }" var="st">
							${st }
						</c:forEach>
						</p>
					</div>
					<!-- <div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="button" class="btn btn-primary">Save
							changes</button>
					</div> -->
					
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->

	</div>


</body>
</html>
