//TODO add photo status
function add_article_photo() {

    const photoPosition = document.getElementById('photoNumber').value;
    const photoHeight = document.getElementById('photo_height').value;
    const photoWidth = document.getElementById('photo_width').value;
    const radioPos = document.getElementsByName('pos');
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('articleId');
    const info = document.getElementById('info');

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
                error_h6(info, xhr, 'd');
            },
            success:function(data) {
                uploadPhoto(id, photoPosition)
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

    async function uploadPhoto(articleId, pos) {
        let formData = new FormData();
        formData.append("file", article_photo.files[0]);
        let response = await fetch('/api/article/photo/inside?articleId=' + articleId + "&position=" + pos, {
            method: "POST",
            body: formData
        });
        if (response.ok){
            error_h6(info, await response.text(), 's');
        }
    }

    request();
}