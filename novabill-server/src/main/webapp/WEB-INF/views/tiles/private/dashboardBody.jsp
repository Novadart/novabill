<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="page-content">
	<!-- BEGIN SAMPLE PORTLET CONFIGURATION MODAL FORM-->
	<div id="portlet-config" class="modal hide">
		<div class="modal-header">
			<button data-dismiss="modal" class="close" type="button"></button>
			<h3>Widget Settings</h3>
		</div>
		<div class="modal-body">
			Widget settings form goes here
		</div>
	</div>
	<!-- END SAMPLE PORTLET CONFIGURATION MODAL FORM-->
	<!-- BEGIN PAGE CONTAINER-->
	<div class="container-fluid">
		<!-- BEGIN PAGE HEADER-->
		<div class="row-fluid">
			<div class="span12">
				<!-- BEGIN STYLE CUSTOMIZER -->
				<div class="color-panel hidden-phone">
					<div class="color-mode-icons icon-color"></div>
					<div class="color-mode-icons icon-color-close"></div>
					<div class="color-mode">
						<p>THEME COLOR</p>
						<ul class="inline">
							<li class="color-black current color-default" data-style="default"></li>
							<li class="color-blue" data-style="blue"></li>
							<li class="color-brown" data-style="brown"></li>
							<li class="color-purple" data-style="purple"></li>
							<li class="color-grey" data-style="grey"></li>
							<li class="color-white color-light" data-style="light"></li>
						</ul>
						<label>
							<span>Layout</span>
							<select class="layout-option m-wrap small">
								<option value="fluid" selected>Fluid</option>
								<option value="boxed">Boxed</option>
							</select>
						</label>
						<label>
							<span>Header</span>
							<select class="header-option m-wrap small">
								<option value="fixed" selected>Fixed</option>
								<option value="default">Default</option>
							</select>
						</label>
						<label>
							<span>Sidebar</span>
							<select class="sidebar-option m-wrap small">
								<option value="fixed">Fixed</option>
								<option value="default" selected>Default</option>
							</select>
						</label>
						<label>
							<span>Footer</span>
							<select class="footer-option m-wrap small">
								<option value="fixed">Fixed</option>
								<option value="default" selected>Default</option>
							</select>
						</label>
					</div>
				</div>
				<!-- END BEGIN STYLE CUSTOMIZER -->    
				<!-- BEGIN PAGE TITLE & BREADCRUMB-->
				<h3 class="page-title">
					Dashboard <small>statistics and more</small>
				</h3>
				<ul class="breadcrumb">
					<li>
						<i class="icon-home"></i>
						<a href="index.html">Home</a> 
						<i class="icon-angle-right"></i>
					</li>
					<li><a href="#">Dashboard</a></li>
					<li class="pull-right no-text-shadow">
						<div id="dashboard-report-range" class="dashboard-date-range tooltips no-tooltip-on-touch-device responsive" data-tablet="" data-desktop="tooltips" data-placement="top" data-original-title="Change dashboard date range">
							<i class="icon-calendar"></i>
							<span></span>
							<i class="icon-angle-down"></i>
						</div>
					</li>
				</ul>
				<!-- END PAGE TITLE & BREADCRUMB-->
			</div>
		</div>
		<!-- END PAGE HEADER-->
		<div id="dashboard">
			<!-- BEGIN DASHBOARD STATS -->
			<div class="row-fluid">
				<div class="span3 responsive" data-tablet="span6" data-desktop="span3">
					<div class="dashboard-stat blue">
						<div class="visual">
							<i class="icon-comments"></i>
						</div>
						<div class="details">
							<div class="number">
								1349
							</div>
							<div class="desc">                           
								New Feedbacks
							</div>
						</div>
						<a class="more" href="#">
						View more <i class="m-icon-swapright m-icon-white"></i>
						</a>                 
					</div>
				</div>
				<div class="span3 responsive" data-tablet="span6" data-desktop="span3">
					<div class="dashboard-stat green">
						<div class="visual">
							<i class="icon-shopping-cart"></i>
						</div>
						<div class="details">
							<div class="number">549</div>
							<div class="desc">New Orders</div>
						</div>
						<a class="more" href="#">
						View more <i class="m-icon-swapright m-icon-white"></i>
						</a>                 
					</div>
				</div>
				<div class="span3 responsive" data-tablet="span6  fix-offset" data-desktop="span3">
					<div class="dashboard-stat purple">
						<div class="visual">
							<i class="icon-globe"></i>
						</div>
						<div class="details">
							<div class="number">+89%</div>
							<div class="desc">Brand Popularity</div>
						</div>
						<a class="more" href="#">
						View more <i class="m-icon-swapright m-icon-white"></i>
						</a>                 
					</div>
				</div>
				<div class="span3 responsive" data-tablet="span6" data-desktop="span3">
					<div class="dashboard-stat yellow">
						<div class="visual">
							<i class="icon-bar-chart"></i>
						</div>
						<div class="details">
							<div class="number">12,5M$</div>
							<div class="desc">Total Profit</div>
						</div>
						<a class="more" href="#">
						View more <i class="m-icon-swapright m-icon-white"></i>
						</a>                 
					</div>
				</div>
			</div>
			<!-- END DASHBOARD STATS -->
			<div class="clearfix"></div>
			<div class="row-fluid">
				<div class="span6">
					<!-- BEGIN PORTLET-->
					<div class="portlet solid bordered light-grey">
						<div class="portlet-title">
							<div class="caption"><i class="icon-bar-chart"></i>Site Visits</div>
							<div class="tools">
								<div class="btn-group pull-right" data-toggle="buttons-radio">
									<a href="" class="btn mini">Users</a>
									<a href="" class="btn mini active">Feedbacks</a>
								</div>
							</div>
						</div>
						<div class="portlet-body">
							<div id="site_statistics_loading">
								<img src="${privateAssetsUrl}/img/loading.gif" alt="loading" />
							</div>
							<div id="site_statistics_content" class="hide">
								<div id="site_statistics" class="chart"></div>
							</div>
						</div>
					</div>
					<!-- END PORTLET-->
				</div>
				<div class="span6">
					<!-- BEGIN PORTLET-->
					<div class="portlet solid light-grey bordered">
						<div class="portlet-title">
							<div class="caption"><i class="icon-bullhorn"></i>Activities</div>
							<div class="tools">
								<div class="btn-group pull-right" data-toggle="buttons-radio">
									<a href="" class="btn blue mini active">Users</a>
									<a href="" class="btn blue mini">Orders</a>
								</div>
							</div>
						</div>
						<div class="portlet-body">
							<div id="site_activities_loading">
								<img src="${privateAssetsUrl}/img/loading.gif" alt="loading" />
							</div>
							<div id="site_activities_content" class="hide">
								<div id="site_activities" style="height:100px;"></div>
							</div>
						</div>
					</div>
					<!-- END PORTLET-->
					<!-- BEGIN PORTLET-->
					<div class="portlet solid bordered light-grey">
						<div class="portlet-title">
							<div class="caption"><i class="icon-signal"></i>Server Load</div>
							<div class="tools">
								<div class="btn-group pull-right" data-toggle="buttons-radio">
									<a href="" class="btn red mini active">
									<span class="hidden-phone">Database</span>
									<span class="visible-phone">DB</span></a>
									<a href="" class="btn red mini">Web</a>
								</div>
							</div>
						</div>
						<div class="portlet-body">
							<div id="load_statistics_loading">
								<img src="${privateAssetsUrl}/img/loading.gif" alt="loading" />
							</div>
							<div id="load_statistics_content" class="hide">
								<div id="load_statistics" style="height:108px;"></div>
							</div>
						</div>
					</div>
					<!-- END PORTLET-->
				</div>
			</div>
			<div class="clearfix"></div>
			<div class="row-fluid">
				<div class="span6">
					<div class="portlet box blue">
						<div class="portlet-title">
							<div class="caption"><i class="icon-bell"></i>Recent Activities</div>
							<div class="actions">
								<div class="btn-group">
									<a class="btn" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
									Filter By
									<i class="icon-angle-down"></i>
									</a>
									<div class="dropdown-menu hold-on-click dropdown-checkboxes pull-right">
										<label><input type="checkbox"> Finance</label>
										<label><input type="checkbox" checked=""> Membership</label>
										<label><input type="checkbox"> Customer Support</label>
										<label><input type="checkbox" checked=""> HR</label>
										<label><input type="checkbox"> System</label>
									</div>
								</div>
							</div>
						</div>
						<div class="portlet-body">
							<div class="scroller" style="height:300px" data-always-visible="1" data-rail-visible="0">
								<ul class="feeds">
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label label-info">                        
														<i class="icon-check"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														You have 4 pending tasks.
														<span class="label label-warning label-mini">
														Take action 
														<i class="icon-share-alt"></i>
														</span>
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												Just now
											</div>
										</div>
									</li>
									<li>
										<a href="#">
											<div class="col1">
												<div class="cont">
													<div class="cont-col1">
														<div class="label label-success">                        
															<i class="icon-bar-chart"></i>
														</div>
													</div>
													<div class="cont-col2">
														<div class="desc">
															Finance Report for year 2013 has been released.   
														</div>
													</div>
												</div>
											</div>
											<div class="col2">
												<div class="date">
													20 mins
												</div>
											</div>
										</a>
									</li>
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label label-important">                      
														<i class="icon-user"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														You have 5 pending membership that requires a quick review.                       
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												24 mins
											</div>
										</div>
									</li>
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label label-info">                        
														<i class="icon-shopping-cart"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														New order received with <span class="label label-success">Reference Number: DR23923</span>             
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												30 mins
											</div>
										</div>
									</li>
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label label-success">                      
														<i class="icon-user"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														You have 5 pending membership that requires a quick review.                       
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												24 mins
											</div>
										</div>
									</li>
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label">                        
														<i class="icon-bell-alt"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														Web server hardware needs to be upgraded. 
														<span class="label label-inverse label-mini">Overdue</span>             
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												2 hours
											</div>
										</div>
									</li>
									<li>
										<a href="#">
											<div class="col1">
												<div class="cont">
													<div class="cont-col1">
														<div class="label label-inverse">                        
															<i class="icon-briefcase"></i>
														</div>
													</div>
													<div class="cont-col2">
														<div class="desc">
															IPO Report for year 2013 has been released.   
														</div>
													</div>
												</div>
											</div>
											<div class="col2">
												<div class="date">
													20 mins
												</div>
											</div>
										</a>
									</li>
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label label-info">                        
														<i class="icon-check"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														You have 4 pending tasks.
														<span class="label label-warning label-mini">
														Take action 
														<i class="icon-share-alt"></i>
														</span>
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												Just now
											</div>
										</div>
									</li>
									<li>
										<a href="#">
											<div class="col1">
												<div class="cont">
													<div class="cont-col1">
														<div class="label label-important">                        
															<i class="icon-bar-chart"></i>
														</div>
													</div>
													<div class="cont-col2">
														<div class="desc">
															Finance Report for year 2013 has been released.   
														</div>
													</div>
												</div>
											</div>
											<div class="col2">
												<div class="date">
													20 mins
												</div>
											</div>
										</a>
									</li>
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label label-inverse">                      
														<i class="icon-user"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														You have 5 pending membership that requires a quick review.                       
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												24 mins
											</div>
										</div>
									</li>
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label label-info">                        
														<i class="icon-shopping-cart"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														New order received with <span class="label label-success">Reference Number: DR23923</span>             
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												30 mins
											</div>
										</div>
									</li>
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label label-success">                      
														<i class="icon-user"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														You have 5 pending membership that requires a quick review.                       
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												24 mins
											</div>
										</div>
									</li>
									<li>
										<div class="col1">
											<div class="cont">
												<div class="cont-col1">
													<div class="label label-warning">                        
														<i class="icon-bell-alt"></i>
													</div>
												</div>
												<div class="cont-col2">
													<div class="desc">
														Web server hardware needs to be upgraded. 
														<span class="label label-inverse label-mini">Overdue</span>             
													</div>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="date">
												2 hours
											</div>
										</div>
									</li>
									<li>
										<a href="#">
											<div class="col1">
												<div class="cont">
													<div class="cont-col1">
														<div class="label label-info">                        
															<i class="icon-briefcase"></i>
														</div>
													</div>
													<div class="cont-col2">
														<div class="desc">
															IPO Report for year 2013 has been released.   
														</div>
													</div>
												</div>
											</div>
											<div class="col2">
												<div class="date">
													20 mins
												</div>
											</div>
										</a>
									</li>
								</ul>
							</div>
							<div class="scroller-footer">
								<div class="pull-right">
									<a href="#">See All Records <i class="m-icon-swapright m-icon-gray"></i></a> &nbsp;
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="portlet box green tasks-widget">
						<div class="portlet-title">
							<div class="caption"><i class="icon-check"></i>Tasks</div>
							<div class="tools">
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="" class="reload"></a>
							</div>
							<div class="actions">
								<div class="btn-group">
									<a class="btn mini" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
									More
									<i class="icon-angle-down"></i>
									</a>
									<ul class="dropdown-menu pull-right">
										<li><a href="#"><i class="i"></i> All Project</a></li>
										<li class="divider"></li>
										<li><a href="#">AirAsia</a></li>
										<li><a href="#">Cruise</a></li>
										<li><a href="#">HSBC</a></li>
										<li class="divider"></li>
										<li><a href="#">Pending <span class="badge badge-important">4</span></a></li>
										<li><a href="#">Completed <span class="badge badge-success">12</span></a></li>
										<li><a href="#">Overdue <span class="badge badge-warning">9</span></a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="portlet-body">
							<div class="task-content">
								<div class="scroller" style="height:305px" data-always-visible="1" data-rail-visible1="1">
									<!-- START TASK LIST -->
									<ul class="task-list unstyled">
										<li>
											<div class="task-checkbox">
												<input type="checkbox" class="liChild" value="" style="">                                       
											</div>
											<div class="task-title">
												<span class="task-title-sp">Present 2013 Year IPO Statistics at Board Meeting</span>
												<span class="label label-success">Company</span>
												<span class="task-bell"><i class="icon-bell"></i></span>
											</div>
											<div class="task-config">
												<div class="task-config-btn btn-group">
													<a class="btn mini blue" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">More <i class="icon-angle-down"></i></a>
													<ul class="dropdown-menu pull-right">
														<li><a href="#"><i class="icon-ok"></i> Complete</a></li>
														<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
														<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
													</ul>
												</div>
											</div>
										</li>
										<li>
											<div class="task-checkbox">
												<input type="checkbox" class="liChild" value="" style="">                                       
											</div>
											<div class="task-title">
												<span class="task-title-sp">Hold An Interview for Marketing Manager Position</span>
												<span class="label label-important">Marketing</span>
											</div>
											<div class="task-config">
												<div class="task-config-btn btn-group">
													<a class="btn mini blue" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">More <i class="icon-angle-down"></i></a>
													<ul class="dropdown-menu pull-right">
														<li><a href="#"><i class="icon-ok"></i> Complete</a></li>
														<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
														<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
													</ul>
												</div>
											</div>
										</li>
										<li>
											<div class="task-checkbox">
												<input type="checkbox" class="liChild" value="" style="">                                       
											</div>
											<div class="task-title">
												<span class="task-title-sp">AirAsia Intranet System Project Internal Meeting</span>
												<span class="label label-success">AirAsia</span>
												<span class="task-bell"><i class="icon-bell"></i></span>
											</div>
											<div class="task-config">
												<div class="task-config-btn btn-group">
													<a class="btn mini blue" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">More <i class="icon-angle-down"></i></a>
													<ul class="dropdown-menu pull-right">
														<li><a href="#"><i class="icon-ok"></i> Complete</a></li>
														<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
														<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
													</ul>
												</div>
											</div>
										</li>
										<li>
											<div class="task-checkbox">
												<input type="checkbox" class="liChild" value="" style="">                                       
											</div>
											<div class="task-title">
												<span class="task-title-sp">Technical Management Meeting</span>
												<span class="label label-warning">Company</span>
											</div>
											<div class="task-config">
												<div class="task-config-btn btn-group">
													<a class="btn mini blue" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">More <i class="icon-angle-down"></i></a>
													<ul class="dropdown-menu pull-right">
														<li><a href="#"><i class="icon-ok"></i> Complete</a></li>
														<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
														<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
													</ul>
												</div>
											</div>
										</li>
										<li>
											<div class="task-checkbox">
												<input type="checkbox" class="liChild" value="" style="">                                       
											</div>
											<div class="task-title">
												<span class="task-title-sp">Kick-off Company CRM Mobile App Development</span>
												<span class="label label-info">Internal Products</span>
											</div>
											<div class="task-config">
												<div class="task-config-btn btn-group">
													<a class="btn mini blue" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">More <i class="icon-angle-down"></i></a>
													<ul class="dropdown-menu pull-right">
														<li><a href="#"><i class="icon-ok"></i> Complete</a></li>
														<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
														<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
													</ul>
												</div>
											</div>
										</li>
										<li>
											<div class="task-checkbox">
												<input type="checkbox" class="liChild" value="" style="">                                       
											</div>
											<div class="task-title">
												<span class="task-title-sp">
												Prepare Commercial Offer For SmartVision Website Rewamp 
												</span>
												<span class="label label-important">SmartVision</span>
											</div>
											<div class="task-config">
												<div class="task-config-btn btn-group">
													<a class="btn mini blue" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">More <i class="icon-angle-down"></i></a>
													<ul class="dropdown-menu pull-right">
														<li><a href="#"><i class="icon-ok"></i> Complete</a></li>
														<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
														<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
													</ul>
												</div>
											</div>
										</li>
										<li>
											<div class="task-checkbox">
												<input type="checkbox" class="liChild" value="" style="">                                       
											</div>
											<div class="task-title">
												<span class="task-title-sp">Sign-Off The Comercial Agreement With AutoSmart</span>
												<span class="label label-inverse">AutoSmart</span>
												<span class="task-bell"><i class="icon-bell"></i></span>
											</div>
											<div class="task-config">
												<div class="task-config-btn btn-group">
													<a class="btn mini blue" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">More <i class="icon-angle-down"></i></a>
													<ul class="dropdown-menu pull-right">
														<li><a href="#"><i class="icon-ok"></i> Complete</a></li>
														<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
														<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
													</ul>
												</div>
											</div>
										</li>
										<li>
											<div class="task-checkbox">
												<input type="checkbox" class="liChild" value="" style="">                                       
											</div>
											<div class="task-title">
												<span class="task-title-sp">Company Staff Meeting</span>
												<span class="label label-success">Cruise</span>
												<span class="task-bell"><i class="icon-bell"></i></span>
											</div>
											<div class="task-config">
												<div class="task-config-btn btn-group">
													<a class="btn mini blue" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">More <i class="icon-angle-down"></i></a>
													<ul class="dropdown-menu pull-right">
														<li><a href="#"><i class="icon-ok"></i> Complete</a></li>
														<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
														<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
													</ul>
												</div>
											</div>
										</li>
										<li class="last-line">
											<div class="task-checkbox">
												<input type="checkbox" class="liChild" value="" style="">                                       
											</div>
											<div class="task-title">
												<span class="task-title-sp">KeenThemes Investment Discussion</span>
												<span class="label label-warning">KeenThemes</span>
											</div>
											<div class="task-config">
												<div class="task-config-btn btn-group">
													<a class="btn mini blue" href="#" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">More <i class="icon-angle-down"></i></a>
													<ul class="dropdown-menu pull-right">
														<li><a href="#"><i class="icon-ok"></i> Complete</a></li>
														<li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
														<li><a href="#"><i class="icon-trash"></i> Cancel</a></li>
													</ul>
												</div>
											</div>
										</li>
									</ul>
									<!-- END START TASK LIST -->
								</div>
							</div>
							<div class="task-footer">
								<span class="pull-right">
								<a href="#">See All Tasks <i class="m-icon-swapright m-icon-gray"></i></a> &nbsp;
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="clearfix"></div>
			<div class="row-fluid">
				<div class="span6">
					<div class="portlet box purple">
						<div class="portlet-title">
							<div class="caption"><i class="icon-calendar"></i>General Stats</div>
							<div class="actions">
								<a href="javascript:;" class="btn yellow easy-pie-chart-reload"><i class="icon-repeat"></i> Reload</a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="row-fluid">
								<div class="span4">
									<div class="easy-pie-chart">
										<div class="number transactions"  data-percent="55"><span>+55</span>%</div>
										<a class="title" href="#">Transactions <i class="m-icon-swapright"></i></a>
									</div>
								</div>
								<div class="margin-bottom-10 visible-phone"></div>
								<div class="span4">
									<div class="easy-pie-chart">
										<div class="number visits"  data-percent="85"><span>+85</span>%</div>
										<a class="title" href="#">New Visits <i class="m-icon-swapright"></i></a>
									</div>
								</div>
								<div class="margin-bottom-10 visible-phone"></div>
								<div class="span4">
									<div class="easy-pie-chart">
										<div class="number bounce"  data-percent="46"><span>-46</span>%</div>
										<a class="title" href="#">Bounce <i class="m-icon-swapright"></i></a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="portlet box blue">
						<div class="portlet-title">
							<div class="caption"><i class="icon-calendar"></i>Server Stats</div>
							<div class="tools">
								<a href="" class="collapse"></a>
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="" class="reload"></a>
								<a href="" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div class="row-fluid">
								<div class="span4">
									<div class="sparkline-chart">
										<div class="number" id="sparkline_bar"></div>
										<a class="title" href="#">Network <i class="m-icon-swapright"></i></a>
									</div>
								</div>
								<div class="margin-bottom-10 visible-phone"></div>
								<div class="span4">
									<div class="sparkline-chart">
										<div class="number" id="sparkline_bar2"></div>
										<a class="title" href="#">CPU Load <i class="m-icon-swapright"></i></a>
									</div>
								</div>
								<div class="margin-bottom-10 visible-phone"></div>
								<div class="span4">
									<div class="sparkline-chart">
										<div class="number" id="sparkline_line"></div>
										<a class="title" href="#">Load Rate <i class="m-icon-swapright"></i></a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="clearfix"></div>
			<div class="row-fluid">
				<div class="span6">
					<!-- BEGIN REGIONAL STATS PORTLET-->
					<div class="portlet">
						<div class="portlet-title">
							<div class="caption"><i class="icon-globe"></i>Regional Stats</div>
							<div class="tools">
								<a href="" class="collapse"></a>
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="" class="reload"></a>
								<a href="" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body">
							<div id="region_statistics_loading">
								<img src="${privateAssetsUrl}/img/loading.gif" alt="loading" />
							</div>
							<div id="region_statistics_content" class="hide">
								<div class="btn-toolbar">
									<div class="btn-group " data-toggle="buttons-radio">
										<a href="" class="btn mini active">Users</a>
										<a href="" class="btn mini">Orders</a> 
									</div>
									<div class="btn-group pull-right">
										<a href="" class="btn mini dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
										Select Region <span class="icon-angle-down"></span>
										</a>
										<ul class="dropdown-menu pull-right">
											<li><a href="javascript:;" id="regional_stat_world">World</a></li>
											<li><a href="javascript:;" id="regional_stat_usa">USA</a></li>
											<li><a href="javascript:;" id="regional_stat_europe">Europe</a></li>
											<li><a href="javascript:;" id="regional_stat_russia">Russia</a></li>
											<li><a href="javascript:;" id="regional_stat_germany">Germany</a></li>
										</ul>
									</div>
								</div>
								<div id="vmap_world" class="vmaps chart hide"></div>
								<div id="vmap_usa" class="vmaps chart hide"></div>
								<div id="vmap_europe" class="vmaps chart hide"></div>
								<div id="vmap_russia" class="vmaps chart hide"></div>
								<div id="vmap_germany" class="vmaps chart hide"></div>
							</div>
						</div>
					</div>
					<!-- END REGIONAL STATS PORTLET-->
				</div>
				<div class="span6">
					<!-- BEGIN PORTLET-->
					<div class="portlet paddingless">
						<div class="portlet-title line">
							<div class="caption"><i class="icon-bell"></i>Feeds</div>
							<div class="tools">
								<a href="" class="collapse"></a>
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="" class="reload"></a>
								<a href="" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body">
							<!--BEGIN TABS-->
							<div class="tabbable tabbable-custom">
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tab_1_1" data-toggle="tab">System</a></li>
									<li><a href="#tab_1_2" data-toggle="tab">Activities</a></li>
									<li><a href="#tab_1_3" data-toggle="tab">Recent Users</a></li>
								</ul>
								<div class="tab-content">
									<div class="tab-pane active" id="tab_1_1">
										<div class="scroller" style="height:290px" data-always-visible="1" data-rail-visible="0">
											<ul class="feeds">
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-success">                        
																	<i class="icon-bell"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	You have 4 pending tasks.
																	<span class="label label-important label-mini">
																	Take action 
																	<i class="icon-share-alt"></i>
																	</span>
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															Just now
														</div>
													</div>
												</li>
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New version v1.4 just lunched!   
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																20 mins
															</div>
														</div>
													</a>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-important">                      
																	<i class="icon-bolt"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	Database server #12 overloaded. Please fix the issue.                      
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															24 mins
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-info">                        
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															30 mins
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-success">                        
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															40 mins
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-warning">                        
																	<i class="icon-plus"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New user registered.                
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															1.5 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-success">                        
																	<i class="icon-bell-alt"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	Web server hardware needs to be upgraded. 
																	<span class="label label-inverse label-mini">Overdue</span>             
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															2 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label">                       
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															3 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-warning">                        
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															5 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-info">                        
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															18 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label">                       
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															21 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-info">                        
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															22 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label">                       
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															21 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-info">                        
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															22 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label">                       
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															21 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-info">                        
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															22 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label">                       
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															21 hours
														</div>
													</div>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-info">                        
																	<i class="icon-bullhorn"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	New order received. Please take care of it.                 
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															22 hours
														</div>
													</div>
												</li>
											</ul>
										</div>
									</div>
									<div class="tab-pane" id="tab_1_2">
										<div class="scroller" style="height:290px" data-always-visible="1" data-rail-visible1="1">
											<ul class="feeds">
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New user registered
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																Just now
															</div>
														</div>
													</a>
												</li>
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New order received 
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																10 mins
															</div>
														</div>
													</a>
												</li>
												<li>
													<div class="col1">
														<div class="cont">
															<div class="cont-col1">
																<div class="label label-important">                      
																	<i class="icon-bolt"></i>
																</div>
															</div>
															<div class="cont-col2">
																<div class="desc">
																	Order #24DOP4 has been rejected.    
																	<span class="label label-important label-mini">Take action <i class="icon-share-alt"></i></span>
																</div>
															</div>
														</div>
													</div>
													<div class="col2">
														<div class="date">
															24 mins
														</div>
													</div>
												</li>
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New user registered
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																Just now
															</div>
														</div>
													</a>
												</li>
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New user registered
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																Just now
															</div>
														</div>
													</a>
												</li>
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New user registered
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																Just now
															</div>
														</div>
													</a>
												</li>
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New user registered
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																Just now
															</div>
														</div>
													</a>
												</li>
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New user registered
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																Just now
															</div>
														</div>
													</a>
												</li>
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New user registered
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																Just now
															</div>
														</div>
													</a>
												</li>
												<li>
													<a href="#">
														<div class="col1">
															<div class="cont">
																<div class="cont-col1">
																	<div class="label label-success">                        
																		<i class="icon-bell"></i>
																	</div>
																</div>
																<div class="cont-col2">
																	<div class="desc">
																		New user registered
																	</div>
																</div>
															</div>
														</div>
														<div class="col2">
															<div class="date">
																Just now
															</div>
														</div>
													</a>
												</li>
											</ul>
										</div>
									</div>
									<div class="tab-pane" id="tab_1_3">
										<div class="scroller" style="height:290px" data-always-visible="1" data-rail-visible1="1">
											<div class="row-fluid">
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Robert Nilson</a> 
															<span class="label label-success">Approved</span>
														</div>
														<div>29 Jan 2013 10:45AM</div>
													</div>
												</div>
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Lisa Miller</a> 
															<span class="label label-info">Pending</span>
														</div>
														<div>19 Jan 2013 10:45AM</div>
													</div>
												</div>
											</div>
											<div class="row-fluid">
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Eric Kim</a> 
															<span class="label label-info">Pending</span>
														</div>
														<div>19 Jan 2013 12:45PM</div>
													</div>
												</div>
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Lisa Miller</a> 
															<span class="label label-important">In progress</span>
														</div>
														<div>19 Jan 2013 11:55PM</div>
													</div>
												</div>
											</div>
											<div class="row-fluid">
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Eric Kim</a> 
															<span class="label label-info">Pending</span>
														</div>
														<div>19 Jan 2013 12:45PM</div>
													</div>
												</div>
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Lisa Miller</a> 
															<span class="label label-important">In progress</span>
														</div>
														<div>19 Jan 2013 11:55PM</div>
													</div>
												</div>
											</div>
											<div class="row-fluid">
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div><a href="#">Eric Kim</a> <span class="label label-info">Pending</span></div>
														<div>19 Jan 2013 12:45PM</div>
													</div>
												</div>
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Lisa Miller</a> 
															<span class="label label-important">In progress</span>
														</div>
														<div>19 Jan 2013 11:55PM</div>
													</div>
												</div>
											</div>
											<div class="row-fluid">
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div><a href="#">Eric Kim</a> <span class="label label-info">Pending</span></div>
														<div>19 Jan 2013 12:45PM</div>
													</div>
												</div>
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Lisa Miller</a> 
															<span class="label label-important">In progress</span>
														</div>
														<div>19 Jan 2013 11:55PM</div>
													</div>
												</div>
											</div>
											<div class="row-fluid">
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Eric Kim</a> 
															<span class="label label-info">Pending</span>
														</div>
														<div>19 Jan 2013 12:45PM</div>
													</div>
												</div>
												<div class="span6 user-info">
													<img alt="" src="${privateAssetsUrl}/img/avatar.png" />
													<div class="details">
														<div>
															<a href="#">Lisa Miller</a> 
															<span class="label label-important">In progress</span>
														</div>
														<div>19 Jan 2013 11:55PM</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<!--END TABS-->
						</div>
					</div>
					<!-- END PORTLET-->
				</div>
			</div>
			<div class="clearfix"></div>
			<div class="row-fluid">
				<div class="span6">
					<!-- BEGIN PORTLET-->
					<div class="portlet box blue calendar">
						<div class="portlet-title">
							<div class="caption"><i class="icon-calendar"></i>Calendar</div>
						</div>
						<div class="portlet-body light-grey">
							<div id="calendar"></div>
						</div>
					</div>
					<!-- END PORTLET-->
				</div>
				<div class="span6">
					<!-- BEGIN PORTLET-->
					<div class="portlet">
						<div class="portlet-title line">
							<div class="caption"><i class="icon-comments"></i>Chats</div>
							<div class="tools">
								<a href="" class="collapse"></a>
								<a href="#portlet-config" data-toggle="modal" class="config"></a>
								<a href="" class="reload"></a>
								<a href="" class="remove"></a>
							</div>
						</div>
						<div class="portlet-body" id="chats">
							<div class="scroller" style="height:435px" data-always-visible="1" data-rail-visible1="1">
								<ul class="chats">
									<li class="in">
										<img class="avatar" alt="" src="${privateAssetsUrl}/img/avatar1.jpg" />
										<div class="message">
											<span class="arrow"></span>
											<a href="#" class="name">Bob Nilson</a>
											<span class="datetime">at Jul 25, 2012 11:09</span>
											<span class="body">
											Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
											</span>
										</div>
									</li>
									<li class="out">
										<img class="avatar" alt="" src="${privateAssetsUrl}/img/avatar2.jpg" />
										<div class="message">
											<span class="arrow"></span>
											<a href="#" class="name">Lisa Wong</a>
											<span class="datetime">at Jul 25, 2012 11:09</span>
											<span class="body">
											Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
											</span>
										</div>
									</li>
									<li class="in">
										<img class="avatar" alt="" src="${privateAssetsUrl}/img/avatar1.jpg" />
										<div class="message">
											<span class="arrow"></span>
											<a href="#" class="name">Bob Nilson</a>
											<span class="datetime">at Jul 25, 2012 11:09</span>
											<span class="body">
											Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
											</span>
										</div>
									</li>
									<li class="out">
										<img class="avatar" alt="" src="${privateAssetsUrl}/img/avatar3.jpg" />
										<div class="message">
											<span class="arrow"></span>
											<a href="#" class="name">Richard Doe</a>
											<span class="datetime">at Jul 25, 2012 11:09</span>
											<span class="body">
											Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
											</span>
										</div>
									</li>
									<li class="in">
										<img class="avatar" alt="" src="${privateAssetsUrl}/img/avatar3.jpg" />
										<div class="message">
											<span class="arrow"></span>
											<a href="#" class="name">Richard Doe</a>
											<span class="datetime">at Jul 25, 2012 11:09</span>
											<span class="body">
											Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
											</span>
										</div>
									</li>
									<li class="out">
										<img class="avatar" alt="" src="${privateAssetsUrl}/img/avatar1.jpg" />
										<div class="message">
											<span class="arrow"></span>
											<a href="#" class="name">Bob Nilson</a>
											<span class="datetime">at Jul 25, 2012 11:09</span>
											<span class="body">
											Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
											</span>
										</div>
									</li>
									<li class="in">
										<img class="avatar" alt="" src="${privateAssetsUrl}/img/avatar3.jpg" />
										<div class="message">
											<span class="arrow"></span>
											<a href="#" class="name">Richard Doe</a>
											<span class="datetime">at Jul 25, 2012 11:09</span>
											<span class="body">
											Lorem ipsum dolor sit amet, consectetuer adipiscing elit, 
											sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.
											</span>
										</div>
									</li>
									<li class="out">
										<img class="avatar" alt="" src="${privateAssetsUrl}/img/avatar1.jpg" />
										<div class="message">
											<span class="arrow"></span>
											<a href="#" class="name">Bob Nilson</a>
											<span class="datetime">at Jul 25, 2012 11:09</span>
											<span class="body">
											Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. sed diam nonummy nibh euismod tincidunt ut laoreet.
											</span>
										</div>
									</li>
								</ul>
							</div>
							<div class="chat-form">
								<div class="input-cont">   
									<input class="m-wrap" type="text" placeholder="Type a message here..." />
								</div>
								<div class="btn-cont"> 
									<span class="arrow"></span>
									<a href="" class="btn blue icn-only"><i class="icon-ok icon-white"></i></a>
								</div>
							</div>
						</div>
					</div>
					<!-- END PORTLET-->
				</div>
			</div>
		</div>
	</div>
	<!-- END PAGE CONTAINER-->    
</div>