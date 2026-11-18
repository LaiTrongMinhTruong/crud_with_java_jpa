<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="topbar-container" style="background: #24292e; color: #fff; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 4px rgba(0,0,0,0.1); flex-wrap: wrap; gap: 15px;">
    <div class="brand" style="font-size: 15px; font-weight: bold; display: flex; gap: 20px; align-items: center; flex-wrap: wrap;">
        <a href="${pageContext.request.contextPath}/home" style="color: #fff; text-decoration: none; display: flex; align-items: center; gap: 6px;">
            <i class="fa fa-home"></i> Trang Chủ
        </a>
        <a href="${pageContext.request.contextPath}/video" style="color: #58a6ff; text-decoration: none; font-size: 14px; display: flex; align-items: center; gap: 6px;">
            <i class="fa fa-play-circle"></i> Videos (/video)
        </a>
        <a href="${pageContext.request.contextPath}/admin/video/list" style="color: #79c0ff; text-decoration: none; font-size: 14px; display: flex; align-items: center; gap: 6px;">
            <i class="fa fa-video-camera"></i> Quản lý Videos (CRUD)
        </a>
        <a href="${pageContext.request.contextPath}/admin/category/list" style="color: #a5d6ff; text-decoration: none; font-size: 14px; display: flex; align-items: center; gap: 6px;">
            <i class="fa fa-list"></i> Quản lý Danh mục
        </a>
    </div>
    <div>
        <c:choose>
            <c:when test="${sessionScope.account == null}">
                <ul class="list-inline right-topbar pull-right" style="list-style: none; display: flex; gap: 15px; margin: 0; padding: 0; align-items: center;">
                    <li>
                        <a href="${pageContext.request.contextPath}/login" style="color: #58a6ff; text-decoration: none;"><i class="fa fa-sign-in"></i> Đăng nhập</a>
                        | <a href="${pageContext.request.contextPath}/register" style="color: #58a6ff; text-decoration: none;"><i class="fa fa-user-plus"></i> Đăng ký</a>
                    </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul class="list-inline right-topbar pull-right" style="list-style: none; display: flex; gap: 15px; margin: 0; padding: 0; align-items: center;">
                    <li>
                        Xin chào, <span style="color: #58a6ff; font-weight: bold;">${sessionScope.account.fullName}</span>
                        | <a href="${pageContext.request.contextPath}/logout" style="color: #ff7b72; text-decoration: none;"><i class="fa fa-sign-out"></i> Đăng Xuất</a>
                    </li>
                </ul>
            </c:otherwise>
        </c:choose>
    </div>
</div>
