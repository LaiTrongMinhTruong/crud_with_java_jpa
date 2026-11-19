<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${video.title} - Phát Video</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; margin: 0; background: #f4f6f9; color: #333; }
    .container { max-width: 1200px; margin: 30px auto; padding: 0 15px; }
    
    .detail-layout { display: grid; grid-template-columns: 2.3fr 1fr; gap: 30px; }
    @media (max-width: 900px) {
        .detail-layout { grid-template-columns: 1fr; }
    }

    /* Player Container */
    .player-wrapper { background: #000; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.15); width: 100%; aspect-ratio: 16 / 9; max-height: 540px; display: flex; align-items: center; justify-content: center; }
    .video-player { width: 100%; height: 100%; object-fit: contain; }
    .iframe-player { width: 100%; height: 100%; border: none; }

    /* Video Details */
    .video-info-box { background: #fff; padding: 25px; border-radius: 10px; margin-top: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
    .video-main-title { font-size: 24px; font-weight: bold; color: #111; margin-top: 0; margin-bottom: 15px; line-height: 1.3; }
    .meta-bar { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px; border-bottom: 1px solid #eee; padding-bottom: 15px; margin-bottom: 18px; }
    .stats-group { font-size: 14px; color: #666; display: flex; gap: 18px; align-items: center; }
    .stats-group span { display: flex; align-items: center; gap: 6px; }
    
    .author-section { display: flex; align-items: center; gap: 14px; margin-bottom: 20px; }
    .author-avatar { width: 48px; height: 48px; border-radius: 50%; background: #0088cc; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 22px; font-weight: bold; }
    .author-name { font-size: 16px; font-weight: 600; color: #222; }
    .author-role { font-size: 12px; color: #888; }
    
    .description-box { background: #f8f9fa; padding: 18px 20px; border-radius: 8px; font-size: 14px; line-height: 1.6; color: #444; white-space: pre-line; }

    /* Related Sidebar */
    .sidebar-card { background: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
    .sidebar-title { font-size: 18px; font-weight: bold; margin-top: 0; margin-bottom: 18px; border-bottom: 2px solid #0088cc; padding-bottom: 8px; color: #222; }
    .related-list { display: flex; flex-direction: column; gap: 16px; }
    .related-item { display: flex; gap: 12px; text-decoration: none; color: inherit; transition: opacity .2s; }
    .related-item:hover { opacity: 0.85; }
    .related-thumb { width: 120px; height: 75px; border-radius: 6px; overflow: hidden; background: #000; flex-shrink: 0; position: relative; }
    .related-thumb img { width: 100%; height: 100%; object-fit: cover; }
    .related-info { flex: 1; display: flex; flex-direction: column; justify-content: center; }
    .related-title { font-size: 13px; font-weight: 600; color: #222; line-height: 1.3; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; margin-bottom: 4px; }
    .related-meta { font-size: 11px; color: #777; }
</style>
</head>
<body>

    <div class="container">
        <!-- Đường dẫn Breadcrumb -->
        <div style="font-size: 13px; color: #666; margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/home" style="color: #0088cc; text-decoration: none;">Trang chủ</a>
            &nbsp;&gt;&nbsp;
            <a href="${pageContext.request.contextPath}/video" style="color: #0088cc; text-decoration: none;">Videos</a>
            &nbsp;&gt;&nbsp;
            <span>${video.title}</span>
        </div>

        <div class="detail-layout">
            <!-- CỘT TRÁI: VIDEO PLAYER & THÔNG TIN CHI TIẾT -->
            <div>
                <div class="player-wrapper">
                    <c:choose>
                        <%-- Xử lý nếu là link YouTube dạng watch?v=... hoặc youtu.be/... --%>
                        <c:when test="${fn:contains(video.url, 'youtube.com') || fn:contains(video.url, 'youtu.be')}">
                            <c:set var="ytUrl" value="${video.url}" />
                            <c:if test="${fn:contains(ytUrl, 'watch?v=')}">
                                <c:set var="ytUrl" value="${fn:replace(ytUrl, 'watch?v=', 'embed/')}" />
                            </c:if>
                            <c:if test="${fn:contains(ytUrl, 'youtu.be/')}">
                                <c:set var="ytUrl" value="${fn:replace(ytUrl, 'youtu.be/', 'www.youtube.com/embed/')}" />
                            </c:if>
                            <iframe src="${ytUrl}?autoplay=1" class="iframe-player" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                        </c:when>

                        <%-- Mặc định phát video HTML5 (hỗ trợ Cloudinary MP4 hoặc video tải lên server) --%>
                        <c:otherwise>
                            <video controls autoplay playsinline class="video-player" poster="${video.poster}" src="${video.url}">
                                <source src="${video.url}" type="video/mp4">
                                <source src="${video.url}" type="video/quicktime">
                                <source src="${video.url}" type="video/webm">
                                Trình duyệt của bạn không hỗ trợ phát định dạng video HTML5 này.
                            </video>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="video-info-box">
                    <h1 class="video-main-title">${video.title}</h1>

                    <div class="meta-bar">
                        <div class="stats-group">
                            <span><i class="fa fa-eye" style="color: #0088cc;"></i> <strong>${video.views}</strong> lượt xem</span>
                            <span><i class="fa fa-calendar-check-o"></i> Đăng ngày: ${video.createdDate}</span>
                            <c:if test="${video.active}">
                                <span style="color: #28a745;"><i class="fa fa-check-circle"></i> Đang hoạt động</span>
                            </c:if>
                        </div>
                        <div>
                            <a href="${pageContext.request.contextPath}/admin/video/edit?id=${video.videoId}" style="color: #0088cc; font-size: 13px; text-decoration: none; font-weight: 600;">
                                <i class="fa fa-pencil"></i> Chỉnh sửa video này
                            </a>
                        </div>
                    </div>

                    <div class="author-section">
                        <div class="author-avatar">
                            ${fn:substring(video.user != null ? video.user.fullName : 'U', 0, 1)}
                        </div>
                        <div>
                            <div class="author-name">${video.user != null ? video.user.fullName : 'Hệ thống'}</div>
                            <div class="author-role">Tác giả (${video.user != null ? video.user.userName : 'admin'})</div>
                        </div>
                    </div>

                    <h4 style="margin: 0 0 8px 0; color: #333;">Mô tả video:</h4>
                    <div class="description-box">
                        <c:choose>
                            <c:when test="${not empty video.description}">
                                ${video.description}
                            </c:when>
                            <c:otherwise>
                                <em>Chưa có mô tả chi tiết cho video này.</em>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- CỘT PHẢI: VIDEO ĐỀ XUẤT -->
            <div>
                <div class="sidebar-card">
                    <h3 class="sidebar-title">
                        <i class="fa fa-list-ul" style="color: #0088cc;"></i> Video Đề Xuất Khác
                    </h3>
                    <div class="related-list">
                        <c:forEach items="${relatedVideos}" var="rv">
                            <c:if test="${rv.videoId != video.videoId}">
                                <a href="${pageContext.request.contextPath}/video/detail?id=${rv.videoId}" class="related-item">
                                    <div class="related-thumb">
                                        <img src="${rv.poster}" alt="${rv.title}" onerror="this.src='https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80';">
                                    </div>
                                    <div class="related-info">
                                        <div class="related-title" title="${rv.title}">${rv.title}</div>
                                        <div class="related-meta">
                                            <span><i class="fa fa-eye"></i> ${rv.views} lượt xem</span>
                                        </div>
                                    </div>
                                </a>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
