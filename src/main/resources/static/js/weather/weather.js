var city;
var error;
var lon;
var lat;
var home;

function getLocation(){
    getPermission();
}

function homePage(){
    home = true;
    getPermission();
}

function findWeather(){
    var element = document.getElementById('weather');
    if (element != null){
        element.parentNode.removeChild(element);
    }
    var section = document.getElementById('forecast');
    if (section != null){
        section.parentNode.removeChild(section);
    }

    city = document.getElementById('city').value;
    weather();
    forecast();
}

 function getPermission() {
     navigator.permissions.query({name:'geolocation'}).then(function(result) {
         if (result.state === 'granted') {
             getPosition();
         }else {
             city = 'Warsaw';
             weather();
             if (!home){
                 forecast();
             }
         }
     });
}

function getPosition(){
    navigator.geolocation.getCurrentPosition(getCor);
}

function getCor(position){
    lon = position.coords.longitude;
    lat = position.coords.latitude;
    getCity();
}

function getCity() {
    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/geolocation/city?lat=' + lat + '&lon='+ lon,
            dataType: 'json',
            error: function (xhr, status, error) {
                city = 'warsaw';
                weather();
                if (!home){
                    forecast();
                }
            },
            success: function (data) {
                city = data[0].name;
                weather();
                if (!home){
                    forecast();
                }
            }
        });
    }
    getData()
}

function weather() {
    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/weather/current?city=' + city,
            dataType: 'json',
            error: function (xhr, status, error) {
                console.log(xhr.responseText)
            },
            success: function (data) {
                generateCurrentWeather(data);
            }
        });
    }
    getData()
}

function forecast() {

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/weather/forecast?city=' + city,
            dataType: 'json',
            error: function (xhr, status, error) {
                window.alert(xhr.responseText);
            },
            success: function (data) {
                generateLongTermForcast(data);
            }
        });
    }
    getData()
}

function generateLongTermForcast(data){

    divGenerator('forecast-section', 'forecast', 'divWeather text-white');
    for (let i = 0; i < data.list.length; i++){
        hrGenerator('forecast');
        divGenerator('forecast', 'weather' + i, 'divWeather text-white');
        divGenerator('weather' + i, 'currentDate' + i, 'divDate text-center mt-2');
        divGenerator('weather' + i, 'currentTemp' + i, 'divTemp text-center');
        divGenerator('weather' + i, 'currentDes' + i, 'divDes text-center');
        bTextGenerator('currentDate' + i, data.list[i].dt_txt);
        bTextGenerator('currentTemp' + i, 'Temperature: ' + Math.round(data.list[i].main.temp));
        bTextGenerator('currentDes' + i, 'Conditions: ' + data.list[i].weather[0].main);
    }
}

function generateCurrentWeather(data){
    divGenerator('section', 'weather', 'divWeather text-white');
    divGenerator('weather', 'currentDate', 'divDate text-center');
    divGenerator('weather', 'currentTemp', 'divTemp text-center');
    divGenerator('weather', 'currentDes', 'divDes text-center');
    h3Generator('currentTemp', 'Temperature: ' + Math.round(data.main.temp));
    bTextGenerator('currentDate', city);
    bTextGenerator('currentDate', dateGenerator());
    bTextGenerator('currentDes', 'Conditions:  ' + data.weather[0].main);
}

function dateGenerator(){
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = mm + '/' + dd + '/' + yyyy;
    return today;
}

function hrGenerator(div){
    var hrDiv = document.getElementById(div);
    var hr = document.createElement('hr');
    hr.className = "mt-2 mb-2";

    hrDiv.appendChild(hr);
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