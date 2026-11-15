<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang chủ</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; margin: 0; background: #f8f9fa; }
    .content { max-width: 800px; margin: 40px auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
    h1 { color: #333; margin-top: 0; }
    .user-info { background: #e9ecef; padding: 20px; border-radius: 6px; margin-top: 20px; }
    .user-info p { margin: 8px 0; font-size: 15px; }
    .badge { display: inline-block; padding: 4px 10px; border-radius: 4px; font-size: 12px; font-weight: bold; background: #0088cc; color: #fff; }
</style>
</head>
<body>
    <jsp:include page="/views/topbar.jsp" />

    <div class="content">
        <c:choose>
            <c:when test="${sessionScope.account != null}">
                <div class="user-info">
                    <h3>Thông tin tài khoản đăng nhập:</h3>
                    <p><strong>Họ và tên:</strong> ${sessionScope.account.fullName}</p>
                    <p><strong>Tài khoản:</strong> ${sessionScope.account.userName}</p>
                    <p><strong>Email:</strong> ${sessionScope.account.email}</p>
                    <p><strong>Số điện thoại:</strong> ${sessionScope.account.phone}</p>
                    <p><strong>Mã vai trò (RoleId):</strong> <span class="badge">${sessionScope.account.roleid}</span>
                        <c:choose>
                            <c:when test="${sessionScope.account.roleid == 1}">(Admin)</c:when>
                            <c:when test="${sessionScope.account.roleid == 2}">(Manager)</c:when>
                            <c:otherwise>(User)</c:otherwise>
                        </c:choose>
                    </p>
                    <div style="margin-top: 15px;">
                        <a href="${pageContext.request.contextPath}/admin/category/list" style="display: inline-block; background: #0088cc; color: #fff; padding: 10px 18px; border-radius: 4px; text-decoration: none; font-weight: bold;"><i class="fa fa-th-list"></i> Đi tới Quản lý Danh mục (CRUD Category)</a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <p>Bạn chưa đăng nhập. Vui lòng bấm vào <strong>Đăng nhập</strong> hoặc <strong>Đăng ký</strong> trên thanh menu trên.</p>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
