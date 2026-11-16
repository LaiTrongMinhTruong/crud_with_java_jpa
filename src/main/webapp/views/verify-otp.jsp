<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Xác nhận mã OTP kích hoạt tài khoản</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; background-color: #f4f6f9; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; padding: 20px 0; }
    .verify-box { background: #fff; padding: 35px 30px; border-radius: 8px; box-shadow: 0 4px 16px rgba(0,0,0,0.08); width: 100%; max-width: 440px; text-align: center; }
    h2 { color: #333; margin-top: 0; margin-bottom: 12px; font-size: 22px; font-weight: 600; }
    p.subtitle { color: #666; font-size: 14px; margin-bottom: 25px; line-height: 1.5; }
    .alert { padding: 12px 15px; border-radius: 6px; margin-bottom: 20px; font-size: 14px; text-align: left; }
    .alert-danger { color: #a94442; background-color: #f2dede; border: 1px solid #ebccd1; }
    .alert-success { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; }
    .alert-info { color: #0c5460; background-color: #d1ecf1; border: 1px solid #bee5eb; }
    .input-group { display: flex; align-items: center; margin-bottom: 18px; border: 1px solid #d2d6de; border-radius: 6px; overflow: hidden; background: #fff; transition: border-color .2s; }
    .input-group:focus-within { border-color: #0088cc; box-shadow: 0 0 0 3px rgba(0,136,204,0.15); }
    .input-group-addon { padding: 12px 15px; background: #f8f9fa; color: #888; border-right: 1px solid #d2d6de; width: 46px; text-align: center; }
    .form-control { flex: 1; border: none; padding: 12px 14px; outline: none; font-size: 14px; color: #333; }
    .otp-input { letter-spacing: 6px; font-size: 20px; font-weight: bold; text-align: center; }
    .btn-submit { width: 100%; background: #0088cc; color: #fff; border: none; padding: 13px; border-radius: 6px; font-size: 15px; cursor: pointer; font-weight: 600; margin-top: 5px; transition: background .2s; }
    .btn-submit:hover { background: #0073ab; }
    .resend-box { margin-top: 25px; font-size: 13px; color: #666; }
    .resend-box a { color: #0088cc; text-decoration: none; font-weight: 600; }
    .resend-box a:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="verify-box">
    <div style="font-size: 48px; color: #0088cc; margin-bottom: 10px;">
        <i class="fa fa-envelope-open-o"></i>
    </div>
    <h2>Xác Nhận Kích Hoạt Tài Khoản</h2>
    <p class="subtitle">Hệ thống đã gửi một mã xác nhận OTP gồm 6 chữ số tới địa chỉ email của bạn. Vui lòng nhập mã để kích hoạt tài khoản.</p>

    <c:if test="${alert != null}">
        <div class="alert alert-danger"><i class="fa fa-exclamation-circle"></i> ${alert}</div>
    </c:if>

    <c:if test="${message != null}">
        <div class="alert alert-success"><i class="fa fa-check-circle"></i> ${message}</div>
    </c:if>

    <c:if test="${sessionScope.demoOtp != null}">
        <div class="alert alert-info" style="font-size: 13px;">
            <i class="fa fa-info-circle"></i> <strong>Hỗ trợ kiểm thử nhanh:</strong> Mã OTP vừa tạo là <strong style="font-size: 16px; color: #0088cc;">${sessionScope.demoOtp}</strong> (đã được ghi nhận vào email).
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/verify-otp" method="post">
        <div class="input-group">
            <span class="input-group-addon"><i class="fa fa-envelope"></i></span>
            <input type="email" name="email" value="${email}" placeholder="Địa chỉ email đã đăng ký" class="form-control" required>
        </div>

        <div class="input-group">
            <span class="input-group-addon"><i class="fa fa-key"></i></span>
            <input type="text" name="otp" placeholder="Nhập mã OTP (6 số)" maxlength="6" class="form-control otp-input" required autofocus autocomplete="off">
        </div>

        <button type="submit" class="btn-submit"><i class="fa fa-check"></i> Xác Nhận Kích Hoạt</button>
    </form>

    <div class="resend-box">
        Bạn chưa nhận được mã hoặc mã đã hết hạn? 
        <br><br>
        <a href="${pageContext.request.contextPath}/resend-otp?email=${email}"><i class="fa fa-refresh"></i> Gửi lại mã OTP</a>
        &nbsp;|&nbsp;
        <a href="${pageContext.request.contextPath}/login">Quay lại Đăng nhập</a>
    </div>
</div>
</body>
</html>
