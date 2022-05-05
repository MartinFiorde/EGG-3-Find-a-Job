// MAF: FUNCION QUE PERMITE ENVIAR UN MISMO FORMULARIO A DIFERENTES CONTROLADORES POST - NO LO USE AL FINAL
/**
 * 
 * @param {id del form} id 
 * @param {path esperado} path 
 */
function changeFormAction(id, path) {
    document.getElementById(id).action = `http://localhost:8080${path}`
}

// MAF: FUNCION QUE PERMITE AUTOGENERAR LA LISTA DESPLEGABLE DE TIPO CUANDO SE ELIJE UN RUBRO
function obtenerTipos() {

    let select = document.getElementById('rubro');
    let rubro = select.options[select.selectedIndex].value;

    fetch(`/profesion/tipo/${rubro}`)
        .then(response => response.json())
        .then(data => {

            let select2 = document.getElementById('subtipo');

            while (select2.options.length > 0) {
                select2.remove(0);
            }

            let select = document.getElementById('tipo');

            while (select.options.length > 0) {
                select.remove(0);
            }
            
            let optnull = document.createElement('option');
            optnull.value = null;
            optnull.innerHTML = null;
            select.append(optnull);

            data.forEach(e => {
                let opt = document.createElement('option');
                opt.value = e;
                opt.innerHTML = e;
                select.appendChild(opt);
            })
        });

}

// MAF: FUNCION QUE PERMITE AUTOGENERAR LA LISTA DESPLEGABLE DE SUBTIPO CUANDO SE ELIJE UN TIPO
function obtenerSubtipos() {

    let select = document.getElementById('tipo');
    let tipo = select.value;

    fetch(`/profesion/subtipo/${tipo}`)
        .then(response => response.json())
        .then(data => {

            let select = document.getElementById('subtipo');

            while (select.options.length > 0) {
                select.remove(0);
            }

            let optnull = document.createElement('option');
            optnull.value = null;
            optnull.innerHTML = null;
            select.append(optnull);

            data.forEach(e => {
                let opt = document.createElement('option');
                opt.value = e;
                opt.innerHTML = e;
                select.appendChild(opt);
            })
        });

}