function error_h6(mainDiv, error){
    var h = document.createElement('h6');
    h.innerHTML = error;
    h.style.textAlign = 'center';
    h.className = 'text-danger';

    mainDiv.appendChild(h);
}