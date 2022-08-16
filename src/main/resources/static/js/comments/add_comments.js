function add_comments(){
    const email = document.getElementById('email').value;
    const comments = document.getElementById('comments').value;
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const articleId = urlParams.get('articleId');

    request();
    refreshComments();
    generateComments();

    function refreshComments(){
        console.log("test");
        var element = document.getElementById('section');
        element.parentNode.removeChild(element);
    }

    function request() {
        const addArticle = {
            "email": email,
            "content": comments,
            "articleId" : articleId

        };

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/comments/add',
            data: JSON.stringify(addArticle),
            error: function (xhr, status, error) {
                console.log(xhr);
            },
            success: function () {
            }
        });
    }
}