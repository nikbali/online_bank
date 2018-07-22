<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
<title>Users</title>
</head>
<body>

 <h1>Users</h1>
 <table width="100%" border="1">
  <tr>
       <td>ID</td>
       <td>Login</td>
       <td>E-mail</td>
       <td>First Name</td>
       <td>Last Name</td>
       <td>Phone</td>
  </tr>
  <c:forEach items="${list}" var="user" >
   <tr>
        <td>${user.id}</td>
        <td>${user.login}</td>
        <td>${user.email}</td>
        <td>${user.first_name}</td>
        <td>${user.last_name}</td>
        <td>${user.phone}</td>
   </tr>
  </c:forEach>
 </table>
</body>
</html>