<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />

<link href="${privateAssetsUrl}/css/pages/pricing-tables.css" rel="stylesheet" type="text/css" />

<style>
.feats-list li {
    background: url("${privateAssetsUrl}/img/checkmark.png") no-repeat 10px 50%;
    padding-left: 30px;
}


/*Service Box v1*/
.service-box-v1 {
    padding: 15px; 
    text-align: center;
}

.service-box-v1 i {
    padding: 15px;
    font-size: 35px;
}

.service-box-v1:hover {
    background: #0da3e2;
    transition:all 0.4s ease-in-out;
    -o-transition:all 0.4s ease-in-out;
    -moz-transition:all 0.4s ease-in-out;
    -webkit-transition:all 0.4s ease-in-out;
}

.service-box-v1:hover i,
.service-box-v1:hover p,
.service-box-v1:hover h4 {
    color: #fff;
}

</style>