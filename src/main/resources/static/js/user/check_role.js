function checkRole() {
    const email = document.getElementById('check-email').value;

    if (email.toString().length > 0){
        getData();
    }else {
        document.getElementById('user-role').innerText = "Email filed is empty.";
    }

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/user/current-role?email=' + email,
            dataType: 'json',
            error: function (xhr, status, error){
                document.getElementById('user-role').innerText = xhr.responseText;
            },
            success: function (data) {
                document.getElementById('user-role').innerText = 'User role: ' + data.role;
            }
        });
    }
}