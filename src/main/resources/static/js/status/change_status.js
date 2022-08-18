function change_status(articleId){

    request();
    function request(){
        $.ajax({
            type: 'POST',
            url: '/api/article/manage/status?articleId=' + articleId,
            error: function(xhr, status, error) {
                console.log(xhr);
            },
            success:function(data) {
                window.location.reload();
            }
        });
    }
}