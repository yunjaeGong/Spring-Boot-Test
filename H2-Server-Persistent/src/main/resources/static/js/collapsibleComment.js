let collapsibleComment = {
    init: function () {
        $("#btn-post").on("click", ()=> {
            this.save();
        });
        $("#btn-reply-save").on("click", ()=> {
            this.saveReply();
        });
    },
    saveReply: function (userId, boardId, replyContent) {
        let data = {
            userId: $("#userId").val(),
            boardId: $("#boardId").val(),
            content: $("#replyContent").val(),
            parentId: 0,
            depth: 0,
            rootId: 0,
        };

        console.log(data);

        $.ajax({
            type: "POST",
            url: `/api/board/reply`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            data_type: "json"
        }).done(function (resp) {
            alert("댓글 쓰기가 완료되었습니다.");
            location.href = `/replyTest1`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    saveNestedReply: function (rootId) {
        let data = {
            userId: $("#nestedReplyId-" + rootId).val(),
            boardId: $("#boardId").val(),
            content: $("#nestedReplyContent-" + rootId).val(),
            parentId: rootId,
            depth: 1,
            rootId: rootId,
        };
        alert(data);
        console.log(data);


        $.ajax({
            type: "POST",
            url: `/api/board/nestedReply`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            data_type: "json"
        }).done(function (resp) {
            alert("대댓글 쓰기가 완료되었습니다.");
            location.href = `/replyTest1`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

};

collapsibleComment.init();