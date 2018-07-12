<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags"   prefix="s"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh-CN" class="ax-vertical-centered">
<head>
	<meta charset="UTF-8">
	<title>武汉理工大学图书馆管理系统后台</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"> 
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-admin-theme.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-admin-theme.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/jQuery/jquery-3.1.1.min.js"></script>
            <script src="${pageContext.request.contextPath}/js/bootstrap-dropdown.min.js"></script>
            
            <script src="${pageContext.request.contextPath}/ajax-lib/ajaxutils.js"></script>
            <script src="${pageContext.request.contextPath}/js/adminUpdateInfo.js"></script>
             <script src="${pageContext.request.contextPath}/js/adminUpdatePwd.js"></script>
             
 		<script src="${pageContext.request.contextPath}/js/updateReaderType.js"></script>
       <script src="${pageContext.request.contextPath}/js/addReaderType.js"></script>
</head>



<script src="${pageContext.request.contextPath}/jQuery/jquery-3.1.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>


<body class="bootstrap-admin-with-small-navbar">
<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<button type="button" class="navbar-toggle"  data-target=".navbar-collapse">
			<script type="text/javascript">
                $(".navbar-toggle").click(function(){
                    $(".navbar-collapse").toggle();
                });
			</script>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="${pageContext.request.contextPath}/admin/admin.jsp"><strong>WHUT图书管理系统</strong></a>
		<div class="navbar-collapse nav-collapse collapse navbar-header pull-right">
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false" data-hover="dropdown">欢迎您，<s:property value="#session.admin.name"/> <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="#updateinfo" data-toggle="modal">个人资料</a></li>
						<li><a href="#updatepwd" data-toggle="modal">修改密码</a></li>
						<li class="divider"></li>
						<li><a href="${pageContext.request.contextPath}/adminLoginAction_logout.action">退出</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</nav>
