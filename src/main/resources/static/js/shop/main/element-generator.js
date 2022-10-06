function divGenerator(name, mainObjID, classes){
    var mainDiv = document.getElementById(mainObjID);
    var div = document.createElement('div');
    div.id = name;
    div.className = classes;
    mainDiv.appendChild(div);
}

function aGenerator(name, mainObjID, classes, link){
    var mainDiv = document.getElementById(mainObjID);
    var a = document.createElement('a');
    a.id = name;
    a.className = classes;
    a.href = link;
    mainDiv.appendChild(a);
}

function imgGenerator(name, mainObjID, classes, link){
    var div = document.getElementById(mainObjID);
    var img = document.createElement('img');
    img.id = name;
    img.className = classes;
    img.src = link;
    div.appendChild(img);
}

function pGenerator(name, mainObjID, classes, text){
    var mainDiv = document.getElementById(mainObjID);
    var p = document.createElement('p');
    p.id = name;
    p.className = classes;
    p.innerText = text;
    mainDiv.appendChild(p);
}

function hrGenerator(mainObjID){
    var mainDiv = document.getElementById(mainObjID);
    var hr = document.createElement('hr');
    mainDiv.appendChild(hr);
}

function buttonGenerator(mainObjID, classes, text){
    var mainDiv = document.getElementById(mainObjID);
    var buttonElement = document.createElement('button');
    buttonElement.className = classes;
    buttonElement.innerHTML = text;
    mainDiv.appendChild(buttonElement);
}