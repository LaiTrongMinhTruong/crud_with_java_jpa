<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Danh Sách Video - Phân Trang 6 Video / Trang</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; margin: 0; background: #f4f6f9; color: #333; }
    .container { max-width: 1200px; margin: 30px auto; padding: 0 15px; }
    
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; flex-wrap: wrap; gap: 15px; background: #fff; padding: 20px 25px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
    .page-title { margin: 0; font-size: 22px; font-weight: bold; color: #222; display: flex; align-items: center; gap: 10px; }
    
    .nav-tabs-custom { display: flex; gap: 10px; align-items: center; }
    .nav-btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border-radius: 6px; text-decoration: none; font-size: 14px; font-weight: 600; border: 1px solid #d2d6de; background: #fff; color: #555; transition: all .2s; }
    .nav-btn.active { background: #0088cc; color: #fff; border-color: #0088cc; }
    .nav-btn:hover:not(.active) { background: #f8f9fa; border-color: #0088cc; color: #0088cc; }
    .btn-add { background: #28a745; color: #fff; border-color: #28a745; }
    .btn-add:hover { background: #218838; }

    /* Grid 6 videos / trang */
    .video-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 24px; margin-bottom: 35px; }
    .video-card { background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.06); transition: transform .2s, box-shadow .2s; display: flex; flex-direction: column; }
    .video-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.12); }
    
    .video-thumb { position: relative; width: 100%; height: 190px; background: #111; overflow: hidden; }
    .video-thumb img { width: 100%; height: 100%; object-fit: cover; }
    .play-overlay { position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.3); display: flex; justify-content: center; align-items: center; opacity: 0; transition: opacity .2s; }
    .video-card:hover .play-overlay { opacity: 1; }
    .play-icon { font-size: 48px; color: #fff; text-shadow: 0 2px 6px rgba(0,0,0,0.6); }

    .video-info { padding: 16px 18px; flex: 1; display: flex; flex-direction: column; }
    .video-title { font-size: 16px; font-weight: 600; line-height: 1.4; margin-bottom: 8px; flex: 1; }
    .video-title a { color: #1a1a1a; text-decoration: none; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
    .video-title a:hover { color: #0088cc; }
    .video-desc { font-size: 13px; color: #666; margin-bottom: 12px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; line-height: 1.4; }
    .video-author { font-size: 13px; color: #444; margin-bottom: 6px; display: flex; align-items: center; gap: 6px; }
    .video-meta { font-size: 12px; color: #888; display: flex; justify-content: space-between; align-items: center; border-top: 1px solid #f0f0f0; padding-top: 10px; margin-top: auto; }

    /* Pagination */
    .pagination-container { display: flex; justify-content: center; align-items: center; gap: 8px; margin-top: 30px; }
    .page-link { display: inline-flex; align-items: center; justify-content: center; min-width: 40px; height: 40px; padding: 0 14px; border: 1px solid #d2d6de; background: #fff; color: #444; text-decoration: none; border-radius: 6px; font-weight: 600; font-size: 14px; transition: all .2s; }
    .page-link:hover { border-color: #0088cc; color: #0088cc; background: #f0f7fc; }
    .page-link.active { background: #0088cc; border-color: #0088cc; color: #fff; }
    .page-link.disabled { color: #ccc; pointer-events: none; border-color: #eee; }
    .page-info { text-align: center; font-size: 13px; color: #777; margin-top: 10px; }
</style>
</head>
<body>

    <div class="container">
        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fa fa-film" style="color: #0088cc;"></i>
                    <c:choose>
                        <c:when test="${targetUserId != null}">
                            Danh Sách Video Của Bạn (Phân trang 6sp/trang)
                        </c:when>
                        <c:otherwise>
                            Khám Phá Tất Cả Videos (Phân trang 6sp/trang)
                        </c:otherwise>
                    </c:choose>
                </h1>
                <div style="font-size: 13px; color: #777; margin-top: 4px;">
                    Tổng số: <strong>${totalItems}</strong> video | Hiển thị <strong>6 sản phẩm / trang</strong>
                </div>
            </div>

            <div class="nav-tabs-custom">
                <c:if test="${sessionScope.account != null}">
                    <a href="${pageContext.request.contextPath}/video" class="nav-btn ${targetUserId != null ? 'active' : ''}">
                        <i class="fa fa-user"></i> Video của tôi
                    </a>
                </c:if>
                <a href="${pageContext.request.contextPath}/video?scope=all" class="nav-btn ${targetUserId == null ? 'active' : ''}">
                    <i class="fa fa-globe"></i> Tất cả videos
                </a>
                <a href="${pageContext.request.contextPath}/admin/video/add" class="nav-btn btn-add">
                    <i class="fa fa-plus-circle"></i> Đăng Video Mới
                </a>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty videoList}">
                <div class="video-grid">
                    <c:forEach items="${videoList}" var="v">
                        <div class="video-card">
                            <div class="video-thumb">
                                <img src="${v.poster}" alt="${v.title}" onerror="this.src='https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80';">
                                <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="play-overlay">
                                    <i class="fa fa-play-circle play-icon"></i>
                                </a>
                            </div>
                            <div class="video-info">
                                <div class="video-title">
                                    <!-- BẤM VÀO TIÊU ĐỀ ĐỂ XEM CHI TIẾT VÀ PLAY VIDEO THEO YÊU CẦU -->
                                    <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" title="${v.title}">
                                        ${v.title}
                                    </a>
                                </div>
                                <c:if test="${not empty v.description}">
                                    <div class="video-desc">${v.description}</div>
                                </c:if>
                                <div class="video-author">
                                    <i class="fa fa-user-circle-o"></i>
                                    <span>Tác giả: <strong>${v.user != null ? v.user.fullName : 'Hệ thống'}</strong></span>
                                </div>
                                <div class="video-meta">
                                    <span><i class="fa fa-eye"></i> ${v.views} lượt xem</span>
                                    <span><i class="fa fa-calendar-o"></i> ${v.createdDate}</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- PHÂN TRANG CHUẨN 6 SP / TRANG -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination-container">
                        <!-- Nút Trang trước -->
                        <c:url var="prevUrl" value="/video">
                            <c:param name="page" value="${currentPage - 1}" />
                            <c:if test="${scope != null}"><c:param name="scope" value="${scope}" /></c:if>
                            <c:if test="${targetUserId != null && scope != 'all'}"><c:param name="userId" value="${targetUserId}" /></c:if>
                        </c:url>
                        <a href="${prevUrl}" class="page-link ${currentPage <= 1 ? 'disabled' : ''}">
                            <i class="fa fa-chevron-left"></i>
                        </a>

                        <!-- Các trang số -->
                        <c:forEach begin="1" end="${totalPages}" var="p">
                            <c:url var="pageUrl" value="/video">
                                <c:param name="page" value="${p}" />
                                <c:if test="${scope != null}"><c:param name="scope" value="${scope}" /></c:if>
                                <c:if test="${targetUserId != null && scope != 'all'}"><c:param name="userId" value="${targetUserId}" /></c:if>
                            </c:url>
                            <a href="${pageUrl}" class="page-link ${p == currentPage ? 'active' : ''}">
                                ${p}
                            </a>
                        </c:forEach>

                        <!-- Nút Trang sau -->
                        <c:url var="nextUrl" value="/video">
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:if test="${scope != null}"><c:param name="scope" value="${scope}" /></c:if>
                            <c:if test="${targetUserId != null && scope != 'all'}"><c:param name="userId" value="${targetUserId}" /></c:if>
                        </c:url>
                        <a href="${nextUrl}" class="page-link ${currentPage >= totalPages ? 'disabled' : ''}">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                    <div class="page-info">
                        Trang <strong>${currentPage}</strong> trên tổng số <strong>${totalPages}</strong> trang (6 videos / trang)
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <div style="background: #fff; padding: 50px 20px; text-align: center; border-radius: 8px; color: #777; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                    <i class="fa fa-video-camera" style="font-size: 54px; margin-bottom: 15px; color: #bbb;"></i>
                    <h3 style="color: #444; margin-top: 0;">Chưa tìm thấy video nào!</h3>
                    <p style="font-size: 14px; max-width: 480px; margin: 0 auto 20px auto;">
                        <c:choose>
                            <c:when test="${targetUserId != null}">
                                Bạn chưa đăng tải video nào lên hệ thống. Hãy tải lên video đầu tiên của mình qua Cloudinary ngay hôm nay!
                            </c:when>
                            <c:otherwise>
                                Hệ thống hiện chưa có video nào được đăng tải.
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <a href="${pageContext.request.contextPath}/admin/video/add" style="display: inline-block; background: #0088cc; color: #fff; padding: 11px 22px; border-radius: 6px; text-decoration: none; font-weight: 600;">
                        <i class="fa fa-cloud-upload"></i> Đăng Video Mới Ngay
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
