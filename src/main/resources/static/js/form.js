/**
 * 
 * @param {id del form} id 
 * @param {path esperado} path 
 */
function changeFormAction(id, path) {
    document.getElementById(id).action = `http://localhost:8080${path}`
}

function obtenerProfesiones() {

    let select = document.getElementById('rubro');
    let rubro = select.options[select.selectedIndex].value;

    fetch(`/profesion/tipo/${rubro}`)
        .then(response => response.json())
        .then(data => {

            let select = document.getElementById('tipo');

            while (select.options.length > 0) {
                select.remove(0);
            }

            data.forEach(e => {
                let opt = document.createElement('option');
                opt.value = e;
                opt.innerHTML = e;
                select.appendChild(opt);
            })
        });

}
