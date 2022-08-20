function error_h6(mainDiv, error, status){
    var h = document.createElement('h6');
    h.innerHTML = error;
    h.style.textAlign = 'center';
    if (status === 's'){
        h.className = 'text-success';
    }
    if (status === 'e'){
        h.className = 'text-danger';
    }

    mainDiv.appendChild(h);
}