<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng nhập vào hệ thống</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; background-color: #f4f6f9; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; }
    .login-box { background: #fff; padding: 35px 30px; border-radius: 8px; box-shadow: 0 4px 16px rgba(0,0,0,0.08); width: 100%; max-width: 400px; }
    h2 { text-align: center; color: #555; margin-top: 0; margin-bottom: 25px; font-size: 22px; font-weight: 500; }
    .alert { padding: 12px 15px; border-radius: 4px; margin-bottom: 20px; font-size: 14px; text-align: center; }
    .alert-danger { color: #a94442; background-color: #f2dede; border: 1px solid #ebccd1; }
    .alert-success { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; }
    .alert-warning { color: #856404; background-color: #fff3cd; border: 1px solid #ffeeba; }
    section { margin-bottom: 10px; }
    .input-group { display: flex; align-items: center; margin-bottom: 15px; border: 1px solid #d2d6de; border-radius: 4px; overflow: hidden; background: #fff; transition: border-color .2s; }
    .input-group:focus-within { border-color: #0088cc; }
    .input-group-addon { padding: 10px 14px; background: #f8f9fa; color: #888; border-right: 1px solid #d2d6de; width: 42px; text-align: center; }
    .form-control { flex: 1; border: none; padding: 12px 14px; outline: none; font-size: 14px; color: #333; }
    .row-options { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; font-size: 13px; color: #666; }
    .row-options label { cursor: pointer; display: flex; align-items: center; gap: 6px; }
    .btn-submit { width: 100%; background: #0088cc; color: #fff; border: none; padding: 12px; border-radius: 4px; font-size: 15px; cursor: pointer; font-weight: 600; transition: background .2s; }
    .btn-submit:hover { background: #0073ab; }
    .bottom-note { text-align: center; margin-top: 25px; font-size: 13px; color: #777; }
    .bottom-note a, .row-options a { color: #0088cc; text-decoration: none; }
    .bottom-note a:hover, .row-options a:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="login-box">
    <h2>Đăng Nhập Vào Hệ Thống</h2>
    
    <c:if test="${successMessage != null}">
        <div class="alert alert-success">
            <i class="fa fa-check-circle"></i> ${successMessage}
        </div>
    </c:if>

    <c:if test="${alert != null}">
        <div class="alert alert-danger">
            <i class="fa fa-exclamation-circle"></i> ${alert}
        </div>
    </c:if>

    <c:if test="${unactivatedEmail != null}">
        <div class="alert alert-warning" style="text-align: left; font-size: 13px;">
            <i class="fa fa-shield"></i> Tài khoản của bạn cần xác thực để kích hoạt:
            <br>
            <a href="${pageContext.request.contextPath}/verify-otp?email=${unactivatedEmail}" style="display: block; margin-top: 8px; font-weight: bold; color: #856404; text-decoration: underline;">
                &rarr; Nhập mã OTP kích hoạt tài khoản
            </a>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post" class="needs-validation" novalidate>
        <section>
            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text"><i class="fa fa-user"></i></span>
                    <input type="text" placeholder="Tài khoản hoặc Email" name="username" value="${username}" class="form-control" required minlength="3">
                    <div class="invalid-feedback">Vui lòng nhập tài khoản hoặc email (tối thiểu 3 ký tự).</div>
                </div>
            </div>

            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text"><i class="fa fa-lock"></i></span>
                    <input type="password" placeholder="Mật khẩu" name="password" class="form-control" required minlength="3">
                    <div class="invalid-feedback">Vui lòng nhập mật khẩu (tối thiểu 3 ký tự).</div>
                </div>
            </div>

            <div class="row-options">
                <label>
                    <input type="checkbox" name="remember"> Nhớ tôi
                </label>
                <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
            </div>

            <button type="submit" class="btn-submit">Đăng nhập</button>
        </section>
    </form>

    <div class="bottom-note">
        Nếu bạn chưa có tài khoản trên hệ thống, thì hãy <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
    </div>
</div>
</body>
</html>