<div class="container">
	<!-- left, vertical navbar & content -->
	<div class="row">
		<div id="wrapper">
			<div class="overlay"></div>

			<!-- Sidebar -->
			<nav class="navbar navbar-inverse navbar-fixed-top" id="sidebar-wrapper" role="navigation">
				<ul class="nav sidebar-nav">
					<li class="active">
						<a href="${pageContext.request.contextPath}/admin/bookManageAction_findBookByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 图书管理</a>
					</li>
					<li>
						<a href="${pageContext.request.contextPath}/admin/bookTypeManageAction_findBookTypeByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 图书分类管理</a>
					</li>
					<li>
						<a href="${pageContext.request.contextPath}/admin/borrowManageAction_findBorrowInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 图书借阅</a>
					</li>
					<li>
						<a href="${pageContext.request.contextPath}/admin/backManageAction_findBackInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 图书归还</a>
					</li>

					<li>
						<a href="${pageContext.request.contextPath}/admin/borrowSearchAction_findBackInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 借阅查询</a>
					</li>
					<li>
						<a href="${pageContext.request.contextPath}/admin/forfeitManageAction_findForfeitInfoByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 逾期处理</a>
					</li>
					<s:if test="#session.admin.authorization.superSet==1"><!-- 对超级管理员和普通管理员进行权限区分 -->
					<li>
						<a href="${pageContext.request.contextPath}/admin/adminManageAction_findAdminByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 管理员管理</a>
					</li>
					</s:if>
					<li>
						<a href="${pageContext.request.contextPath}/admin/readerManageAction_findReaderByPage.action"><i class="glyphicon glyphicon-chevron-right"></i> 读者管理</a>
					</li>
					<li>
						<a href="${pageContext.request.contextPath}/admin/readerTypeManageAction_getAllReaderType.action"><i class="glyphicon glyphicon-chevron-right"></i> 系统设置</a>
					</li>
				</ul>
			</nav>
			<!-- /#sidebar-wrapper -->

			<!-- Page Content -->
			<div id="page-content-wrapper">
				<button type="button" class="hamburger is-closed animated fadeInLeft" data-toggle="offcanvas">
					<span class="hamb-top"></span>
					<span class="hamb-middle"></span>
					<span class="hamb-bottom"></span>
				</button>
			</div>
			<!-- /#page-content-wrapper -->

			<script type="text/javascript">
                $(document).ready(function () {
                    var trigger = $('.hamburger'),
                        overlay = $('.overlay'),
                        isClosed = false;

                    trigger.click(function () {
                        hamburger_cross();
                    });

                    function hamburger_cross() {

                        if (isClosed == true) {
                            overlay.hide();
                            trigger.removeClass('is-open');
                            trigger.addClass('is-closed');
                            isClosed = false;
                        } else {
                            overlay.show();
                            trigger.removeClass('is-closed');
                            trigger.addClass('is-open');
                            isClosed = true;
                        }
                    }

                    $('[data-toggle="offcanvas"]').click(function () {
                        $('#wrapper').toggleClass('toggled');
                    });
                });
			</script>
		</div>
		<!-- /#wrapper -->

           <!-- content -->
            <div class="col-xs-12 col-lg-11 pull-right">
              <div class="row">
                    <div class="col-lg-11">
                        <div class="panel panel-default bootstrap-admin-no-table-panel">
                            <div class="panel-heading">
                                <div class="text-muted bootstrap-admin-box-title">添加读者类型</div>
                            </div>
                            <div class="bootstrap-admin-no-table-panel-content bootstrap-admin-panel-content collapse in">
                                    <div class="col-lg-2 form-group">
                                        <button type="button" class="btn btn-primary" id="btn_add" data-toggle="modal" data-target="#addModal">添加</button>          
                                    </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row table-responsive">
                    <div class="col-xs-11">
                        <table id="data_list" class="table table-hover table-bordered" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>读者类型</th>
                                <th>最大借阅数量</th>
                                <th>可借阅天数</th>
                                <th>逾期每日罚金</th>
                                <th>可续借天数</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            
                            
                            <!---在此插入信息-->
                            <s:if test="#request.readerTypes!=null">
                            <s:iterator value="#request.readerTypes" var="readerType">
                             <tbody>
	                         	   <td><s:property value="#readerType.readerTypeName"/></td>
	                                <td><s:property value="#readerType.maxNum"/></td>
	                                <td><s:property value="#readerType.backDay"/></td>
	                                <td><s:property value="#readerType.penalty"/></td>
	                                <td><s:property value="#readerType.renewDay"/></td>
	                                <td>
	                                	<button type="button" class="btn btn-warning btn-xs" data-toggle="modal" data-target="#updateModal" onclick="updateReaderType(<s:property value="#readerType.readerTypeId"/>)">修改</button>
	                               	</td>                                              
                          	  </tbody>
                            </s:iterator>
                            </s:if>
                            <s:else>
                            	<tbody>
	                         	   	<td>暂无数据</td>
	                         	   	<td>暂无数据</td>
	                                <td>暂无数据</td>
	                                <td>暂无数据</td>
	                                <td>暂无数据</td>
	                                <td>暂无数据</td>                                              
                          	  </tbody>
                            </s:else>
                            
                        </table>
                        
                               
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    

     
                                     <!-- 修改模态框（Modal） -->
                                     <!-------------------------------------------------------------->  
                                
                                        <!-- 修改模态框（Modal） -->
                               <form class="form-horizontal">   <!--保证样式水平不混乱-->   
									<div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="updateModalLabel" aria-hidden="true">
										<div class="modal-dialog">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
														&times;
													</button>
													<h4 class="modal-title" id="updateModalLabel">
														修改读者分类设置
													</h4>
												</div>
												<div class="modal-body">
												
										<!---------------------表单-------------------->
										
										
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">读者类型名称</label>
												<div class="col-sm-7">
													<input type="hidden" id="readerTypeId">
													<input type="text" class="form-control" id="readerTypeName" placeholder="请输入读者类型名称">
													<label class="control-label" for="readerTypeName" style="display: none;"></label>	
												</div>
										</div>
											
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">最大借阅数量</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="maxNum" placeholder="请输入最大借阅数量">
												<label class="control-label" for="maxNum" style="display: none;"></label>	
												</div>
										</div>
											
									
										
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">最大借阅天数</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="bday" placeholder="请输入最大借阅天数">
												<label class="control-label" for="bday" style="display: none;"></label>	
												</div>
										</div>
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">逾期每日罚金</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="penalty"  placeholder="请输入逾期每日罚金">
												<label class="control-label" for="penalty" style="display: none;"></label>	
												</div>
										</div>
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">续借天数</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="renewDays"  placeholder="请输入续借天数">
													<label class="control-label" for="renewDays" style="display: none;"></label>	
												</div>
										</div>
										
										<!---------------------表单-------------------->
															
										</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭
													</button>
													<button type="button" class="btn btn-primary" id="updateType">
														修改
													</button>
												</div>
											</div><!-- /.modal-content -->
										</div><!-- /.modal -->
									</div>
	
                                 </form>
                                   <!-------------------------------------------------------------->
 
    
    
    
    
 
 
 
 
    
    <!------------------------------修改密码模糊框-------------------------------->  
                 
                   <form class="form-horizontal">   <!--保证样式水平不混乱-->                  
                                     <!-- 模态框（Modal） -->
				<div class="modal fade" id="updatepwd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
									&times;
								</button>
								<h4 class="modal-title" id="myModalLabel">
									修改密码
								</h4>
							</div>
							
							<div class="modal-body">
							 
								<!--正文-->
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">原密码</label>
								<div class="col-sm-7">
									<input type="password" class="form-control" id="oldPwd"  placeholder="请输入原密码">
											<label class="control-label" for="oldPwd" style="display: none;"></label>			
								</div>
							</div>	
							
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">新密码</label>
								<div class="col-sm-7">
									<input type="password" class="form-control" id="newPwd"  placeholder="请输入新密码">
										<label class="control-label" for="newPwd" style="display: none;"></label>			
								</div>
							</div>	
							
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">确认密码</label>
								<div class="col-sm-7">
									<input type="password" class="form-control" id="confirmPwd"  placeholder="请输入确认密码">
										<label class="control-label" for="confirmPwd" style="display: none;"></label>			
								</div>
							</div>	
								<!--正文-->
								
								
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default" data-dismiss="modal">关闭
								</button>
								<button type="button" class="btn btn-primary" id="update_adminPwd">
									修改
								</button>
							</div>
						</div><!-- /.modal-content -->
					</div><!-- /.modal -->
				</div>

				</form>	
                                   <!-------------------------------------------------------------->
                                   
                                   
                                   
                                   
                                   
                                   
                                   
                                   
                                   
                                   <!-------------------------个人资料模糊框------------------------------------->  
                 
                   <form class="form-horizontal">   <!--保证样式水平不混乱-->                  
                                     <!-- 模态框（Modal） -->
				<div class="modal fade" id="updateinfo" tabindex="-1" role="dialog" aria-labelledby="ModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
									&times;
								</button>
								<h4 class="modal-title" id="ModalLabel">
									个人资料
								</h4>
							</div>
							
							<div class="modal-body">
							 
								<!--正文-->
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">用户名</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" id="username"  value='<s:property value="#session.admin.username"/>'>
											<label class="control-label" for="username" style="display: none;"></label>		
								</div>
							</div>				
								
								
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">真实姓名</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" id="name"  placeholder="请输入您的真实姓名" value='<s:property value="#session.admin.name"/>'>
												<label class="control-label" for="name" style="display: none;"></label>	
								</div>
							</div>	
							
							<div class="form-group">
								<label for="firstname" class="col-sm-3 control-label">联系号码</label>
								<div class="col-sm-7">
									<input type="text" class="form-control" id="phone"  placeholder="请输入您的联系号码" value='<s:property value="#session.admin.phone"/>'>
											<label class="control-label" for="phone" style="display: none;"></label>			
								</div>
							</div>	
							
								<!--正文-->
								
								
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default" data-dismiss="modal">关闭
								</button>
								<button type="button" class="btn btn-primary" id="admin_updateInfo" >
									修改
								</button>
							</div>
						</div><!-- /.modal-content -->
					</div><!-- /.modal -->
				</div>

				</form>	
                                   <!-------------------------------------------------------------->
    
    
    
    
				    <div class="modal fade" id="modal_info" tabindex="-1" role="dialog" aria-labelledby="addModalLabel">
				    <div class="modal-dialog" role="document">
				        <div class="modal-content">
				            <div class="modal-header">
				                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				                <h4 class="modal-title" id="infoModalLabel">提示</h4>
				            </div>
				            <div class="modal-body">
				                <div class="row">
				                    <div class="col-lg-12" id="div_info"></div>
				                </div>
				            </div>
				            <div class="modal-footer">
				                <button type="button" class="btn btn-default" id="btn_info_close" data-dismiss="modal">关闭</button>
				            </div>
				        </div>
				    </div>
				</div>
				    
    
 
 
  <!--------------------------------------添加的模糊框------------------------>  
                                 <form class="form-horizontal">   <!--保证样式水平不混乱-->   
                                        <!-- 模态框（Modal） -->
									<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
										<div class="modal-dialog">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
														&times;
													</button>
													<h4 class="modal-title" id="myModalLabel">
														添加新读者分类
													</h4>
												</div>
												<div class="modal-body">
												
										<!---------------------表单-------------------->
										 <div class="form-group">
											<label for="firstname" class="col-sm-3 control-label">读者类型</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="addType"  placeholder="请输入读者类型名称">
												<label class="control-label" for="addType" style="display: none;"></label>		
												</div>
										</div>
											
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">最大借阅书量</label>
											<div class="col-sm-7">
												<input type="text" class="form-control" id="addMaxNum"  placeholder="请输入最大借阅书量">
											<label class="control-label" for="addMaxNum" style="display: none;"></label>	
											</div>
										</div>
											
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">可借天数</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="addBday"  placeholder="请输入可借天数">
												<label class="control-label" for="addBday" style="display: none;"></label>	
												</div>
										</div>
										
										
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">逾期每日罚金</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="addPenalty"  placeholder="请输入每日罚金">
												<label class="control-label" for="addPenalty" style="display: none;"></label>	
												</div>
										</div>
										<div class="form-group">	
											<label for="firstname" class="col-sm-3 control-label">续借天数</label>
												<div class="col-sm-7">
													<input type="text" class="form-control" id="addrenewDays"  placeholder="请输入逾期每日罚金">
												<label class="control-label" for="addrenewDays" style="display: none;"></label>	
												</div>
										</div>
										<!---------------------表单-------------------->
									</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default" data-dismiss="modal">关闭
													</button>
													<button type="button" class="btn btn-primary" id="addReaderType">
														添加
													</button>
												</div>
											</div><!-- /.modal-content -->
										</div><!-- /.modal -->
									</div>

                                 </form>	
 								<!--------------------------------------添加的模糊框------------------------>  
 

 
</body>
</html>
