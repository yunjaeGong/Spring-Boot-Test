let collapsibleComment = {
    init: function () {
        $("#btn-post").on("click", ()=> {
            this.save();
        });
        $("#btn-reply-save").on("click", ()=> {
            this.saveReply();
        });
        $("#btn-nested-reply-save").on("click", ()=> {
            this.saveNestedReply();
        });

    },
    saveReply: function () {
        let data = {
            userId: $("#userId").val(),
            boardId: $("#boardId").val(),
            content: $("#replyContent").val(),
            parentId: $("#id").val(),
            //  nested의 경우 root id필요
        };

        console.log(data);

        $.ajax({
            type: "POST",
            url: `/api/board/${data.boardId}/reply`,
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
    saveNestedReply: function () {
        let data = {
            userId: $("#userId").val(),
            boardId: $("#boardId").val(),
            content: $("#replyContent").val(),
            parentId: $("#id").val(),
            //  nested의 경우 root id필요
        };

        console.log(data);

        $.ajax({
            type: "POST",
            url: `/api/board/${data.boardId}/reply`,
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

};

collapsibleComment.init();