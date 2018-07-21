<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
<title>Customer</title>
</head>
<body>

 <table width="100%" border="1">
  <tr>
   <td>ID</td>
   <td>Name</td>
   <td>Password</td>
  </tr>
  <c:forEach items="${list}" var="user" >
   <tr>
    <td>${user.id}</td>
    <td>${user.name}</td>
    <td>${user.password}</td>
   </tr>
  </c:forEach>
 </table>
</body>
</html>