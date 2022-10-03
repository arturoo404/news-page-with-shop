window.onload = function() {
    cartNavbarInfo();
};

function cartNavbarInfo(){
    const email = document.getElementById('email').value;

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/shop/cart/nav-info?email=' + email,
            dataType: 'json',
            success: function (data) {
               var cart = document.getElementById('cart-info');
               cart.innerHTML = '&#128722 Cart(' + data.quantity + ') = ' + data.amount + '$';
            }
        });
    }
    getData();
}