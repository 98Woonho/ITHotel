function editComment(button) {
    const commentId = button.getAttribute('data-comment_id');
    const boardId = button.getAttribute('data-board_id');
    const commentElement = document.getElementById('comment-' + commentId);
    const commentContent = commentElement.textContent;

    const newContent = prompt('답변을 수정하겠습니까?', commentContent);
    if (newContent) {
        $.ajax({
            type: 'POST',
            url: `/admin/${boardId}/comment/${commentId}/update`,
            data: { content: newContent },
            success: function (data) {
                // 서버로부터의 응답을 처리할 코드
                window.location.href = data; // 예시: 리다이렉션
            },
            error: function (error) {
                // 에러 처리 코드
                console.error(error);
            }
        });
    }
}