window.onload = function() {
    weather();
};

const city = 'Krakow';

function weather() {

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/weather/current?city=' + city,
            dataType: 'json',
            error: function (xhr, status, error) {
            },
            success: function (data) {
                console.log(data);
                generateCurrentWeather(data);
            }
        });
    }
    getData()
}

function generateCurrentWeather(data){
    divGenerator('section', 'weather', 'divWeather text-white');
    divGenerator('weather', 'currentDate', 'divDate text-center');
    divGenerator('weather', 'currentTemp', 'divTemp text-center');
    divGenerator('weather', 'currentDes', 'divDes text-center');
    h3Generator('currentTemp', 'Temperature: ' + Math.round(data.main.temp));
    bTextGenerator('currentDate', city);
    bTextGenerator('currentDate', dateGenerator());
    bTextGenerator('currentDes', 'Description:  ' + data.weather[0].main);
}

function dateGenerator(){
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = mm + '/' + dd + '/' + yyyy;
    return today;
}

function bTextGenerator(divId, text){
    var bDiv = document.getElementById(divId);
    var bElement = document.createElement('b');
    bElement.className = "small mb-2 mt-2 ms-2";
    bElement.style.float = 'center';
    bElement.textContent = text;

    bDiv.appendChild(bElement);
}

function h3Generator(divId, text){
    var bDiv = document.getElementById(divId);
    var hElement = document.createElement('h3');
    hElement.style.float = 'center';
    hElement.textContent = text;

    bDiv.appendChild(hElement);
}


function divGenerator(mainDiv, newDivId, divClass){
    var myDiv = document.getElementById(mainDiv);
    var div = document.createElement('div');
    div.id = newDivId;
    div.className = divClass;

    myDiv.appendChild(div);
}