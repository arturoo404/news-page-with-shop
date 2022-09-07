function randomArticle() {
    var tag;

    function randomIntFromInterval(min, max) {
        return Math.floor(Math.random() * (max - min + 1) + min)
    }

    const rndInt = randomIntFromInterval(1, 7);

    switch (rndInt) {
        case 1:
            tag = 'country';
            break;
        case 2:
            tag = 'world';
            break;
        case 3:
            tag = 'politics'
            break;
        case 4:
            tag = 'tech'
            break;
        case 5:
            tag = 'sports'
            break;
        case 6:
            tag = 'science'
            break;
        default:
            tag = 'news'
            break;
    }

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/article/last-published/' + tag,
            dataType: 'json',
            success: function (data) {
                generateRandomArticle(data);
            }
        });
    }
    getData()
}

function popularityArticle() {
    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/article/popularity',
            dataType: 'json',
            success: function (data) {
                generatePopularityArticle(data);
            }
        });
    }
    getData()
}
function lastArticle() {
    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/article/last-published',
            dataType: 'json',
            success: function (data) {
                generateLastArticle(data);
            }
        });
    }
    getData()
}

function newsArticle() {
    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/article/last-published/news',
            dataType: 'json',
            success: function (data) {
                generateNewsArticle(data);
            }
        });
    }
    getData()
}

function generatePopularityArticle(data){
    for (let i = 0; i < data.length; i++){
        divGenerator('popularity-article-section', 'popularity-article' + i, 'divArticleMain');
        imgGen('popularity-article' + i, data[i].id);
        articleLink('popularity-article' + i, data[i].id, data[i].title);
    }
}
function generateRandomArticle(data){
    for (let i = 0; i < data.length; i++){
        divGenerator('random-article-section', 'random-article' + i, 'divArticleMain');
        imgGen('random-article' + i, data[i].id);
        articleLink('random-article' + i, data[i].id, data[i].title);
    }
}

function generateNewsArticle(data){
    for (let i = 0; i < data.length; i++){
        divGenerator('news-article-section', 'news-article' + i, 'divArticleMain');
        imgGen('news-article' + i, data[i].id);
        articleLink('news-article' + i, data[i].id, data[i].title);
    }
}

function generateLastArticle(data){
    for (let i = 0; i < data.length; i++){
        divGenerator('last-article-section', 'last-article' + i, 'divArticleMain');
        imgGen('last-article' + i, data[i].id);
        articleLink('last-article' + i, data[i].id, data[i].title);
    }
}

function divGenerator(mainDiv, newDivId, divClass){
    var myDiv = document.getElementById(mainDiv);
    var div = document.createElement('div');
    div.id = newDivId;
    div.className = divClass;

    myDiv.appendChild(div);
}

function imgGen(divId, articleId){
    var myDiv = document.getElementById(divId);
    var img = document.createElement('img');

    img.src = 'http://localhost:8080/api/article/'+ articleId +'/photo';
    img.className = 'mt-2 imgHomePage';
    myDiv.appendChild(img);
}

function articleLink(divId, articleId, text){
    var myDiv = document.getElementById(divId);
    var anchorElement = document.createElement('a');
    const link = document.createTextNode(text);
    anchorElement.className = 'mt-4 text-white';
    anchorElement.href = 'http://localhost:8080/article/detail?articleId=' + articleId;
    anchorElement.appendChild(link);
    myDiv.appendChild(anchorElement);
}
