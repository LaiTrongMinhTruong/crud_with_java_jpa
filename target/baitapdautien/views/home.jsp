<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang Chủ - Hệ Thống Video</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; margin: 0; background: #f4f6f9; color: #333; }
    .container { max-width: 1200px; margin: 30px auto; padding: 0 15px; }
    .welcome-card { background: #fff; padding: 25px 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 30px; }
    .section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 2px solid #0088cc; padding-bottom: 10px; }
    .section-title { font-size: 22px; font-weight: bold; color: #222; margin: 0; display: flex; align-items: center; gap: 10px; }
    .view-all-link { color: #0088cc; text-decoration: none; font-size: 14px; font-weight: 600; }
    .view-all-link:hover { text-decoration: underline; }
    
    /* Video Grid */
    .video-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 20px; }
    .video-card { background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.06); transition: transform .2s, box-shadow .2s; display: flex; flex-direction: column; }
    .video-card:hover { transform: translateY(-4px); box-shadow: 0 6px 18px rgba(0,0,0,0.12); }
    .video-thumb { position: relative; width: 100%; height: 140px; background: #000; overflow: hidden; }
    .video-thumb img { width: 100%; height: 100%; object-fit: cover; }
    .play-overlay { position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.3); display: flex; justify-content: center; align-items: center; opacity: 0; transition: opacity .2s; }
    .video-card:hover .play-overlay { opacity: 1; }
    .play-icon { font-size: 38px; color: #fff; text-shadow: 0 2px 4px rgba(0,0,0,0.5); }
    .video-info { padding: 12px 14px; flex: 1; display: flex; flex-direction: column; }
    .video-title { font-size: 15px; font-weight: 600; line-height: 1.4; margin-bottom: 8px; flex: 1; }
    .video-title a { color: #222; text-decoration: none; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
    .video-title a:hover { color: #0088cc; }
    .video-meta { font-size: 12px; color: #777; display: flex; justify-content: space-between; align-items: center; margin-top: 6px; }
    .video-author { font-size: 13px; color: #555; margin-bottom: 4px; display: flex; align-items: center; gap: 5px; }
    .badge { display: inline-block; padding: 3px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; background: #0088cc; color: #fff; }
</style>
</head>
<body>
    <jsp:include page="/views/topbar.jsp" />

    <div class="container">
        <c:if test="${sessionScope.account != null}">
            <div class="welcome-card">
                <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px;">
                    <div>
                        <h3 style="margin: 0 0 8px 0; color: #0088cc;">
                            <i class="fa fa-user-circle"></i> Xin chào, ${sessionScope.account.fullName}!
                        </h3>
                        <p style="margin: 0; color: #666; font-size: 14px;">
                            Tài khoản: <strong>${sessionScope.account.userName}</strong> | Vai trò: 
                            <span class="badge">
                                <c:choose>
                                    <c:when test="${sessionScope.account.roleid == 1}">Quản trị viên (Admin)</c:when>
                                    <c:when test="${sessionScope.account.roleid == 2}">Quản lý (Manager)</c:when>
                                    <c:otherwise>Người dùng (User)</c:otherwise>
                                </c:choose>
                            </span>
                        </p>
                    </div>
                    <div style="display: flex; gap: 10px;">
                        <a href="${pageContext.request.contextPath}/admin/video/add" style="background: #28a745; color: #fff; padding: 9px 15px; border-radius: 5px; text-decoration: none; font-size: 14px; font-weight: 600;">
                            <i class="fa fa-cloud-upload"></i> Đăng Video Mới (Cloudinary)
                        </a>
                        <a href="${pageContext.request.contextPath}/video" style="background: #0088cc; color: #fff; padding: 9px 15px; border-radius: 5px; text-decoration: none; font-size: 14px; font-weight: 600;">
                            <i class="fa fa-film"></i> Video Của Tôi
                        </a>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- KHỐI 10 VIDEO MỚI NHẤT TRÊN TRANG CHỦ -->
        <div class="section-header">
            <h2 class="section-title">
                <i class="fa fa-fire" style="color: #e65100;"></i> 10 Video Mới Nhất
            </h2>
            <a href="${pageContext.request.contextPath}/video?scope=all" class="view-all-link">
                Xem tất cả videos &rarr;
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty top10Videos}">
                <div class="video-grid">
                    <c:forEach items="${top10Videos}" var="v">
                        <div class="video-card">
                            <div class="video-thumb">
                                <img src="${v.poster}" alt="${v.title}" onerror="this.src='https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80';">
                                <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="play-overlay">
                                    <i class="fa fa-play-circle play-icon"></i>
                                </a>
                            </div>
                            <div class="video-info">
                                <div class="video-title">
                                    <!-- BẤM VÀO TIÊU ĐỀ ĐỂ PHÁT VIDEO THEO YÊU CẦU -->
                                    <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" title="${v.title}">
                                        ${v.title}
                                    </a>
                                </div>
                                <div class="video-author">
                                    <i class="fa fa-user-circle-o"></i> 
                                    <span>${v.user != null ? v.user.fullName : 'Hệ thống'}</span>
                                </div>
                                <div class="video-meta">
                                    <span><i class="fa fa-eye"></i> ${v.views} lượt xem</span>
                                    <span><i class="fa fa-calendar-o"></i> ${v.createdDate}</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div style="background: #fff; padding: 40px; text-align: center; border-radius: 8px; color: #777;">
                    <i class="fa fa-video-camera" style="font-size: 48px; margin-bottom: 15px; color: #ccc;"></i>
                    <p style="font-size: 16px;">Chưa có video nào trên hệ thống. Hãy là người đầu tiên đăng tải video!</p>
                    <a href="${pageContext.request.contextPath}/admin/video/add" style="display: inline-block; margin-top: 10px; background: #0088cc; color: #fff; padding: 10px 20px; border-radius: 5px; text-decoration: none; font-weight: 600;">
                        <i class="fa fa-plus"></i> Đăng Video Ngay
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
