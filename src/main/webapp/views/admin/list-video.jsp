<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý danh sách Videos (CRUD)</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; margin: 0; background-color: #f4f6f9; color: #333; }
    .container { max-width: 1150px; margin: 30px auto; background: #fff; padding: 25px 30px; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
    .header-box { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #0088cc; padding-bottom: 15px; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
    h2 { margin: 0; color: #333; font-size: 22px; display: flex; align-items: center; gap: 10px; }
    .btn-add { background: #28a745; color: #fff; padding: 10px 18px; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 14px; display: inline-flex; align-items: center; gap: 6px; }
    .btn-add:hover { background: #218838; }
    
    .toolbar-box { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; flex-wrap: wrap; gap: 10px; }
    .search-form { display: flex; gap: 8px; align-items: center; }
    .search-input { padding: 9px 14px; border: 1px solid #d2d6de; border-radius: 5px; font-size: 14px; outline: none; width: 280px; }
    .search-input:focus { border-color: #0088cc; }
    .btn-search { background: #0088cc; color: #fff; border: none; padding: 9px 16px; border-radius: 5px; cursor: pointer; font-weight: 600; font-size: 14px; }
    .btn-search:hover { background: #0073ab; }

    table { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 14px; }
    th, td { border: 1px solid #dee2e6; padding: 12px 10px; text-align: center; vertical-align: middle; }
    th { background: #0088cc; color: #fff; font-weight: 600; }
    tr:nth-child(even) { background: #f8f9fa; }
    tr:hover { background: #f1f3f5; }
    .video-thumb { width: 110px; height: 70px; object-fit: cover; border-radius: 4px; border: 1px solid #ddd; background: #000; }
    .video-title-col { text-align: left; max-width: 280px; }
    .video-title-col a { color: #0088cc; font-weight: 600; text-decoration: none; }
    .video-title-col a:hover { text-decoration: underline; }
    .action-links a { text-decoration: none; font-weight: 600; margin: 0 4px; display: inline-block; padding: 4px 8px; border-radius: 4px; font-size: 13px; }
    .action-play { background: #17a2b8; color: #fff; }
    .action-edit { background: #007bff; color: #fff; }
    .action-delete { background: #dc3545; color: #fff; }
    .badge-status { display: inline-block; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
    .badge-active { background: #e8f5e9; color: #2e7d32; }
    .badge-inactive { background: #ffebee; color: #c62828; }
</style>
</head>
<body>
<jsp:include page="/views/topbar.jsp" />

<div class="container">
    <div class="header-box">
        <h2><i class="fa fa-video-camera" style="color: #0088cc;"></i> Quản Lý Danh Sách Videos (CRUD)</h2>
        <a href="<c:url value='/admin/video/add'/>" class="btn-add">
            <i class="fa fa-cloud-upload"></i> Thêm Video Mới (Cloudinary)
        </a>
    </div>

    <div class="toolbar-box">
        <form action="<c:url value='/admin/video/list'/>" method="get" class="search-form">
            <input type="text" name="keyword" value="${keyword}" placeholder="Tìm kiếm theo tiêu đề hoặc mô tả..." class="search-input">
            <button type="submit" class="btn-search"><i class="fa fa-search"></i> Tìm kiếm</button>
            <c:if test="${not empty keyword}">
                <a href="<c:url value='/admin/video/list'/>" style="color: #666; font-size: 13px; text-decoration: underline;">Xóa bộ lọc</a>
            </c:if>
        </form>
        <div style="font-size: 13px; color: #666;">
            Tổng cộng: <strong>${videoList.size()}</strong> video
        </div>
    </div>

    <table>
        <thead>
            <tr>
                <th style="width: 50px;">STT</th>
                <th style="width: 125px;">Poster</th>
                <th>Tiêu đề &amp; Mô tả</th>
                <th style="width: 140px;">Người đăng</th>
                <th style="width: 80px;">Lượt xem</th>
                <th style="width: 100px;">Trạng thái</th>
                <th style="width: 105px;">Ngày tạo</th>
                <th style="width: 190px;">Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty videoList}">
                    <c:forEach items="${videoList}" var="v" varStatus="stt">
                        <tr>
                            <td>${stt.index + 1}</td>
                            <td>
                                <img src="${v.poster}" alt="Poster" class="video-thumb" onerror="this.src='https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80';">
                            </td>
                            <td class="video-title-col">
                                <!-- BẤM VÀO TIÊU ĐỀ ĐỂ PHÁT VIDEO -->
                                <a href="<c:url value='/video/detail?id=${v.videoId}'/>" title="Xem &amp; Phát video">
                                    ${v.title}
                                </a>
                                <div style="font-size: 12px; color: #777; margin-top: 4px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                    ${v.description}
                                </div>
                            </td>
                            <td>
                                <strong>${v.user != null ? v.user.fullName : 'Hệ thống'}</strong>
                                <div style="font-size: 12px; color: #888;">@${v.user != null ? v.user.userName : 'admin'}</div>
                            </td>
                            <td><strong>${v.views}</strong></td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.active}">
                                        <span class="badge-status badge-active">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge-status badge-inactive">Tạm ẩn</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${v.createdDate}</td>
                            <td class="action-links">
                                <a href="<c:url value='/video/detail?id=${v.videoId}'/>" class="action-play" title="Phát video">
                                    <i class="fa fa-play"></i> Xem
                                </a>
                                <a href="<c:url value='/admin/video/edit?id=${v.videoId}'/>" class="action-edit" title="Sửa thông tin">
                                    <i class="fa fa-pencil"></i> Sửa
                                </a>
                                <a href="<c:url value='/admin/video/delete?id=${v.videoId}'/>" class="action-delete" title="Xóa video" onclick="return confirm('Bạn có chắc chắn muốn xóa video này?');">
                                    <i class="fa fa-trash"></i> Xóa
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="8" style="padding: 30px; color: #777;">
                            Chưa có video nào. Bấm nút <strong>Thêm Video Mới</strong> ở trên để tải lên video đầu tiên!
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>
</body>
</html>
