<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />

<link href="${privateAssetsUrl}/css/pages/pricing-tables.css" rel="stylesheet" type="text/css" />

<style>
.feats-list li {
    background: url("${privateAssetsUrl}/img/checkmark.png") no-repeat 10px 50%;
    padding-left: 30px;

}
</style>