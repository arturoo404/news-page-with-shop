window.onload = function() {
    getJournalist();
};

function getJournalist() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('articleId');

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/article/photo/content/' + id,
            dataType: 'json',
            success: function (data) {
                if (data > 0){
                    const input = document.createElement("input");
                    input.type = "number";
                    input.min = "1";
                    input.defaultValue = "1";
                    input.max = data;
                    input.id = "photoNumber"
                    input.className = "css-class-name";
                    totalPhotoNumber.appendChild(input);
                }
            }
        });
    }
    getData();
}