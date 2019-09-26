<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
   <%@ include file="common/common.jsp" %>
   <%
      String access_token = request.getParameter("access_token") ;
   %>
<script type="text/javascript">
    var access_token="<%=access_token %>"
    $(document).ready(function() {

        $('#username').val(access_token)

        $('#mySubmit').submit();
    });
</script>
</head>
<body>
<div class="J_loginMain" style="display: none;">
   <div class="l_inner">
      <form action="${ctx }/token" method="post" id="mySubmit">
         <div class="i_main">
            <div class="m_txt">
               <c:choose>
                  <c:when test="${not empty msg }">${msg }</c:when>
                  <c:otherwise>一站式智慧校园全应用接入管理系统</c:otherwise>
               </c:choose>
            </div>
            <div class="m_input">
               <input placeholder="请输入帐号" type="text" name="access_token" id="username"/>
            </div>
            <div class="m_btn">
               <a href="javascript:mySubmit();">登录</a>
            </div>
         </div>
      </form>
   </div>
</div>

<%@ include file="common/footer.jsp" %>
</body>
</html>