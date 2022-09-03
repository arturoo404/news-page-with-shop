function changePassword() {

    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const oldPassword = document.getElementById('oldPassword').value;
    const email = document.getElementById('email').value;

    function register(){
        request();
    }
    function request() {

        const changePassword = {
            "newPassword": newPassword,
            "confirmPassword": confirmPassword,
            "oldPassword": oldPassword,
            "account": email
        };

        $.ajax({
            type: 'PATCH',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/user/change-password',
            data: JSON.stringify(changePassword),
            error: function(xhr, status, error) {
                document.getElementById('error').innerHTML = xhr.responseText;
            },
            success:function(data) {
                document.getElementById('success').innerHTML = "Successfully change password";
            }
        });
    }

    register();
}