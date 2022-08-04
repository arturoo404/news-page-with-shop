window.onload = function() {
    getJournalist();
};

function getJournalist() {
    getData();

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/journalist/list',
            dataType: 'json',
            success: function (data) {

                var myDiv = document.getElementById("journalist");
                var selectList = document.createElement("select");
                selectList.setAttribute("id", "journalistList");
                myDiv.appendChild(selectList);

                for (let i = 0; i < data.length; i++) {
                    var option = document.createElement("option");
                    option.setAttribute("value", data[i].id);
                    option.text = data[i].name;
                    selectList.appendChild(option);
                }
            }
        });
    }
}