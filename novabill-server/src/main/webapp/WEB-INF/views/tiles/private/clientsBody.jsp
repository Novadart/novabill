<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="page-content" >

	<div ng-controller="ClientsCtrl">
		<ul>
			<li ng-repeat="client in clients" ng-bind="client.name"></li>
		</ul>
	</div>

</div>