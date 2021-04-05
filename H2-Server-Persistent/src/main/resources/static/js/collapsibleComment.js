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
            password: $("#nestedReplyPassword-" + rootId).val(),
            boardId: $("#boardId").val(),
            content: $("#nestedReplyContent-" + rootId).val(),
            parentId: rootId,
            depth: 1,
            rootId: rootId,
        };

        let form = $('<form></form>');
        form.attr("method", "post");
        form.attr("action", path);
        let parameters = {username : data.userId, password: data.password}
        let field = $('<input></input>');

        field.attr("type", "hidden");
        field.attr("name", key);
        field.attr("value", value);
        /*$.each(parameters, function(key, value) {
            let field = $('<input></input>');

            field.attr("type", "hidden");
            field.attr("name", key);
            field.attr("value", value);

            form.append(field);
        });*/

        console.log(data);

        $.ajax({
            type: "POST",
            url: `/auth/login`,
            data: JSON.stringify({username: data.userId, password: data.password}),
            contentType: "application/json; charset=utf-8",
            data_type: "json"
        }).done(function (resp) {
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
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

};

collapsibleComment.init();