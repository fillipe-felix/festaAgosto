$//GET ALL PRODUCTS IN DATABASE
    (document).ready(function () {
    $.ajax({
        type: "get",
        url: "/products",
        data: {Produto: $("#selectVenda").val()},
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        success: function (obj) {
            if (obj != null) {
                var data = obj;
                var selectbox = $('.selectVenda');
                selectbox.find('option').remove();
                $.each(data, function (i, d) {
                    $('<option>').val(d.id).text(d.name).appendTo(selectbox);

                });
            }
        }
    });
});

// SELECT ONE PRODUCT IN SELECT AND PUT A PRODUCT IN ORDER
$(".selectVenda").on("change", function () {
    var x = document.getElementById("produtoSelect").selectedIndex;
    var y = document.getElementById("produtoSelect").options;
    // alert("Index: " + y[x].id + " is " + y[x].text);
    $.ajax({
        type: "GET",
        url: "/orders/getDesc?desc=" + y[x].text + "",
        data: $('#produto').serialize(),
        datatype: 'json',
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            //console.log(JSON.stringify(data));
            table(data)
        },
        error: function () {
        }
    });

})

//RETURN REQUEST WITH PRODUCT AND PUT IN TABLE
function table(json) {
    var table = $('#tabelaitem');
    var qtd = 0;
    table.find('tr').each(function (i, el) {
        var $tds = $(this).find('td'),
            nome = $tds.eq(0).text()
        if (json.nome == nome) {
            qtd += 1;
            return false;
        }
    });
    if (!qtd == 1) {
        tr = $('<tr/>');
        tr.append("<td class = 'nome'>" + json.product.name + "</td>");
        tr.append("<td class = 'preco centerTd'> " + converteFloatMoeda(json.price) + "</td>");
        tr.append("<td class = 'centerTd'>" + "<input class ='col-3 quantidade tamInput' type='number' id='qqid' min='1'max='20' value='1' oninput='validity.valid ? this.save = value : value = this.save;' />" + "</td>");
        tr.append("<td class = 'subtotal centerTd' id = 'subtotal'><span class = 'spanSub'>" + converteFloatMoeda(json.subTotal) + "</span></td>");
        tr.append("<td class = 'centerTd'> <a data-toggle = 'modal' data-target='#modalVenda 'type='button' class='btn-floating btn-sm red informacao'><i class='fas fa-info text-info' aria-hidden='true'></i></a></td>")
        tr.append("<td class = 'centerTd'> <a type='button' class='btn-floating btn-sm red btnDelete' ><i class='fas fa-trash text-danger' aria-hidden='true'></i></a></td>")
        $('#tabelaitem').append(tr);
        sumTable();
    }
}

$(".btnPagamento").on('click', function () {
    var valorTotal;
    $.ajax({
        async: null,
        type: "GET",
        url: "/orders/total",
        data: $('#order').serialize(),
        datatype: 'json',
        contentType: "application/json; charset=utf-8",

        success: function (data) {
           // alert("venda  = " + JSON.stringify(data));
            valorTotal = data.total;

        },
        error: function () {
        }
    });

    var quantidadeItens = 0;
    $(".quantidade").each(function () {
        quantidadeItens += Number($(this).closest('tr').find('input').val());
    });

    $("#vlrpago").val("");
    $("#totalitens").val(quantidadeItens);
    $("#totalpagar").val(converteFloatMoeda(valorTotal));
    $("#totalpago").val("0,00");
    $("#troco").val("0,00");

});

//add payment in table
$(document).ready(function () {
    $("#btnAddPagamento").on('click', function () {
        var select = document.getElementById("pgto");
        var typePayment = select.options[select.selectedIndex].value;
        var valuePayment = document.getElementById("vlrpago").value;
        if (!valuePayment || valuePayment == 0) {
            const Toast = Swal.mixin({
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 4000
            });
            Toast.fire({
                type: 'error',
                title: 'Não é aceito o campo valor em branco'
            })
        } else {

            var payment = {
                typePayment: typePayment,
                value: realParaNumber(valuePayment),
            }

          //  alert(JSON.stringify(payment));
            $.ajax({
                async: null,
                type: "POST",
                url: "/orders/payment/add",
                data: JSON.stringify(payment),
                datatype: 'json',
                contentType: "application/json; charset=utf-8",

                success: function (data) {
                    //console.log(JSON.stringify(data));

                    tr = $('<tr/>');
                    tr.append("<td>" +data.codigo+ "</td>");
                    tr.append("<td>" +data.typePayment + "</td>");
                    tr.append("<td><span>"+converteFloatMoeda(data.value)+"</span></td>");
                    $('#tablePgto').append(tr);
                    $("#totalpago").val(converteFloatMoeda(data.totalPago));
                    $("#totalpagar").val(converteFloatMoeda(data.remainingValue));
                    $("#troco").val(converteFloatMoeda(data.troco));


                    const Toast = Swal.mixin({
                        toast: true,
                        position: 'top-end',
                        showConfirmButton: false,
                        timer: 6000
                    });

                    Toast.fire({
                        type: 'success',
                        title: 'Pagamento recebido com sucesso'
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
                        title: 'Erro ao adicionar o pagamento'
                    })
                }
            });
        }
    });
});

