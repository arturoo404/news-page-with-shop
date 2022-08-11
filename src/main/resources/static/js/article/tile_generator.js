window.onload = function() {
    tile_generator();
};

var page = 0;

function tile_generator(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const tag = urlParams.get('tag');

    function getData(){
        $.ajax({
            type: 'GET',
            url: '/api/article/tile?tag=' + tag + '&page=' + page,
            dataType: 'json',
            success: function (data) {
                tile(data);
            }
        });
    }

    function tile(data){
        for (let i = 0; i < data.content.length; i++){
            var myDiv = document.getElementById("tileCont");
            var div = document.createElement('div');
            div.id = 'tile' + i;
            div.className = 'tile';

            myDiv.appendChild(div);
            imgTile(i, data.content[i].id);
            titleTile(i, data.content[i]);
        }
    }

    function titleTile(id, data){
        var myDiv = document.getElementById("tile" + id);
        var div = document.createElement('div');
        div.id = 'titleTile' + id;
        div.className = 'titleTile';

        myDiv.appendChild(div);
        generateTitle(div, data.title);
        articleLink(div, data.id);
    }

    function generateTitle(divId, title){
        var myDiv = document.getElementById(divId.id);
        var h3 = document.createElement('h6');
        h3.textContent = title;
        h3.className = "h3Prop";

        myDiv.appendChild(h3);
    }

    function articleLink(divId, articleId){
        var myDiv = document.getElementById(divId.id);
        var anchorElement = document.createElement('a');
        const link = document.createTextNode("Read more");
        anchorElement.className = 'btn btn-outline-primary btn-lg mt-4';
        anchorElement.href = 'http://localhost:8080/article/detail?articleId=' + articleId;
        anchorElement.appendChild(link);

        myDiv.appendChild(anchorElement);
    }

    function imgTile(id, articleId){
        var myDiv = document.getElementById("tile" + id);
        var div = document.createElement('div');
        div.id = 'imgTile' + id;
        div.className = 'imgTile';

        myDiv.appendChild(div);
        imgGen(div, articleId);
    }

    function imgGen(divId, articleId){
        var myDiv = document.getElementById(divId.id);
        var img = document.createElement('img');

        img.src = 'http://localhost:8080/api/article/'+ articleId +'/photo';
        img.className = "imgProp";

        myDiv.appendChild(img);
    }
    getData();
}