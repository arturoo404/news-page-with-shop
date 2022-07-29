function add_journalist() {

    const name = document.getElementById('name').value;
    const info = document.getElementById('info').value;

    function add(){
        request();
    }

    function request() {
        const userRegistrationDto = {
            "name": name,
            "info": info
        };

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/journalist/add',
            data: JSON.stringify(userRegistrationDto),
            error: function(xhr, status, error) {
                console.log(xhr);
            },
            success:function(data) {
                console.log(data);
            }
        });
    }

    add();
}