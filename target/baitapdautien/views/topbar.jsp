<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="topbar-container" style="background: #24292e; color: #fff; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
    <div class="brand" style="font-size: 16px; font-weight: bold; display: flex; gap: 20px; align-items: center;">
        <a href="${pageContext.request.contextPath}/home" style="color: #fff; text-decoration: none;">Trang Chủ</a>
        <a href="${pageContext.request.contextPath}/admin/category/list" style="color: #58a6ff; text-decoration: none; font-size: 14px;"><i class="fa fa-list"></i> Quản lý danh mục (CRUD)</a>
    </div>
    <div class="col-sm-6">
        <c:choose>
            <c:when test="${sessionScope.account == null}">
                <ul class="list-inline right-topbar pull-right" style="list-style: none; display: flex; gap: 15px; margin: 0; padding: 0; align-items: center;">
                    <li>
                        <a href="${pageContext.request.contextPath}/login" style="color: #58a6ff; text-decoration: none;">Đăng nhập</a>
                        | <a href="${pageContext.request.contextPath}/register" style="color: #58a6ff; text-decoration: none;">Đăng ký</a>
                    </li>
                    <li><i class="search fa fa-search search-button" style="cursor: pointer;"></i></li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul class="list-inline right-topbar pull-right" style="list-style: none; display: flex; gap: 15px; margin: 0; padding: 0; align-items: center;">
                    <li>
                        Xin chào, <a href="${pageContext.request.contextPath}/member/myaccount" style="color: #58a6ff; font-weight: bold; text-decoration: none;">${sessionScope.account.fullName}</a>
                        | <a href="${pageContext.request.contextPath}/logout" style="color: #ff7b72; text-decoration: none;">Đăng Xuất</a>
                    </li>
                    <li><i class="search fa fa-search search-button" style="cursor: pointer;"></i></li>
                </ul>
            </c:otherwise>
        </c:choose>
    </div>
</div>
