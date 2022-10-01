window.onload = function() {
    productListGenerator();
};

//todo add to cart button and pages
function productListGenerator() {

    var page = 0;

    function getData() {
        $.ajax({
            type: 'GET',
            url: '/api/shop/product/list?page=' + page,
            dataType: 'json',
            success: function (data) {
              contentLoad(data);
              console.log(data);
            }
        });
    }
    getData();
}

function contentLoad(data) {
    for (let i = 0; i < data.content.length; i++) {
        aGenerator('contentLink' + i, 'content', 'a-link', 'http://localhost:8080/shop/product/detail?id=' + data.content[i].id);
        divGenerator('contentDetail' + i, 'contentLink' + i, 'content-detail');
        imgGenerator('imgName' + i, 'contentDetail' + i, 'content-img', 'http://localhost:8080/api/shop/product/photo/get/' + data.content[i].id);
        divGenerator('info' + i, 'contentDetail' + i, '');
        pGenerator('name' + i, 'info' + i, 'product-name', data.content[i].name);
        if (data.content[i].price > data.content[i].discountPrice) {
            pGenerator('namePrice' + i, 'info' + i, 'product-disc', data.content[i].price + ' $');
            pGenerator('nameDisc' + i, 'info' + i, 'product-price', data.content[i].discountPrice + ' $');
        } else {
            pGenerator('namePrice' + i, 'info' + i, 'product-price', data.content[i].price + ' $');
        }
        pGenerator('nameAva' + i, 'info' + i, 'ava-product', 'Available: ' + data.content[i].quantity);
    }
}
