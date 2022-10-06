window.onload = function() {
    cartDetail();
};

function cartDetail() {

    const email = document.getElementById('email').value;

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/shop/cart/detail/?email=' + email,
            dataType: 'json',
            success: function (data) {
                contentLoad(data);
                console.log(data);
            }
        });
    }

    getData();

    function contentLoad(data) {
        for (let i = 0; i < data.product.length; i++) {
            divGenerator('product' + i, 'load', 'content');
            imgGenerator('imgName' + i, 'product' + i, 'product-img', 'http://localhost:8080/api/shop/product/photo/get/' + data.product[i].id);
            divGenerator('product-name' + i, 'product' + i, '');
            aGenerator('contentLink' + i, 'product-name' + i, 'a-link', 'http://localhost:8080/shop/product/detail?id=' + data.product[i].id);
            pGenerator('productId' + i, 'contentLink' + i, 'product-name', data.product[i].name);
            divGenerator('product-right' + i, 'product' + i, '');
            divGenerator('product-delete' + i, 'product-right' + i, 'product-delete-section');
            buttonGeneratorDelete('product-delete' + i, 'product-delete', '&#128465', data.product[i].id, data.product[i].quantity);
            divGenerator('product-info' + i, 'product-right' + i, '');
            pGenerator('price-info' + i, 'product-info' + i, 'product-quantity', 'Quantity: ' + data.product[i].quantity);
            pGenerator('price-info' + i, 'product-info' + i, 'product-price', data.product[i].price + '$');
        }
        hrGenerator('load');
        divGenerator('total', 'load', 'content');
        divGenerator('left', 'total', '');
        divGenerator('center', 'total', 'text-center');
        divGenerator('right', 'total', '');
        pGenerator('quantity-total-info', 'right', 'product-quantity', 'Quantity: ' + data.quantity);
        pGenerator('price-total-info', 'right', 'product-price', data.amount + '$');
    }

    function buttonGeneratorDelete(mainObjID, classes, text, id, quantity){
        var mainDiv = document.getElementById(mainObjID);
        var buttonElement = document.createElement('button');
        buttonElement.className = classes;
        buttonElement.innerHTML = text;
        buttonElement.onclick = function(){
            deleteProduct(id, quantity);
        };
        mainDiv.appendChild(buttonElement);
    }

    //TODO select quantity to delete

    function deleteProduct(id, quantity) {
        $.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/shop/cart/delete-product?email=' + email + '&quantity=' + quantity + '&productId=' + id,
            statusCode: {
                400: function (response) {
                   alert(response.responseText);
                },
                200: function () {
                    document.location.reload(true);
                }
            }
        });
    }
}