//pega a linha clicada e preenche a modal editar
$('document').ready(function () {
    $('.table .eBtn').on('click', function (event) {

        event.preventDefault();

        var href = $(this).attr('href');

        $.get(href, function (produto) {
            $('#id-produto').val(produto.id);
            $('#nome-produto').val(produto.nome);
            $('#preco-produto').val(produto.preco);
            $('#tipo-produto').val(produto.tipoProduto);
            $('#descricao-produto').val(produto.descricaoProduto);
        })
        $('#modalEditar').modal();
    });
});

//cadastra um produto na tabela
$(document).ready(function () {
    $(".btnCadastrarProduto").on('click', function () {


        var select = document.getElementById("produtoSelect");
        var value = select.options[select.selectedIndex].value;

        var preco = document.getElementById("idpreco").value;
        var precoFormat = preco.toLocaleString(
            'pt-BR', {minimumFractionDigits: 2, maximumFractionDigits: 2, style: 'currency', currency: 'BRL'}
        );

        var produto = {
            nome: document.getElementById("idnome").value,
            tipoProduto: value,
            preco: precoFormat,
            descricaoProduto: document.getElementById("idDescricao").value,
        }
        $.ajax({
            type: "POST",
            url: "/products",
            data: JSON.stringify(produto),
            datatype: 'json',
            contentType: "application/json; charset=utf-8",

            success: function (data) {
                //console.log(JSON.stringify(data));
                $('input[class="form-control"],textarea').val('');

                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 6000
                });

                Toast.fire({
                    type: 'success',
                    title: 'Produto cadastrado com sucesso!'
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
                    title: 'Erro ao cadastrar o produto'
                })
            }
        });


    });
});

//edita um produto na tabela
$(document).ready(function () {
    $(".btnEditarProduto").on('click', function () {

        var id = document.getElementById("id-produto").value;
        var select = document.getElementById("tipo-produto");
        var value = select.options[select.selectedIndex].value;

        var preco = document.getElementById("preco-produto").value;
        var precoFormat = preco.toLocaleString(
            'pt-BR', {minimumFractionDigits: 2, maximumFractionDigits: 2, style: 'currency', currency: 'BRL'}
        );

        var produto = {
            nome: document.getElementById("nome-produto").value,
            tipoProduto: value,
            preco: precoFormat,
            descricaoProduto: document.getElementById("descricao-produto").value,
        }
        $.ajax({
            type: "PUT",
            url: "/api/produto/update/" + id,
            data: JSON.stringify(produto),
            datatype: 'json',
            contentType: "application/json; charset=utf-8",

            success: function (data) {
                //console.log(JSON.stringify(data));
                $('input[class="form-control"],textarea').val('');

                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 6000
                });

                Toast.fire({
                    type: 'success',
                    title: 'Produto atualizado com sucesso!'
                })
                //fecha a modal apos o envio do formulario
                $("#modalEditar").modal("hide");
                atualizarPagina();
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
                    title: 'Erro ao atualizar o produto'
                })
            }
        });


    });
});



//deleta um produto na tabela
$(document).ready(function () {
    $('.table .deleteBtn').on('click', function (event) {

        event.preventDefault();

        var href = $(this).attr('href');

        $.get(href, function (produto) {
            var id = produto.id;
            //$('#modalConfirmDelete').modal();
            console.log(id)

            /*$.ajax({
                type: "DELETE",
                url: "/api/produto/delete/" + id,
                //data: JSON.stringify(produto),
                datatype: 'json',
                contentType: "application/json; charset=utf-8",

                success: function (data) {
                    //console.log(JSON.stringify(data));
                    const Toast = Swal.mixin({
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 6000
                    });

                    Toast.fire({
                        type: 'success',
                        title: 'Produto atualizado com sucesso!'
                    })
                    //fecha a modal apos o envio do formulario
                    $("#modalConfirmDelete").modal("hide");
                    atualizarPagina();
                },
                error: function () {
                    console.log(id)

                    const Toast = Swal.mixin({
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 6000
                    });

                    Toast.fire({
                        type: 'error',
                        title: 'Erro ao atualizar o produto'
                    })
                    alert(id)
                }
            });*/
        });
    });
});

//Função para atualizar a pagina
function atualizarPagina() {

    /*$(document).ready(function () {
        var id =  document.getElementById("id-produto").value;
        $.ajax({
            type: "GET",
            url: "/api/produto/listar",
            datatype: 'json',
            //data: $('#produto').serialize(),
            contentType: "application/json; charset=utf-8",
            success: function (url) {
                var produto = url;
                //console.log(id)
                console.log(JSON.stringify(produto));
            }
        })
    });*/
    //$("#mydiv").load(location.href+" #myDiv>*","");

    setTimeout(function () {
        window.location.reload(true);
    }, 100);
}


//Todos os exemplos estao dentro da documentação .DataTable
//Paginação da tabela
$(function () {
    $('#paginacaoProduto').DataTable({
        "language": {
            "info": "Exibindo _START_ de _END_ no total de _TOTAL_ registros",
            "emptyTable": "Não há produtos cadastrados.",
            "infoFiltered": " (filtragem de _MAX_ registros)",
            "paginate": {
                "previous": "Anterior",
                "next": "Proximo"
            }
        },
        "paging": true,
        "lengthChange": false,
        "searching": true,
        "ordering": true,
        "info": true,
        "autoWidth": true,
    });
});
