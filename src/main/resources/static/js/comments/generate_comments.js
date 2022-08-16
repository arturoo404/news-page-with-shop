function generateComments() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const articleId = urlParams.get('articleId');

    getData();
    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/comments/list?articleId=' + articleId,
            dataType: 'json',
            success: function (data) {
                createComments(data);
            }
        });
    }

    function createComments(data){
        for (let i = 0; i < data.length; i++){
            divGenerator('comments_section', 'section', '');
            divGenerator('section', 'card' + i, 'card mb-4');
            divGenerator('card' + i, 'body' + i, 'card-body');
            divGenerator('body' + i, 'nick' + i, 'mb-4');
            imgGenerator('nick' + i);
            bTextGenerator('nick' + i, data[i].nick);
            divGenerator('body' + i, 'content' + i, 'mb-4');
            pGenerator('content' + i, data[i].content, 'big mb-0 ms-2 mt-4 text-left');
            divGenerator('body' + i, 'date' + i, 'mb-4');
            pGenerator('date' + i, data[i].date, 'mt-2 small');
        }
    }


    function divGenerator(mainDiv, newDivId, divClass){
        var myDiv = document.getElementById(mainDiv);
        var div = document.createElement('div');
        div.id = newDivId;
        div.className = divClass;

        myDiv.appendChild(div);
    }

    function imgGenerator(divId){
        var imgDiv = document.getElementById(divId);
        var img = document.createElement('img');

        img.src = 'https://e7.pngegg.com/pngimages/605/198/png-clipart-computer-icons-avatar-avatar-web-design-heroes.png';
        img.height = 40;
        img.width = 40;
        img.style.float = 'left';

        imgDiv.appendChild(img);
    }

    function bTextGenerator(divId, text){
        var bDiv = document.getElementById(divId);
        var bElement = document.createElement('b');
        bElement.className = "small mb-4 mt-2 ms-2";
        bElement.style.float = 'left';
        bElement.textContent = text;

        bDiv.appendChild(bElement);
    }

    function pGenerator(divId, text, pClass){
        var textDiv = document.getElementById(divId);
        var p = document.createElement('p');
        p.textContent = text;
        p.className = pClass;

        textDiv.appendChild(p);
    }
}