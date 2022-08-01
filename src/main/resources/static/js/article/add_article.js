function add_article() {

    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;

    function add() {
        request();
    }

    function request() {
        const userRegistrationDto = {
            "title": title,
            "content": content
        };

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/article/add',
            data: JSON.stringify(userRegistrationDto),
            error: function (xhr, status, error) {
                console.log(xhr);
            },
            success: function (data) {
                uploadFile(data.id);
            }
        });
    }
    add();
}