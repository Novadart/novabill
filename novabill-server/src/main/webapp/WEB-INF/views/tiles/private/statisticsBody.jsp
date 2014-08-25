<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	
<div class="page-content" ng-app="novabill.stats">
    <div ng-view></div>
    <toaster-container toaster-options="{'time-out': 0, 'position-class': 'toast-top-right', 'close-button':true}"></toaster-container>
</div>