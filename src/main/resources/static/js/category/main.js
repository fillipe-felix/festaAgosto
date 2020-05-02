$(document).ready(function () {
    $(".btnCadastrarCategoria").on('click', function () {



        console.log("Entrou")
        var categoria = {
            name: document.getElementById("idnome").value,
        }



        $.ajax({
            type: "POST",
            url: "/categories",
            data: JSON.stringify(categoria),
            datatype: 'json',
            contentType: "application/json; charset=utf-8",

            success: function (data) {
                //console.log(JSON.stringify(data));
                $('input[class="form-control"]').val('');

                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 6000
                });

                Toast.fire({
                    type: 'success',
                    title: 'Categoria cadastrado com sucesso!'
                })
            },
            error: function () {
                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 6000
                });

                Toast.fire({
                    type: 'error',
                    title: 'Erro ao cadastrar o categoria'
                })
            }
        });


    });
});