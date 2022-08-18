window.onload = function() {
    generateDivs()
};

//TODO Make page button

function generateDivs() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const page = urlParams.get('pageNumber');

    getData();

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/article/manage/list?pageNumber=' + page,
            dataType: 'json',
            success: function (data) {
                generate(data.content);
            }
        });
    }

    function generate(data) {
        hrGenerator('section');
        for (let i = 0; i < data.length; i++){
            console.log(data[i]);
            divGenerator('section', 'content' + i, 'text-center', '100%');
            divGenerator('content' + i, 'id' + i, '', '100%');
            bGenerator('id' + i, 'Article ID:');
            pGenerator('id' + i, data[i].articleId);
            divGenerator('content' + i, 'title' + i, '', '100%');
            bGenerator('title' + i, 'Title:');
            pGenerator('title' + i, data[i].title);
            divGenerator('content' + i, 'journalist' + i, '', '100%');
            bGenerator('journalist' + i, 'Journalist:');
            pGenerator('journalist' + i, data[i].journalist);
            divGenerator('content' + i, 'status' + i, '', '100%');
            bGenerator('status' + i, 'Status:');
            pGenerator('status' + i, data[i].status);
            divGenerator('content' + i, 'change_status' + i, '', '100%');
            bGenerator('change_status' + i, 'ChangeStatus:');
            statusButton('change_status' + i, data[i].articleId);

            hrGenerator('content' + i);
        }
    }

    function bGenerator(mainDiv, text){
        var myDiv = document.getElementById(mainDiv);
        var b = document.createElement('b');
        b.innerHTML = text;
        b.style.textAlign = 'center';

        myDiv.appendChild(b);
    }

    function pGenerator(mainDiv, text){
        var myDiv = document.getElementById(mainDiv);
        var p = document.createElement('p');
        p.innerText = text;
        p.style.textAlign = 'center';

        myDiv.appendChild(p);
    }

    function hrGenerator(div){
        var hrDiv = document.getElementById(div);
        var hr = document.createElement('hr');
        hr.className = "mt-2 mb-2";

        hrDiv.appendChild(hr);
    }

    function divGenerator(mainDiv, newDivId, divClass, width){
        var myDiv = document.getElementById(mainDiv);
        var div = document.createElement('div');
        div.id = newDivId;
        div.className = divClass;
        div.style.width = width;

        myDiv.appendChild(div);
    }

    function statusButton(divId, articleId){
        var myDiv = document.getElementById(divId);
        var buttonElement = document.createElement('button');
        buttonElement.innerHTML = 'Change status';
        buttonElement.className = 'btn btn-dark';
        buttonElement.style.marginLeft = '5px';
        buttonElement.onclick = function(){
            change_status(articleId);
        };
        myDiv.appendChild(buttonElement);
    }
}