<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="privateAssetsUrl" value="/private_assets" />

<div class="page-content">

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3" style="height: 100%;" ng-controller="ClientsCtrl">
				
				
				<div class="clients-filter input-icon left">
                    <i class="icon-search"></i>
                    <input class="m-wrap" type="text" placeholder="Filter..." ng-model="query">    
                </div>
				
				<ul class="clients-list unstyled">
					<li class="client-item" ng-repeat="client in clients | filter:query">
						<div class="client-name" >
							<span class="client-name-sp" ng-bind="client.name"></span>
						</div>
						<div class="client-config">
							<div class="btn-group">
								<a class="btn mini blue" href="#" data-toggle="dropdown"
									data-hover="dropdown" data-close-others="true">More <i
									class="icon-angle-down"></i></a>
								<ul class="dropdown-menu pull-right">
									<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
									<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
								</ul>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="span9">
                <div class="client-detail">
                    <h1>{{}}</h1>
                </div>
			</div>
		</div>
	</div>


<!-- 
    TODO fix dropdown on hover
 -->

</div>