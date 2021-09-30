<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

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

<style>
    body {
        background: #eee
    }

    .date {
        font-size: 11px
    }

    .comment-text {
        font-size: 14px
    }

    .fs-12 {
        font-size: 12px
    }

    .fs-16 {
        font-size: 16px
    }

    .shadow-none {
        box-shadow: none
    }

    .name {
        color: #007bff
    }

    .cursor:hover {
        color: blue
    }

    .cursor {
        cursor: pointer
    }

    .textarea {
        resize: none
    }

    .fa-facebook {
        color: #3b5999
    }

    .fa-twitter {
        color: #55acee
    }

    .fa-linkedin {
        color: #0077B5
    }

    .fa-instagram {
        color: #e4405f
    }

    .fa-dribbble {
        color: #ea4c89
    }

    .fa-pinterest {
        color: #bd081c
    }

    .fa {
        cursor: pointer
    }

    .n-reply {
        padding: 8px;
        margin-left: 24px;
        margin-bottom: 8px;
    }
</style>

<nav class="navbar navbar-expand-md bg-dark navbar-dark">
    <a class="navbar-brand" href="/">Yunjae's Blog</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
    </div>
</nav>
<br />

<security:authorize access="isAuthenticated()">
    authenticated as <security:authentication property="principal.userId" />
</security:authorize>

<div class="container-xl">
    <div>
        글번호: <span id="id"><i>${board.id}</i></span>
        작성자: <span><i>${board.user.userId}</i></span>
    </div>
    <div>principal: <span>${principal.userId}</span></div>
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

    <input type="hidden" id="boardId" value="${board.id}"/>

    <%-- 돌아가기 글 삭제, 수정 --%>

    <button class="btn btn-secondary" onclick="history.back()">돌아가기</button>
    <%-- <c:if test="${board.user.id == principal.user.id}"> --%>
    <button id="btn-delete" class="btn btn-danger">삭제</button>
    <a href="/board/${board.id}/updateForm" class="btn btn-warning">수정</a>
    <%-- </c:if> --%>
    <br/><br/>

    <%-- 댓글 작성 --%>

    <c:forEach var="reply" items="${rootReplies}" varStatus="rootStatus">
    <div class="d-flex flex-flow row">
        <div class="col-lg-10">
            <div class="d-flex flex-column comment-section mb-2" id="myGroup">
                <div class="bg-white p-2">
                    <div class="d-flex flex-row user-info">
                        <%--<div class="form-inline my-2">
                            <label for="replyId"></label>
                            <input type="text" class="form-control ml-2" placeholder="Id" id="replyId">
                            <label for="replyPassword"></label>
                            <input type="text" class="form-control ml-2" placeholder="Password" id="replyPassword">
                        </div>--%>
                        <span class="fs-16">${reply.user.userId}</span>
                            <%--<div class="d-flex flex-column justify-content-start ml-2"><span class="d-block font-weight-bold name">Marry Andrews</span><span class="date text-black-50">Shared publicly - Jan 2020</span></div>--%>
                    </div>
                    <div class="mt-2 mx-2">
                        <p class="comment-text">${reply.content}</p>
                    </div>
                </div>
                <div class="bg-light p-2">
                    <div class="d-flex fs-12">
                        <div class="like p-2 cursor action-collapse" data-toggle="collapse" aria-expanded="true" aria-controls="collapse-${rootStatus.count}" href="#collapse-${rootStatus.count}"><button class="btn btn-primary btn-sm ml-2">Reply</button></div>
                    </div>
                </div>

                <%-- Collapsible --%>
                <div id="collapse-${rootStatus.count}" class="bg-light p-2 collapse">
                        <%-- nested replies --%>
                    <c:forEach var="nestedReply" items="${nestedReplies}" varStatus="status">
                        <c:if test="${nestedReply.parentId eq reply.id}">
                            <div class="bg-white n-reply">
                                <div class="d-flex flex-row user-info">
                                    <span class="fs-16">${nestedReply.user.userId}</span>
                                        <%--<div class="d-flex flex-column justify-content-start ml-2"><span class="d-block font-weight-bold name">Marry Andrews</span><span class="date text-black-50">Shared publicly - Jan 2020</span></div>--%>
                                </div>
                                <div class="mt-2 mx-2">
                                    <p class="comment-text">${nestedReply.content}</p>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>

                        <%--nested reply post--%>
                        <%--principal--%>
                            <div class="d-flex flex-row user-info">
                                <div class="form-inline my-2">
                                    <label for="nestedReplyId-${reply.id}"></label>
                                    <input type="text" class="form-control ml-2" placeholder="Id" id="nestedReplyId-${reply.id}">
                                    <label for="nestedReplyPassword-${reply.id}"></label>
                                    <input type="text" class="form-control ml-2" placeholder="Password" id="nestedReplyPassword-${reply.id}">
                                </div>
                            </div>

                    <div class="d-flex flex-row align-items-start">
                        <label>
                            <textarea class="form-control ml-1 shadow-none textarea" id="nestedReplyContent-${reply.id}"></textarea>
                        </label>
                    </div>

                    <div class="mt-2 text-right">
                        <button class="btn btn-primary btn-sm shadow-none" id="btn-nested-reply-save-${reply.id}"
                                type="button" onclick="collapsibleComment.saveNestedReply(${reply.id})">Post comment
                        </button>
                        <button class="btn btn-outline-primary btn-sm ml-1 shadow-none" type="button">Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    </c:forEach>

    <script src="/js/collapsibleComment.js"></script>

</body>
