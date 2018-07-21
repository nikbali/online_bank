<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
<title>Add User</title>
</head>
<body>
    <form:form action="/users/save" method="POST" modelAttribute="user">
      <form:hidden path="id"/>
      <table>
       <tr>
        <td>Name</td>
        <td><form:input path="name"/></td>
       </tr>
       <tr>
        <td>Password</td>
        <td><form:input path="password"/></td>
       </tr>
       <tr>
        <td></td>
        <td><button type="submit">Create</button></td>
       </tr>
      </table>
     </form:form>

</body>
</html>