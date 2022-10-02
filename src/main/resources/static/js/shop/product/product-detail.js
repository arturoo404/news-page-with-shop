window.onload = function() {
    productListGenerator();
};

function productListGenerator() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id');

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/shop/product/detail?id=' + id,
            dataType: 'json',
            success: function (data) {
                createContent(data, id);
                console.log(data);
            }
        });
    }
    getData();
}

function createContent(data, id){
    var name = document.getElementById('product-name');
    var description = document.getElementById('description');
    var avaProduct = document.getElementById('available-product');
    var productPhoto = document.getElementById('product-photo');
    name.innerText = data.name;
    description.innerText = data.description;
    avaProduct.innerText = 'Available product: ' + data.productQuantity;
    productPhoto.setAttribute('src', 'http://localhost:8080/api/shop/product/photo/get/' + id);
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