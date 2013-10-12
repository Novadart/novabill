<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />

<div class="page-content" ng-controller="ClientsCtrl">

	<div class="container">
	   <button type="button" class="btn red new-client" ng-click="newClientClicked()">New Client</button>
                
       <div class="clients-filter input-icon left">
           <i class="icon-search"></i>
           <input class="m-wrap" type="text" placeholder="Filter..." ng-model="query">    
       </div>


		<div class="partition" ng-repeat="(letter, partition) in clients" >
			<h1>
				<strong ng-cloak>{{letter}}</strong>
			</h1>
			<div>
				<div style="display: inline-block;" ng-repeat="cl in partition | filter:query">
					<h3 ng-cloak>{{cl.name}}</h3>

				</div>
			</div>
		</div>

	</div>


<!-- 
    TODO fix dropdown on hover
 -->

</div>