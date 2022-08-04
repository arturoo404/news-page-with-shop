function add_article() {

    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    const select = document.getElementById('journalistList');
    const value = select.options[select.selectedIndex].value;

    function add() {
        request();
    }

    function request() {
        const userRegistrationDto = {
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
            data: JSON.stringify(userRegistrationDto),
            error: function (xhr, status, error) {
                console.log(xhr);
            },
            success: function (data) {
                console.log(data.id);
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
    add();
}