<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />

<div class="page-content" ng-controller="ClientsCtrl">

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3" style="height: 100%;" >
				
				<button type="button" class="btn red new-client" ng-click="newClientClicked()">New Client</button>
				
				<div class="clients-filter input-icon left">
                    <i class="icon-search"></i>
                    <input class="m-wrap" type="text" placeholder="Filter..." ng-model="query">    
                </div>
				
				<ul class="clients-list unstyled">
					<li class="client-item" ng-repeat="client in clients | filter:query">
						<div class="client-name" >
							<a href="#/{{client.id}}" class="client-name-sp" ng-cloak>{{client.name}}</a>
						</div>
					</li>
				</ul>
			</div>
			<div class="span9">
                <div ng-view></div>
			</div>
		</div>
	</div>


<!-- 
    TODO fix dropdown on hover
 -->

</div>