//TODO add photo status
function add_article_photo() {

    const photoPosition = document.getElementById('photoNumber').value;
    const photoHeight = document.getElementById('photo_height').value;
    const photoWidth = document.getElementById('photo_width').value;
    const radioPos = document.getElementsByName('pos');
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('articleId');

    function request() {
        const articlePhoto = {
            "articleId": id,
            "photoPosition": photoPosition,
            "photoWidth": photoWidth,
            "photoHeight": photoHeight,
            "photoPlace": photoPlace()
        };

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/article/photo/parameter',
            data: JSON.stringify(articlePhoto),
            error: function(xhr, status, error) {
                console.log(xhr);
            },
            success:function(data) {
            }
        });
    }

    function photoPlace(){
        for(let i = 0; i < radioPos.length; i++) {
            if(radioPos[i].checked)
                return radioPos[i].value;
        }
        return "right";
    }

    request();
}