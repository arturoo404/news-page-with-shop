function registerAccount() {

    const email = document.getElementById('email').value;
    const nick = document.getElementById('nick').value;
    const password = document.getElementById('password').value;
    const passwordConfirm = document.getElementById('confirmPassword').value;

    function register(){
        request();
    }
    function request() {

        const userRegistrationDto = {
            "email": email,
            "nick": nick,
            "password": password,
            "passwordConfirm": passwordConfirm
        };

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/user/registration',
            data: JSON.stringify(userRegistrationDto),
            error: function(xhr, status, error) {
                document.getElementById('register_error').innerHTML = xhr.responseText;
            },
            success:function(data) {
                document.getElementById('register_error').innerHTML = "Successfully create account";
            }
            });
    }

    register();
}