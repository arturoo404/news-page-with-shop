function changeRole(){
    var radios = document.getElementsByName('role');
    const email = document.getElementById('change-email').value;
    var length = radios.length;
    var role;

    for (var i = 0; i < length; i++) {
        if (radios[i].checked) {
            role = radios[i].value;
        }
    }

    const roleObj = {
        "email": email,
        "role": role
    }

    getData();
    function getData() {
        $.ajax({
            type: 'PATCH',
            url: '/api/user/change-role',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(roleObj),
            error: function (xhr, status, error){
                document.getElementById('change-role-text').innerText = xhr.responseText;
                setTimeout(end, 2000);
            },
            success: function (data) {
                document.getElementById('change-role-text').innerText =  data.responseText;
                setTimeout(end, 2000);
            }
        });
    }

    function end(){
        document.getElementById('change-role-text').innerText = '';
    }
}