//RETURN SUM  ALL ITENS IN ORDER
function sumTable() {
    $.ajax({
        async: null,
        type: "GET",
        url: "/orders/total",
        data: $('#order').serialize(),
        datatype: 'json',
        contentType: "application/json; charset=utf-8",

        success: function (data) {
            //  alert("venda  = "+JSON.stringify(data));
            $(".total").text(converteFloatMoeda(data.total));

        },
        error: function () {
        }
    });
};


//SUBTOTAL
$(document).ready(function () {
    $("#tabelaitem").on('click', '.quantidade', function () {
        var currentRow = $(this).closest("tr");
        var nome = currentRow.find(".nome").html();
        var quantidade = $(this).closest('tr').find('input').val();
        var subtotal;
        $.ajax({
            async: null,
            type: "GET",
            url: "/orders/subtotal/" + nome + "/" + quantidade + "",
            data: $('#produto').serialize(),
            datatype: 'json',
            contentType: "application/json; charset=utf-8",

            success: function (data) {
                subtotal = data.subTotal;
                // alert(JSON.stringify(data));
            },
            error: function () {
            }
        });
        $(this).closest('tr').find('span').html(converteFloatMoeda(subtotal));

        sumTable();

    });
});

//CLEAN OBEJECT IN ORDER
$(document).ready(function () {
    $("#openOrder").on('click', function () {

        $.ajax({
            async: null,
            type: "POST",
            url: "/orders/clean",
            dataType: 'json',
            contentType: "application/json",
            success: function () {

            },
            error: function () {
                alert("aconteceu algo inesperado, entre em contato com o TI");

            }

        });
    });
});

$(document).ready(function () {
    $("#tabelaitem").on('click', '.btnDelete', function () {
        var currentRow = $(this).closest("tr");
        var name = currentRow.find(".nome").html();

        $.ajax({
            async: null,
            type: "GET",
            url: "/orders/remove/" + name + "",
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            success: function () {

            },
            error: function () {
                alert("aconteceu algo inesperado, entre em contato com o TI");

            }

        });
        $(this).closest("tr").remove();

        sumTable();
    });
});

function realParaNumber(valor) {
    if (valor === "") {
        valor = 0;
    } else {
        valor = valor.replace(".", "");
        valor = valor.replace(",", ".");
        valor = parseFloat(valor);
    }
    return valor;

}

$(document).ready(function () {
    $("#tabelaitem").on('click', '.informacao', function () {
        var currentRow = $(this).closest("tr");
        var nome = currentRow.find(".nome").html();

        $.ajax({
            type: "GET",
            url: "/orders/getDesc?desc=" + nome,
            data: $('#produto').serialize(),
            datatype: 'json',
            contentType: "application/json; charset=utf-8",

            success: function (produto) {
                //console.log(JSON.stringify(data));
                $('#id-produto-venda').val(produto.id);
                $('#nome-produto-venda').val(produto.nome);
                $('#tipo-produto-venda').val(produto.tipoProduto);
                $('#preco-produto-venda').val(converteFloatMoeda(produto.preco));
                $('#descricao-produto-venda').val(produto.descricaoProduto);

                $('#modalVenda').modal();
            },
            error: function () {
            }
        });
    });
});

//SELECT SEARCH
$('.selectVenda').select2({
    placeholder: "Selecione um Produto",
    allowClear: true,
    maximumResultsForSearch: 1 // at least 20 results must be displayed
});


function converteFloatMoeda(valor){
    var inteiro = null, decimal = null, c = null, j = null;
    var aux = new Array();
    valor = ""+valor;
    c = valor.indexOf(".",0);
    //encontrou o ponto na string
    if(c > 0){
        //separa as partes em inteiro e decimal
        inteiro = valor.substring(0,c);
        decimal = valor.substring(c+1,valor.length);
    }else{
        inteiro = valor;
    }

    //pega a parte inteiro de 3 em 3 partes
    for (j = inteiro.length, c = 0; j > 0; j-=3, c++){
        aux[c]=inteiro.substring(j-3,j);
    }

    //percorre a string acrescentando os pontos
    inteiro = "";
    for(c = aux.length-1; c >= 0; c--){
        inteiro += aux[c]+'.';
    }
    //retirando o ultimo ponto e finalizando a parte inteiro

    inteiro = inteiro.substring(0,inteiro.length-1);

    decimal = parseInt(decimal);
    if(isNaN(decimal)){
        decimal = "00";
    }else{
        decimal = ""+decimal;
        if(decimal.length === 1){
            decimal = decimal+"0";
        }
    }


    valor = "R$ "+inteiro+","+decimal;


    return valor;

}


