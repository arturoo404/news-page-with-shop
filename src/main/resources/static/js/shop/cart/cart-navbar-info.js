window.addEventListener("load",function(event) {
    cartNavbarInfo();
},false);


function cartNavbarInfo(){
    const email = document.getElementById('email-nav').value;

    function getData() {
        console.log("data")
        $.ajax({
            type: 'GET',
            url: '/api/shop/cart/nav-info?email=' + email,
            dataType: 'json',
            success: function (data) {
               var cart = document.getElementById('cart-info');
               cart.innerHTML = '&#128722 Cart(' + data.quantity + ') = ' + data.amount + '$';
            }, error: function (xhr, status, error) {
                console.log(xhr.responseText);
            }
        });
    }
    getData();
}