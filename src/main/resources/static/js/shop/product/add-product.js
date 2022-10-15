function addProduct() {

    const name = document.getElementById('name').value;
    const price = document.getElementById('price').value;
    const productQuantity = document.getElementById('quantity').value;
    const description = document.getElementById('desc').value;
    const select = document.getElementById('category');
    const value = select.options[select.selectedIndex].value;
    const errorDiv = document.getElementById('error');

    function add() {
        if(document.getElementById('product_photo').files.length === 0 ){
            error_h6(errorDiv, 'You did not chose photo.', 'e')
        }else {
            request();
        }
    }

    function request() {
        const addProduct = {
            "name": name,
            "price": price,
            "productQuantity": productQuantity,
            "productCategory": value,
            "description": description
        };

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            url: '/api/shop/product/create',
            data: JSON.stringify(addProduct),
            error: function (xhr, status, error) {
                error_h6(errorDiv, xhr.responseText, 'e');
            },
            success: function (data) {
                uploadFile(data.id);
            }
        });
    }

    async function uploadFile(id) {
        let formData = new FormData();
        formData.append("file", product_photo.files[0]);
        let response = await fetch('/api/shop/product/photo/set/' + id, {
            method: "POST",
            body: formData
        });
        if (response.ok){
            error_h6(errorDiv, "Successfully create product.", 's')
        }
    }

    add();
}