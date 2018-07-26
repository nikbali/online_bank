<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
<title>Registration</title>
</head>
<body>
<h1>Registration</h1>
    <form:form action="/save" method="POST" modelAttribute="user">
      <form:hidden path="id"/>
      <table>
       <tr>
           <td>E-mail</td>
           <td><form:input path="email"/></td>
       </tr>
       <tr>
          <td>Login</td>
          <td><form:input path="login"/></td>
      </tr>
      <tr>
        <td>First name</td>
        <td><form:input path="first_name"/></td>
      </tr>

       <tr>
        <td>Last Name</td>
        <td><form:input path="last_name"/></td>
       </tr>
       <tr>
        <td>Phone</td>
        <td><form:input path="phone"/></td>
       </tr>
       <tr>
           <td>Password</td>
           <td><form:input type="password" path="password"/></td>
      </tr>
       <tr>
        <td></td>
        <td><button type="submit">Create</button></td>
       </tr>
      </table>
     </form:form>

</body>
</html>