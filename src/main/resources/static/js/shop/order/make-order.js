function makeOrder(){
    const firstName = document.getElementById('first-name').value;
    const lastName = document.getElementById('last-name').value;
    const phoneNumber = document.getElementById('phone-number').value;
    const comments = document.getElementById('comments').value;
    const city = document.getElementById('city').value;
    const postcode = document.getElementById('postcode').value;
    const street = document.getElementById('street').value;
    const homeNumber = document.getElementById('home-number').value;
    const email = document.getElementById('email').value;
    var err = document.getElementById('error-mess');
    var success = document.getElementById('success-mess');

    function request() {
        const makeOrderObj = {
            "firstName": firstName,
            "lastName": lastName,
            "city": city,
            "postcode": postcode,
            "street": street,
            "homeNumber": homeNumber,
            "phoneNumber": phoneNumber,
            "email": email,
            "commentsToOrder": comments
        };

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/shop/order/make',
            data: JSON.stringify(makeOrderObj),
            statusCode: {
                400: function (response) {
                    console.log(response);
                    err.innerText = response.responseText;
                    setTimeout(deleteErr, 2000);
                },
                200: function (response) {
                    success.innerText = response.responseText;
                    setTimeout(deleteSuccess, 2000);
                }
            }
        });
    }

    request()

    function deleteSuccess(){
        success.innerText = '';
    }
    function deleteErr(){
        err.innerText = '';
    }
}