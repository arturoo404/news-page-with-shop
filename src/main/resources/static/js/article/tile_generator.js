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
                console.log(data.content);
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
            titleTile(i, data.content[i].title);
        }
    }

    function titleTile(id, title){
        var myDiv = document.getElementById("tile" + id);
        var div = document.createElement('div');
        div.id = 'titleTile' + id;
        div.className = 'titleTile';

        myDiv.appendChild(div);
        generateTitle(div, title);
    }

    function generateTitle(divId, title){
        var myDiv = document.getElementById(divId.id);
        var h3 = document.createElement('h3');
        h3.textContent = title;
        h3.className = "h3Prop";

        myDiv.appendChild(h3);
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