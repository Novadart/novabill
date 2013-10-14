<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="gwtUrl" value="/rpc/rpc.nocache.js" />

<script type="text/javascript" language="javascript" src="${gwtUrl}"></script>

<script type="text/javascript">
function onGWTLoaded(){
	GWT_UI.bootstrapDialog();
}
</script>