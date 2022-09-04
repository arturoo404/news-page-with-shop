window.onload = function() {
    content_loader();
    generateComments();
};


function content_loader() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const article = urlParams.get('articleId');

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/article/content?articleId=' + article,
            dataType: 'json',
            error: function (xhr, status, error) {
                const errorElement = document.getElementById("error");
                errorElement.innerText = "Article not found.";
            },
            success: function (data) {
                contentGen(data);
            }
        });
    }

    function getTitle(articleId, anchor) {
        $.ajax({
            type: 'GET',
            dataType: 'json',
            url: '/api/article/title?articleId=' + articleId,
            success: function (data) {
                var a = document.getElementById(anchor);
                const text = document.createTextNode(data.title);

                a.appendChild(text);
            }
        });
    }

    function contentGen(data){
        divTitleGenerator(data, "title")
        let p = 0;
        for (let i = 0; i < data.contentDto.length; i++){
            switch (data.contentDto[i].contentType) {
                case 'TEXT':
                    divTextGenerator(data.contentDto[i].text, "text" + i);
                    break;
                case 'PHOTO':
                    divPhotoGenerator(data.articlePhotoPropertiesDto[p], "photo" + i);
                    p++;
                    break;
                case 'ARTICLE':
                    divArticleGenerator(data.contentDto[i].text, "article" + i);
                    break;
                case 'SUBTITLE':
                    divSubtitleGenerator(data.contentDto[i].text, "subtitle" + i);
                    break;
            }
        }
        hrGenerator("contentCont");
        divTagGenerator(data.tags);
    }


    function divTagGenerator(tags){
        var tagsDiv = document.getElementById("contentCont");
        var div = document.createElement('div');
        div.id = "tags";
        div.className = "inline";
        tagsDiv.appendChild(div);

        for (let i = 0; i < tags.length; i++){
            tagsLinkGenerator(tags[i]);
        }
        hrGenerator(div.id);
    }

    function tagsLinkGenerator(tag){
        const element = document.getElementById("tags");
        const a = document.createElement('a');
        const link = document.createTextNode("#" + tag);

        a.appendChild(link);
        a.style.fontSize = '16';
        a.style.textDecoration = "none";
        a.style.color = "black";
        a.style.marginRight = "10";

        a.href = "http://localhost:8080/article?tag=" + tag.toLocaleString().toLocaleLowerCase();

        element.prepend(a);
    }

    function divPhotoGenerator(photoProp, name){
        var myDiv = document.getElementById("contentCont");
        var div = document.createElement('div');
        div.id = name;
        div.className = "text-" + photoProp.photoPlace;
        myDiv.appendChild(div);
        imgGenerator(photoProp, div.id);
    }

    //TODO fix display img right

    function imgGenerator(photoProp, id){
        var imgDiv = document.getElementById(id);
        var img = document.createElement('img');

        img.src = 'http://localhost:8080/api/article/photo/inside/'+ photoProp.photoId;
        img.height = photoProp.photoHeight;
        img.width = photoProp.photoWidth;

        imgDiv.appendChild(img);
    }

    function divArticleGenerator(text, name){
        var myDiv = document.getElementById("contentCont");
        var div = document.createElement('div');
        div.id = name;

        myDiv.appendChild(div);

        spanGenerator(div.id, "Read more");
        hrGenerator(div.id);
        createArticleLink(text, div.id);
        hrGenerator(div.id);
    }

    function createArticleLink(articleId, divId) {
        const element = document.getElementById(divId);
        const a = document.createElement('a');

        a.id = "a" + divId;
        a.style.fontSize = '30';
        a.style.textDecoration = "none";
        a.style.color = "black";

        a.href = "http://localhost:8080/article/detail?articleId=" + articleId;

        element.appendChild(a);

        getTitle(articleId, a.id);
    }

    function divSubtitleGenerator(text, name){
        var myDiv = document.getElementById("contentCont");
        var div = document.createElement('div');
        div.id = name;
        div.className = 'text-left';

        myDiv.appendChild(div);

        var subTitle = document.getElementById(div.id);
        var h5 = document.createElement('h5');
        h5.textContent = text;
        h5.className = "mt-4 mb-4"

        subTitle.appendChild(h5);
    }

    function divTextGenerator(text, name){
        var myDiv = document.getElementById("contentCont");
        var div = document.createElement('div');
        div.id = name;
        div.className = 'text-left';

        myDiv.appendChild(div);

        pGenerator(div.id, text)
    }

    function spanGenerator(div, text){
        var spanDiv = document.getElementById(div);
        var spanElement = document.createElement('span');
        spanElement.className = "mt-2 mt-2";
        spanElement.textContent = text;

        spanDiv.appendChild(spanElement);
    }

    function hrGenerator(div){
        var hrDiv = document.getElementById(div);
        var hr = document.createElement('hr');
        hr.className = "mt-2 mb-2";

        hrDiv.appendChild(hr);
    }

    function pGenerator(div, text){
        var textDiv = document.getElementById(div);
        var p = document.createElement('p');
        p.textContent = text;
        p.className = "mt-4 text-left";

        textDiv.appendChild(p);
    }

    //TODO journalist link to description

    function divTitleGenerator(data, name){
        var myDiv = document.getElementById("contentCont");
        var div = document.createElement('div');
        div.id = name;

        myDiv.appendChild(div);

        var titleDiv = document.getElementById(div.id);
        var h3 = document.createElement('h3');
        h3.textContent = data.title;
        h3.className = "mt-4"

        spanGenerator(div.id, "Author: " + data.journalistGetDto.name)
        hrGenerator(div.id);
        titleDiv.appendChild(h3);
    }

    getData();
}