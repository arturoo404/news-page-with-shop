function add_article() {

    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    const select = document.getElementById('journalistList');
    const value = select.options[select.selectedIndex].value;
    const errorDiv = document.getElementById('errors');

    function add() {
        if( document.getElementById('article_photo').files.length === 0 ){
            error_h6(errorDiv, 'You do not chose photo.')
        }else {
            request();
        }
    }

    //TODO add article description

    function request() {
        const addArticle = {
            "title": title,
            "content": content,
            "tags": tags(),
            "journalist": value
        };

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/article/add',
            data: JSON.stringify(addArticle),
            error: function (xhr, status, error) {
                error(errorDiv, xhr);
            },
            success: function (data) {
                uploadFile(data.id);
            }
        });
    }

    function tags(){
        const checkboxTag = document.getElementsByName('tag');

        var tagList = [];

        for (let i = 0; i < checkboxTag.length; i++) {
            const checkbox1 = checkboxTag[i];
            if (checkbox1.checked){
                tagList.push(checkbox1.value);
                console.log(checkbox1.value);
            }
        }
        return tagList;
    }

    async function uploadFile(id) {
        let formData = new FormData();
        formData.append("file", article_photo.files[0]);
        let response = await fetch('/api/article/photo/add/' + id, {
            method: "POST",
            body: formData
        });
        if (response.ok){
            createArticleEditLink(id);
            createSuccessText();
        }
    }

    function createArticleEditLink(id) {
        const element = document.getElementById("editArticle");
        const a = document.createElement('a');
        const link = document.createTextNode("Add photo for article");

        a.appendChild(link);
        a.className = 'btn btn-primary btn-lg';

        a.href = "http://localhost:8080/article/photo?articleId=" + id;

        element.prepend(a);
    }
    function createSuccessText() {
        const element = document.getElementById("editArticle");
        const h = document.createElement('h3');

        h.innerHTML = "Successfully create article!";

        element.prepend(h);
    }
    add();
}