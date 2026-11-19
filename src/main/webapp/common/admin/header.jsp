<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3 shadow-sm">
    <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/admin/category/list">
        <i class="fa-solid fa-gauge-high text-primary me-2"></i>Admin Panel
    </a>
    <div class="ms-auto d-flex align-items-center gap-3 text-light small">
        <c:if test="${sessionScope.account != null}">
            <span>
                <i class="fa-solid fa-user-shield text-info me-1"></i>
                <strong>${sessionScope.account.fullName}</strong>
            </span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-danger">
                <i class="fa-solid fa-right-from-bracket me-1"></i>Đăng xuất
            </a>
        </c:if>
    </div>
</nav>
