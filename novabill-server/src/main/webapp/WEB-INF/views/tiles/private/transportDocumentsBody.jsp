<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="dashboardUrl" value="/private/" />

<div class="page-content">
    <div class="container-fluid">
        <!-- BEGIN PAGE TITLE & BREADCRUMB-->
        <h3 class="page-title">Transport Documents</h3>
        <ul class="breadcrumb">
            <li><i class="icon-home"></i> <a href="${dashboardUrl}">Home</a>
                <i class="icon-angle-right"></i></li>
            <li><a href="#">Transport Documents</a></li>
        </ul>
        <!-- END PAGE TITLE & BREADCRUMB-->

        <div ng-view></div>
    </div>
</div>