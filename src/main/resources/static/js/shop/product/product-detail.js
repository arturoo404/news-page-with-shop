window.onload = function() {
    productListGenerator();
};
var id;

function productListGenerator() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    id = urlParams.get('id');

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/shop/product/detail?id=' + id,
            dataType: 'json',
            success: function (data) {
                createContent(data, id);;
            }
        });
    }
    getData();
}

function addToCart(){
    const email = document.getElementById('email').value;
    const quantity = document.getElementById('quantity').value;
    var err = document.getElementById('error-mess');
    var success = document.getElementById('success-mess');

    function add() {
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/shop/cart/add-product?email=' + email + '&quantity=' + quantity + '&productId=' + id,
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
            },
            error: function (xhr, status, error) {
            },
            success: function (data) {
            }
        });
    }

    function deleteSuccess(){
        success.innerText = '';
    }
    function deleteErr(){
        err.innerText = '';
    }

    add();
}



function createContent(data, id){
    var name = document.getElementById('product-name');
    var description = document.getElementById('description');
    var avaProduct = document.getElementById('available-product');
    var productPhoto = document.getElementById('product-photo');
    var goCart = document.getElementById('go-cart');
    name.innerText = data.name;
    description.innerText = data.description;
    avaProduct.innerText = 'Available product: ' + data.productQuantity;
    productPhoto.setAttribute('src', 'http://localhost:8080/api/shop/product/photo/get/' + id);
    goCart.setAttribute('href', 'http://localhost:8080/shop/cart');
    priceDiv(data.price, data.discountPrice);
}

function priceDiv(price, discPrice){
    if (discPrice < price){
        hrGenerator('price-content');
        pGenerator('normal-price', 'price-content', 'product-price-info', 'Normal price: ');
        pGenerator('normal-price', 'price-content', 'product-disc', price + '$');
        hrGenerator('price-content');
        pGenerator('normal-price', 'price-content', 'product-price', 'Promotion price: ' + discPrice + '$');
        hrGenerator('price-content');
        return;
    }
    hrGenerator('price-content');
    pGenerator('normal-price', 'price-content', 'product-price', 'Price: ' + price + '$');
    hrGenerator('price-content');
}