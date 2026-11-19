<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<div class="topbar-strip py-1 border-bottom border-secondary border-opacity-25">
    <div class="container d-flex justify-content-between align-items-center flex-wrap gap-2">
        <div>
            <i class="fa-solid fa-video text-primary me-1"></i>
            <span>Hệ Thống Quản Lý Video &amp; Danh Mục</span>
        </div>
        <div>
            <c:choose>
                <c:when test="${sessionScope.account == null}">
                    <a href="${pageContext.request.contextPath}/login" class="me-3">
                        <i class="fa-solid fa-right-to-bracket me-1"></i>Đăng nhập
                    </a>
                    <a href="${pageContext.request.contextPath}/register">
                        <i class="fa-solid fa-user-plus me-1"></i>Đăng ký
                    </a>
                </c:when>
                <c:otherwise>
                    <span class="me-3">
                        Xin chào, <strong class="text-info">${sessionScope.account.fullName}</strong>
                    </span>
                    <a href="${pageContext.request.contextPath}/logout" class="text-danger">
                        <i class="fa-solid fa-right-from-bracket me-1"></i>Đăng xuất
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
