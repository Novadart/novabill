<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />

<div class="page-content" ng-controller="ClientsCtrl">

	<div class="container">
	    <button type="button" class="btn red new-client" ng-click="newClientClick()">New Client</button>
		
		<div class="clients-filter input-icon left">
			<i class="icon-search"></i> 
		      <input class="m-wrap"	autofocus="autofocus" type="text" placeholder="Filter..." 
		          ng-model="query" ng-keyup="filterKeyUp()">
		</div>

		<div class="partition" ng-repeat="pt in partitions">
			<div class="letter">
				<h2>
					<strong ng-cloak>{{pt.letter}}</strong>
				</h2>
			</div>
			<div class="clients">
				<div class="client" ng-repeat="cl in pt.clients" ng-click="clientClick(cl)">
					<h3 ng-cloak>{{cl.name}}</h3>
					<span class="address">{{address(cl)}}</span>
				</div>
			</div>
		</div>

	</div>

</div>