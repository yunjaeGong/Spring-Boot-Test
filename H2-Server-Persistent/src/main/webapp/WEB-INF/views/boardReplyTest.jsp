<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Yunjae's Blog</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%-- Bootstrap4 CDN --%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <%-- summernote --%>
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>

</head>
<body>

<nav class="navbar navbar-expand-md bg-dark navbar-dark">
    <a class="navbar-brand" href="/">Yunjae's Blog</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <%--<c:choose>
            <c:when test="${empty principal}">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="/auth/loginForm">로그인</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/auth/joinForm">회원가입</a>
                    </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="/board/writeForm">글쓰기</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/user/updateForm">회원정보</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/logout">로그아웃</a>
                    </li>
                </ul>
            </c:otherwise>
        </c:choose>--%>

    </div>
</nav>
<br />

<style>
    /* reply system */
    ul.replies {
        margin: 0px;
        padding: 0px;
        list-style: none;
    }
    ul.replies li.reply {
        position: relative;
        margin-bottom: 15px;
        padding: 0px;
        z-index: 9;
    }
    ul.replies li.reply:before {
        content: '';
        width: 1px;
        height: 100%;
        background: #ebedf2;
        position: absolute;
        left: 15px;
        top: 15px;
    }
    ul.replies li.reply:last-child:before {
        display: none;
    }
    ul.replies li.reply .reply-wrapper {
        position: relative;
        z-index: 99;
    }
    ul.replies li.reply .card .card-header {
        position: relative;
        padding-top: 8px;
        padding-bottom: 8px;
        margin-bottom: 0;
        line-height: 20px;
        vertical-align: bottom;
    }
    ul.replies li.reply .card .card-header .username{
        font-size: 20px;
        font-weight: normal;
    }
    ul.replies li.reply .card .card-header img {
        width: 32px;
        background: #fff;
    }
    ul.replies li.reply .card .card-header .reply-date {
        font-size: 14px;
        color: #bbb;
        margin-left: 8px;
    }
    ul.replies li.reply .card .card-header .ribbon {
        width: 40px;
        height: 40px;
        overflow: hidden;
        position: absolute;
        top: -5px;
        left: -5px;
    }
    ul.replies li.reply .card .card-header .ribbon span {
        position: absolute;
        display: block;
        width: 58px;
        padding: 0px 0;
        background-color: #4680ff;
        box-shadow: 0 5px 10px rgba(0, 0, 0, 0.05);
        color: #fff;
        font-size: 8px;
        font-weight: 400;
        text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
        text-transform: uppercase;
        text-align: center;
        right: -5px;
        top: 10px;
        transform: rotate(-45deg);
        z-index: 9;
    }
    ul.replies li.reply .card .card-header .ribbon:before,
    ul.replies li.reply .card .card-header .ribbon:after {
        position: absolute;
        z-index: -1;
        content: '';
        display: block;
        border: 5px solid #2980b9;
        border-top-color: transparent;
        border-left-color: transparent;
    }
    ul.replies li.reply .card .card-header .ribbon:before {
        top: 0;
        right: 0;
    }
    ul.replies li.reply .card .card-header .ribbon:after {
        bottom: 0;
        left: 0;
    }
    ul.replies li.reply ul {
        padding-left: 30px;
        margin-top: 15px;
    }
    ul.replies li > ul > li:first-child > .reply-wrapper > .card > .card-header:after {
        content: '';
        width: 15px;
        height: 1px;
        background: #ebedf2;
        position: absolute;
        left: -15px;
        top: 50%;
        z-index: 3;
    }
    ul.replies li > ul > li:first-child > .reply-wrapper > .card > .card-header:before {
        content: '';
        width: 1px;
        height: 100%;
        background: #ebedf2;
        position: absolute;
        left: -16px;
        bottom: 50%;
        z-index: 1;
    }
</style>

<div class="container">
    <div>
        글번호: <span id="id"><i>${board.id}</i></span>
        작성자: <span><i>${board.user.username}</i></span>
    </div>
    <br/>

    <div>
        <h3>${board.title}</h3>
    </div>
    <br/>

    <div>
        <p><strong style="color: dimgray; font-family: Verdana; font-size: large">Content:</strong></p>
        <hr/>
        <div>${board.content}</div>
    </div>
    <br/><br/>


    <%-- 돌아가기 글 삭제, 수정 --%>

    <button class="btn btn-secondary" onclick="history.back()">돌아가기</button>
<%-- <c:if test="${board.user.id == principal.user.id}"> --%>
        <button id="btn-delete" class="btn btn-danger">삭제</button>
        <a href="/board/${board.id}/updateForm" class="btn btn-warning">수정</a>
<%-- </c:if> --%>
    <br/><br/>

    <%-- 댓글 작성 --%>

    <div>
        <div class="card mb-2">

            <input type="hidden" id="boardId" value="${board.id}"/>
            <input type="hidden" id="userId" value="${principal.user.id}"/>

            <div class="card-header d-flex align-items-center">
                <p class="d-flex align-items-center"><h5 style="color: dimgray;font-size: 16px;line-height: 1;margin: 0 10px;">Reply</h5></p>
            </div>
            <c:choose>
                <c:when test="${empty principal}">
                    <div class="form-inline my-2">
                        <label for="replyId"></label>
                        <input type="text" class="form-control ml-2" placeholder="Id" id="replyId">
                        <label for="replyPassword"></label>
                        <input type="text" class="form-control ml-2" placeholder="Password" id="replyPassword">
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="mx-2">
                        <a href="#"><h5>${principal.user.username}</h5></a>
                    </div>
                </c:otherwise>
            </c:choose>

            <div><textarea class="form-control px-2" id="replyContent" rows="3"></textarea></div>
            <div class="card-footer bg-white p-2">
                <button id="btn-reply" type="button" class="btn btn-secondary btn-sm">Register</button>
            </div>
        </div>


        <%-- https://github.com/ZsharE/threaded-comments-bootstrap --%>
        <%-- TODO: MIT License 표기 --%>
        <%-- replies --%>
        <%-- wrapper 안에 reply class들--%>
        <br/>
        <ul class="replies">
            <c:forEach var="reply" items="${board.replies}">
                <li id="reply-${reply.id}" class="reply">
                    <div class="reply-wrapper">
                        <div class="card">
                            <div class="card-header">
                                <a href="#" class="username">${reply.user.username}</a>

                                <span class="reply-date"><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium"  value="${reply.createDate}" /></span>
                            </div>
                            <div class="card-body">
                                <p class="card-text">${reply.content}</p>
                            </div>
                            <div class="card-footer bg-white p-2">
                                <button id="btn-nested-reply" type="button" class="btn btn-secondary btn-sm">Reply</button>
                                <small class="text-muted ml-2">Last updated 3 mins ago</small > <%--TODO: 업데이트된 시간{x mins ago, x days 7], x week, a year, ""}--%>
                                <span class="float-right">
                                    <button class="btn btn-danger btn-sm">삭제</button>
                                    <button class="btn btn-warning btn-sm">수정</button>
                                    <%--<c:choose><c:when test="${board.user.id == principal.user.id}">
                                        <button onclick="index.replyDelete(${board.id},${reply.id}" class="btn btn-danger btn-sm">삭제</button>
                                        <button onclick="index.replyDelete(${board.id},${reply.id}" class="btn btn-warning btn-sm">수정</button>
                                    </c:when></c:choose>--%>
                                </span>
                            </div>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>


<script src="/js/board.js"></script>
</body>

</html>