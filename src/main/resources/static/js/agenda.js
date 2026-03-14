function mudarTela(idTelaAlvo) {
    const telas = document.querySelectorAll('.tela');
    telas.forEach(tela => {
        tela.style.display = 'none';
        tela.classList.remove('ativa');
    });

    const telaAtiva = document.getElementById(idTelaAlvo);
    if (telaAtiva) {
        telaAtiva.style.display = 'block';
        telaAtiva.classList.add('ativa');
    }

    if (idTelaAlvo === 'tela-clientes') {
        window.dispatchEvent(new CustomEvent('carregarClientesEvent')); 
    }
}

function fecharModal(idModal) {
    const modal = document.getElementById(idModal);
    if(modal) modal.style.display = 'none';
}

function abrirModal(idModal) {
    const modal = document.getElementById(idModal);
    if(modal) modal.style.display = 'flex'; 
}

function getAuthToken() { return localStorage.getItem('@CDB:token'); }
function getBarbeiroId() {
    const user = JSON.parse(localStorage.getItem('@CDB:user') || '{}');
    return user.barbeiroId || user.id;
}

function converterParaBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}

async function salvarPerfil(event) {
    if (event) event.preventDefault(); 

    const barbeiroId = getBarbeiroId(); 
    const token = getAuthToken();
    const fotoInput = document.getElementById('perfil-foto');
    
    let fotoBase64 = null;

    if (fotoInput.files && fotoInput.files.length > 0) {
        try {
            fotoBase64 = await converterParaBase64(fotoInput.files[0]);
        } catch (error) {
            alert("Erro ao processar a imagem do perfil.");
            return;
        }
    }
    
    const dados = {
        nome: document.getElementById('perfil-nome').value,
        whatsapp: document.getElementById('perfil-whatsapp').value
    };

    if (fotoBase64) {
        dados.fotoUrl = fotoBase64; 
    }

    try {
        const response = await fetch(`http://localhost:8080/api/barbeiros/${barbeiroId}/perfil`, {
            method: 'PUT',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` 
            },
            body: JSON.stringify(dados)
        });

        if (response.ok) {
            alert("Perfil atualizado com sucesso!");
            const userObj = JSON.parse(localStorage.getItem('@CDB:user'));
            if(dados.nome) userObj.nome = dados.nome;
            if(dados.fotoUrl) userObj.fotoUrl = dados.fotoUrl;
            localStorage.setItem('@CDB:user', JSON.stringify(userObj));

            fecharModal('modal-editar-perfil');
            location.reload(); 
        } else {
            alert("Erro ao atualizar perfil.");
        }
    } catch (error) { console.error("Erro:", error); }
}

async function salvarSenha() {
    const barbeiroId = getBarbeiroId(); 
    const token = getAuthToken();
    
    const dados = {
        senhaAntiga: document.getElementById('senha-antiga').value,
        novaSenha: document.getElementById('nova-senha').value
    };

    try {
        const response = await fetch(`http://localhost:8080/api/barbeiros/${barbeiroId}/senha`, {
            method: 'PATCH',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(dados)
        });

        if (response.ok) {
            alert("Senha alterada com sucesso!");
            fecharModal('modal-editar-senha');
            document.getElementById('senha-antiga').value = '';
            document.getElementById('nova-senha').value = '';
        } else {
            alert("Erro: Verifique se a senha atual está correta.");
        }
    } catch (error) { console.error("Erro:", error); }
}

function abrirEdicaoServico(id, nome, valor) {
    document.getElementById('edit-servico-id').value = id;
    document.getElementById('edit-servico-nome').value = nome;
    document.getElementById('edit-servico-valor').value = valor;
    
    abrirModal('modal-editar-servico');
}

async function salvarEdicaoServico() {
    const token = getAuthToken();
    const id = document.getElementById('edit-servico-id').value;
    const dados = {
        nomeServico: document.getElementById('edit-servico-nome').value,
        valorBase: parseFloat(document.getElementById('edit-servico-valor').value)
    };

    try {
        const response = await fetch(`http://localhost:8080/api/servicos/${id}`, {
            method: 'PUT',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(dados)
        });

        if (response.ok) {
            alert("Serviço atualizado!");
            fecharModal('modal-editar-servico');
            window.dispatchEvent(new CustomEvent('recarregarServicosEvent'));
        } else {
            alert("Erro ao editar o serviço.");
        }
    } catch (error) { console.error("Erro:", error); }
}

window.excluirCliente = async function(id) {
    if (!confirm("Tem certeza que deseja apagar este cliente do sistema?")) return;
    
    const token = getAuthToken();
    try {
        const response = await fetch(`http://localhost:8080/api/clientes/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            alert("Cliente excluído com sucesso!");
            window.dispatchEvent(new CustomEvent('carregarClientesEvent')); 
        } else {
            alert("Erro ao excluir. Este cliente pode ter agendamentos vinculados.");
        }
    } catch(e) {
        console.error(e);
        alert("Erro de conexão ao tentar excluir o cliente.");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem('@CDB:token');
    const userDataStr = localStorage.getItem('@CDB:user');

    if (!token || !userDataStr) {
        window.location.href = "login.html";
        return;
    }

    const user = JSON.parse(userDataStr);
    const nomeElement = document.getElementById('nome-barbeiro');
    const fotoElement = document.getElementById('foto-perfil');
    const idDoBarbeiro = user.barbeiroId || user.id;

    if (nomeElement) nomeElement.textContent = user.nome;
    if (fotoElement) {
        fotoElement.src = user.fotoUrl || "https://ui-avatars.com/api/?name=" + encodeURIComponent(user.nome) + "&background=D4AF37&color=232129";
    }

    const btnSair = document.getElementById('btn-sair');
    if (btnSair) {
        btnSair.addEventListener('click', () => {
            localStorage.removeItem('@CDB:token');
            localStorage.removeItem('@CDB:user');
            window.location.href = "login.html";
        });
    }

    let listaClientesGlobal = [];
    const tabelaListaClientesDOM = document.getElementById('tabela-lista-clientes');
    const inputPesquisaClienteLista = document.getElementById('pesquisa-cliente-lista');

    function renderizarTabelaClientes(clientes) {
        if (!tabelaListaClientesDOM) return;
        tabelaListaClientesDOM.innerHTML = '';

        if (clientes.length === 0) {
            tabelaListaClientesDOM.innerHTML = '<tr><td colspan="3" style="text-align: center; padding: 15px; color: #999;">Nenhum cliente encontrado.</td></tr>';
            return;
        }

        clientes.forEach(cliente => {
            const tr = document.createElement('tr');
            tr.style.borderBottom = "1px solid #444";
            
            const nome = cliente.nome || 'Sem nome';
            const telefone = cliente.telefone || cliente.whatsapp || 'Sem número';
            
            tr.innerHTML = `
                <td style="padding: 10px 0; color: #fff;">${nome}</td>
                <td style="padding: 10px 0; color: #ccc;">${telefone}</td>
                <td style="padding: 10px 0; text-align: right;">
                    <button type="button" onclick="window.excluirCliente('${cliente.id}')" style="background: #ff4c4c; color: white; border: none; padding: 4px 8px; cursor: pointer; border-radius: 4px; font-weight: bold;">Excluir</button>
                </td>
            `;
            tabelaListaClientesDOM.appendChild(tr);
        });
    }

    async function carregarClientes() {
        try {
            const response = await fetch('http://localhost:8080/api/clientes', {
                headers: { 'Authorization': `Bearer ${token}` }
            }); 
            if (response.ok) {
                listaClientesGlobal = await response.json();
                renderizarTabelaClientes(listaClientesGlobal);
            }
        } catch (error) {
            console.error("Erro ao carregar clientes do banco:", error);
        }
    }

    window.addEventListener('carregarClientesEvent', carregarClientes);
    carregarClientes(); 

    if (inputPesquisaClienteLista) {
        inputPesquisaClienteLista.addEventListener('input', (e) => {
            const termoBusca = e.target.value.toLowerCase();
            const clientesFiltrados = listaClientesGlobal.filter(c => 
                c.nome && c.nome.toLowerCase().includes(termoBusca)
            );
            renderizarTabelaClientes(clientesFiltrados);
        });
    }

 const inputNomeCliente = document.getElementById('nome-cliente');
    const inputTelefone = document.getElementById('telefone-cliente');
    const inputIdCliente = document.getElementById('id-cliente-selecionado');
    const dropdownClientes = document.getElementById('lista-clientes-dropdown');

    if (inputNomeCliente && dropdownClientes) {
        inputNomeCliente.addEventListener('input', function() {
            const termoBusca = this.value.toLowerCase();
            dropdownClientes.innerHTML = ''; 
            
            if (termoBusca.length === 0) {
                dropdownClientes.style.display = 'none';
                if(inputIdCliente) inputIdCliente.value = '';
                return;
            }

            const clientesFiltrados = listaClientesGlobal.filter(cliente => 
                cliente.nome && cliente.nome.toLowerCase().includes(termoBusca)
            );

            if (clientesFiltrados.length > 0) {
                dropdownClientes.style.display = 'block';
                
                clientesFiltrados.forEach(cliente => {
                    const li = document.createElement('li');
                    li.textContent = `${cliente.nome} (${cliente.telefone || cliente.whatsapp || 'Sem número'})`; 
                    
                    // 👇 MUDANÇA APLICADA AQUI
                    li.onclick = () => {
                        inputNomeCliente.value = cliente.nome;
                        if(inputTelefone) inputTelefone.value = cliente.telefone || cliente.whatsapp || ''; 
                        if(inputIdCliente) inputIdCliente.value = cliente.id; 
                        
                        // Salvando a foto temporariamente no formulário:
                        formAgendamento.dataset.fotoCliente = cliente.fotoPerfil || cliente.fotoUrl || '';
                        
                        dropdownClientes.style.display = 'none'; 
                    };
                    // 👆 FIM DA MUDANÇA

                    dropdownClientes.appendChild(li);
                });
            } else {
                dropdownClientes.style.display = 'none';
            }
        });

        // Fecha o dropdown se clicar fora
        document.addEventListener('click', function(e) {
            if (e.target !== inputNomeCliente && e.target !== dropdownClientes) {
                dropdownClientes.style.display = 'none';
            }
        });
    }

    // Continuação do seu código original...
    const btnNovoCliente = document.getElementById('btn-novo-cliente');
    const btnFecharModalCliente = document.getElementById('btn-fechar-modal-cliente');
    const formCliente = document.getElementById('form-cliente');

    if (btnFecharModalCliente) {
        btnFecharModalCliente.addEventListener('click', () => {
            fecharModal('modal-cliente');
            formCliente.reset();
        });
    }

    if (formCliente) {
        formCliente.addEventListener('submit', async (e) => {
            e.preventDefault();

            const nome = document.getElementById('novo-cliente-nome').value;
            const whatsapp = document.getElementById('novo-cliente-whatsapp').value;
            const fotoInput = document.getElementById('novo-cliente-foto');
            
            let fotoBase64 = null;

            if (fotoInput.files.length > 0) {
                try {
                    fotoBase64 = await converterParaBase64(fotoInput.files[0]);
                } catch (error) {
                    alert("Erro ao processar a imagem.");
                    return;
                }
            }

            const corpoRequisicao = {
                nome: nome,
                telefone: whatsapp, 
                fotoPerfil: fotoBase64,
                barbeiro: { id: idDoBarbeiro } 
            };

            try {
                const response = await fetch('http://localhost:8080/api/clientes', {
                    method: 'POST',
                    headers: { 
                        'Authorization': `Bearer ${token}`, 
                        'Content-Type': 'application/json' 
                    },
                    body: JSON.stringify(corpoRequisicao)
                });

                if (response.ok) {
                    alert("Cliente cadastrado com sucesso!");
                    fecharModal('modal-cliente');
                    formCliente.reset();
                    carregarClientes(); 
                } else {
                    alert("Erro ao cadastrar cliente: " + await response.text());
                }
            } catch (error) {
                console.error(error);
                alert("Erro de conexão ao salvar cliente.");
            }
        });
    }

    const btnAbrirModal = document.getElementById('btn-abrir-modal');
    const btnFecharModalAgendamento = document.getElementById('btn-fechar-modal');
    const formAgendamento = document.getElementById('form-agendamento');

    const btnFecharModalPagamento = document.getElementById('btn-fechar-modal-pagamento');
    const formPagamento = document.getElementById('form-pagamento');

    btnAbrirModal.addEventListener('click', () => {
        carregarServicosParaSelect(); 
        abrirModal('modal-agendamento');
    });

    btnFecharModalAgendamento.addEventListener('click', () => {
        fecharModal('modal-agendamento');
        formAgendamento.reset(); 
    });

    btnFecharModalPagamento.addEventListener('click', () => {
        fecharModal('modal-pagamento');
        formPagamento.reset();
    });

    let dataSelecionada = new Date(); 
    let dataCalendario = new Date(); 

    const meses = ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];
    
    function renderizarCalendario() {
        const mesAtual = dataCalendario.getMonth();
        const anoAtual = dataCalendario.getFullYear();
        
        document.getElementById('mes-calendario').textContent = `${meses[mesAtual]} ${anoAtual}`;
        const gridDias = document.getElementById('dias-calendario');
        gridDias.innerHTML = '';

        const primeiroDiaDoMes = new Date(anoAtual, mesAtual, 1).getDay();
        const ultimoDiaDoMes = new Date(anoAtual, mesAtual + 1, 0).getDate();

        for (let i = 0; i < primeiroDiaDoMes; i++) {
            gridDias.appendChild(document.createElement('span'));
        }

        for (let dia = 1; dia <= ultimoDiaDoMes; dia++) {
            const spanDia = document.createElement('span');
            spanDia.textContent = dia;
            spanDia.style.cursor = 'pointer';
            spanDia.style.padding = '5px';
            spanDia.style.borderRadius = '50%';
            spanDia.style.textAlign = 'center';

            if (dia === dataSelecionada.getDate() && mesAtual === dataSelecionada.getMonth() && anoAtual === dataSelecionada.getFullYear()) {
                spanDia.style.background = '#D4AF37';
                spanDia.style.color = '#232129';
                spanDia.style.fontWeight = 'bold';
            } else {
                spanDia.style.color = '#fff';
            }

            spanDia.addEventListener('click', () => {
                dataSelecionada = new Date(anoAtual, mesAtual, dia);
                renderizarCalendario(); 
                carregarAgendaDoDia();  
            });

            gridDias.appendChild(spanDia);
        }
    }

    document.querySelector('.calendar-header button:first-child').addEventListener('click', () => {
        dataCalendario.setMonth(dataCalendario.getMonth() - 1);
        renderizarCalendario();
    });
    document.querySelector('.calendar-header button:last-child').addEventListener('click', () => {
        dataCalendario.setMonth(dataCalendario.getMonth() + 1);
        renderizarCalendario();
    });

    function formatarDataISO(data) {
        const y = data.getFullYear();
        const m = String(data.getMonth() + 1).padStart(2, '0');
        const d = String(data.getDate()).padStart(2, '0');
        return `${y}-${m}-${d}`; 
    }

    function formatarDataBR(data) {
        const d = String(data.getDate()).padStart(2, '0');
        const m = String(data.getMonth() + 1).padStart(2, '0');
        const y = data.getFullYear();
        return `${d}/${m}/${y}`; 
    }

    async function carregarAgendaDoDia() {
        const dataISO = formatarDataISO(dataSelecionada);
        document.getElementById('data-atual').textContent = `Agenda de ${formatarDataBR(dataSelecionada)}`;
        
        const containerAgenda = document.getElementById('container-proximo');
        containerAgenda.innerHTML = '<p style="color: #999;">Buscando horários...</p>';

        try {
           const url = `http://localhost:8080/api/agendamentos?barbeiroId=${idDoBarbeiro}&dataInicial=${dataISO}&dataFinal=${dataISO}`;
            const response = await fetch(url, {
                headers: { 'Authorization': `Bearer ${token}` }
            });

            if (response.ok) {
                const agendamentos = await response.json();
                renderizarGradeDeHorarios(agendamentos, dataISO);
            } else {
                containerAgenda.innerHTML = '<p style="color: red;">Erro ao carregar a agenda.</p>';
            }
        } catch (error) {
            console.error(error);
            containerAgenda.innerHTML = '<p style="color: red;">Erro de conexão com o servidor.</p>';
        }
    }

 function renderizarGradeDeHorarios(agendamentos, dataISO) {
        const containerAgenda = document.getElementById('container-proximo');
        containerAgenda.innerHTML = ''; 

        const horariosDoDia = [];
        for (let h = 7; h <= 18; h++) {
            horariosDoDia.push(`${String(h).padStart(2, '0')}:00`);
            horariosDoDia.push(`${String(h).padStart(2, '0')}:30`);
        }

        horariosDoDia.forEach(horario => {
            const agendamentoMarcado = agendamentos.find(ag => 
                ag.dataHoraInicio && ag.dataHoraInicio.includes(horario) && ag.status !== 'CANCELADO'
            );

            const divSlot = document.createElement('div');
            divSlot.style.padding = '15px';
            divSlot.style.marginBottom = '10px';
            divSlot.style.borderRadius = '8px';
            divSlot.style.display = 'flex';
            divSlot.style.justifyContent = 'space-between';
            divSlot.style.alignItems = 'center';

            if (agendamentoMarcado) {
                const isConcluido = agendamentoMarcado.status === 'CONCLUIDO';
                divSlot.style.background = isConcluido ? '#2e4233' : '#3e3b47'; 
                divSlot.style.borderLeft = isConcluido ? '5px solid #4CAF50' : '5px solid #D4AF37';
                
                const nomeDoServico = agendamentoMarcado.servico ? (agendamentoMarcado.servico.nomeServico || agendamentoMarcado.servico.nome) : (agendamentoMarcado.servicoNome || 'Serviço agendado');
                const valorDoServico = agendamentoMarcado.servico ? (agendamentoMarcado.servico.valorBase || agendamentoMarcado.servico.preco || 0) : 0;

                // --- Buscando a foto do cliente ou gerando uma com as iniciais ---
                let fotoClienteUrl = agendamentoMarcado.clienteFotoUrl || agendamentoMarcado.clienteFoto || (agendamentoMarcado.cliente && agendamentoMarcado.cliente.fotoPerfil);
                
                // Se vier vazio/null, já define a URL do avatar de iniciais
                if (!fotoClienteUrl) {
                    fotoClienteUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(agendamentoMarcado.clienteNome)}&background=232129&color=D4AF37`;
                }

                let botoesHTML = '';
                if (!isConcluido) {
                    botoesHTML = `
                        <button class="btn-concluir" title="Finalizar Atendimento" style="background: transparent; border: none; color: #4CAF50; cursor: pointer; font-size: 18px; padding: 5px;">✔</button>
                        <button class="btn-excluir" title="Cancelar Agendamento" style="background: transparent; border: none; color: #ff4c4c; cursor: pointer; font-size: 18px; padding: 5px;">✖</button>
                    `;
                } else {
                    botoesHTML = `
                        <div class="status-container" style="display: flex; align-items: center; gap: 10px;">
                            <span style="color: #4ade80; font-size: 14px; font-weight: bold;">Finalizado</span>
                            <button class="btn-excluir-agendamento" title="Excluir registro" style="background: transparent; border: none; color: #999; cursor: pointer; padding: 5px;">
                                <i data-lucide="trash-2" style="width: 20px; height: 20px;"></i>
                            </button>
                        </div>
                    `;
                }

                // --- Estrutura HTML atualizada com proteção de erro na imagem (onerror) ---
                divSlot.innerHTML = `
                    <div style="display: flex; align-items: center; gap: 15px;">
                        <img src="${fotoClienteUrl}" 
                             onerror="this.onerror=null; this.src='https://ui-avatars.com/api/?name=${encodeURIComponent(agendamentoMarcado.clienteNome)}&background=232129&color=D4AF37';" 
                             alt="Foto ${agendamentoMarcado.clienteNome}" 
                             style="width: 45px; height: 45px; border-radius: 50%; object-fit: cover;">
                        <div>
                            <strong style="color: #fff; display: block; font-size: 16px;">${agendamentoMarcado.clienteNome}</strong>
                            <span style="color: #D4AF37; font-size: 14px;">${nomeDoServico}</span>
                        </div>
                    </div>
                    
                    <div style="display: flex; align-items: center; gap: 15px;">
                        <div style="display: flex; align-items: center; gap: 6px; color: #999;">
                            <i data-lucide="clock" style="color: #D4AF37; width: 16px; height: 16px;"></i>
                            <strong style="font-size: 16px;">${horario}</strong>
                        </div>
                        <div style="display: flex; align-items: center; gap: 5px;">
                            ${botoesHTML}
                        </div>
                    </div>
                `;

                if (!isConcluido) {
                    divSlot.querySelector('.btn-excluir').addEventListener('click', (e) => {
                        e.stopPropagation(); 
                        cancelarAgendamento(agendamentoMarcado.id);
                    });

                    divSlot.querySelector('.btn-concluir').addEventListener('click', (e) => {
                        e.stopPropagation();
                        abrirModalDaPagina(agendamentoMarcado.id, valorDoServico);
                    });
                } else {
                    divSlot.querySelector('.btn-excluir-agendamento').addEventListener('click', (e) => {
                        e.stopPropagation();
                        excluirAgendamentoFinalizado(agendamentoMarcado.id);
                    });
                }

            } else {
                divSlot.style.background = 'transparent';
                divSlot.style.border = '1px dashed #555';
                divSlot.style.cursor = 'pointer';
                divSlot.innerHTML = `<span style="color: #777;">Disponível</span><strong style="color: #777;">${horario}</strong>`;

                divSlot.addEventListener('click', () => {
                    abrirModal('modal-agendamento');
                    carregarServicosParaSelect();
                    document.getElementById('data-agendamento').value = dataISO;
                    document.getElementById('hora-agendamento').value = horario;
                });
            }

            containerAgenda.appendChild(divSlot);
        });

        if (typeof lucide !== 'undefined') {
            lucide.createIcons();
        }
    }

    async function excluirAgendamentoFinalizado(idAgendamento) {
        const confirmar = confirm("Tem certeza que deseja excluir o registro deste atendimento?");
        if (!confirmar) return;

        try {
            const response = await fetch(`http://localhost:8080/api/agendamentos/${idAgendamento}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                alert("Atendimento excluído com sucesso!");
                carregarAgendaDoDia();
            } else {
                const erro = await response.text();
                alert("Erro ao excluir: " + erro);
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
            alert("Erro ao conectar com o servidor.");
        }
    }

    // --- ROTA DE CANCELAR ATUALIZADA ---
    async function cancelarAgendamento(idAgendamento) {
        if (!confirm("Tem certeza que deseja cancelar este agendamento?")) return;
        try {
            const response = await fetch(`http://localhost:8080/api/agendamentos/${idAgendamento}/status`, {
                method: 'PATCH',
                headers: { 
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ status: "CANCELADO" })
            });
            
            if (response.ok) {
                alert("Agendamento cancelado!");
                carregarAgendaDoDia();
            } else {
                alert("Erro ao cancelar: " + await response.text());
            }
        } catch (error) { alert("Erro de conexão ao cancelar."); }
    }

    function abrirModalDaPagina(idAgendamento, valorBase) {
        document.getElementById('id-agendamento-pagamento').value = idAgendamento;
        document.getElementById('valor-final').value = valorBase.toFixed(2);
        abrirModal('modal-pagamento');
    }

    // --- ROTA DE FINALIZAR/CONCLUIR ATUALIZADA ---
    formPagamento.addEventListener('submit', async (e) => {
        e.preventDefault();
        const idAgendamento = document.getElementById('id-agendamento-pagamento').value;
        const valorFinal = parseFloat(document.getElementById('valor-final').value.replace(',', '.'));
        const formaPagamento = document.getElementById('forma-pagamento').value;

        try {
            const response = await fetch(`http://localhost:8080/api/agendamentos/${idAgendamento}/status`, {
                method: 'PATCH',
                headers: { 
                    'Authorization': `Bearer ${token}`, 
                    'Content-Type': 'application/json' 
                },
                body: JSON.stringify({ 
                    status: "CONCLUIDO",
                    valorCobrado: valorFinal, 
                    formaPagamento: formaPagamento 
                })
            });

            if (response.ok) {
                alert("Atendimento finalizado com sucesso!");
                fecharModal('modal-pagamento');
                carregarAgendaDoDia();
            } else { 
                alert("Erro ao finalizar: " + await response.text()); 
            }
        } catch (error) { alert("Erro de conexão."); }
    });

    formAgendamento.addEventListener('submit', async (e) => {
        e.preventDefault(); 
        
        const servicoId = document.getElementById('select-servico').value;

        if (!idDoBarbeiro || !servicoId || servicoId === "") {
            alert("Selecione um serviço válido na lista!");
            return;
        }

        const corpoRequisicao = {
            barbeiroId: idDoBarbeiro,
            servicoId: servicoId,
            clienteNome: document.getElementById('nome-cliente').value,
            clienteWhatsapp: document.getElementById('telefone-cliente').value, 
            clienteFotoUrl: formAgendamento.dataset.fotoCliente || '',
            dataHoraInicio: `${document.getElementById('data-agendamento').value}T${document.getElementById('hora-agendamento').value}:00`,
            quantidadeBlocos: 1, 
            isPausa: false,      
            observacao: "Agendado pelo painel" 
        };

        try {
            const response = await fetch('http://localhost:8080/api/agendamentos', {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
                body: JSON.stringify(corpoRequisicao)
            });

            if (response.ok) {
                alert("Agendamento salvo!");
                fecharModal('modal-agendamento');
                formAgendamento.reset();
                carregarAgendaDoDia(); 
            } else { alert("Erro ao salvar: " + await response.text()); }
        } catch (error) { alert("Erro de conexão."); }
    });
    
    window.addEventListener('recarregarServicosEvent', () => {
        carregarTabelaConfiguracao();
        carregarServicosParaSelect();
    });

    async function carregarServicosParaSelect() {
        const selectServico = document.getElementById('select-servico');
        try {
            const response = await fetch('http://localhost:8080/api/servicos', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                const servicos = await response.json();
                selectServico.innerHTML = '<option value="" disabled selected>Selecione um serviço...</option>';
                
                servicos.forEach(s => {
                    const id = s.id || s.idServico;
                    const nome = s.nomeServico || s.nome || s.descricao;
                    const preco = s.valorBase || s.preco || 0;
                    
                    const option = document.createElement('option');
                    option.value = id; 
                    option.textContent = `${nome} - R$ ${preco.toFixed(2).replace('.', ',')}`;
                    selectServico.appendChild(option);
                });
            }
        } catch (error) { console.error("Erro select:", error); }
    }

    const btnConfig = document.getElementById('btn-configurar-servicos');
    const btnFecharConfig = document.getElementById('btn-fechar-config');
    const formNovoServico = document.getElementById('form-novo-servico');
    const tabelaListaServicos = document.getElementById('tabela-lista-servicos');

    if (btnConfig) {
        btnConfig.addEventListener('click', () => {
            abrirModal('modal-config-servicos');
            carregarTabelaConfiguracao();
        });
    }

    if (btnFecharConfig) {
        btnFecharConfig.addEventListener('click', () => fecharModal('modal-config-servicos'));
    }

    async function carregarTabelaConfiguracao() {
        tabelaListaServicos.innerHTML = '<tr><td colspan="3">Carregando...</td></tr>';
        try {
            const response = await fetch('http://localhost:8080/api/servicos', {
                headers: { 'Authorization': `Bearer ${token}` }
            });

            if (response.ok) {
                const servicos = await response.json();
                tabelaListaServicos.innerHTML = ''; 

                servicos.forEach(s => {
                    const id = s.id || s.idServico;
                    const nome = s.nomeServico || s.nome || s.descricao;
                    const preco = s.valorBase || s.preco || 0;

                    const tr = document.createElement('tr');
                    tr.style.borderBottom = "1px solid #444";
                    tr.innerHTML = `
                        <td style="padding: 10px 0;">${nome}</td>
                        <td style="padding: 10px 0;">R$ ${preco.toFixed(2).replace('.', ',')}</td>
                        <td style="padding: 10px 0; text-align: right;">
                            <button type="button" class="btn-editar-servico" onclick="abrirEdicaoServico('${id}', '${nome}', ${preco})" style="background: #D4AF37; color: #232129; border: none; padding: 4px 8px; cursor: pointer; border-radius: 4px; margin-right: 5px; font-weight: bold;">Editar</button>
                            <button type="button" class="btn-excluir-servico" data-id="${id}" style="background: #ff4c4c; color: white; border: none; padding: 4px 8px; cursor: pointer; border-radius: 4px; font-weight: bold;">Excluir</button>
                        </td>
                    `;
                    tabelaListaServicos.appendChild(tr);
                });

                document.querySelectorAll('.btn-excluir-servico').forEach(btn => {
                    btn.addEventListener('click', (e) => excluirServicoDaAPI(e.target.getAttribute('data-id')));
                });
            }
        } catch (error) { console.error(error); }
    }

    if (formNovoServico) {
        formNovoServico.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const corpoRequisicao = {
                nomeServico: document.getElementById('novo-nome-servico').value,
                valorBase: parseFloat(document.getElementById('novo-preco-servico').value)
            };

            try {
                const response = await fetch('http://localhost:8080/api/servicos', {
                    method: 'POST',
                    headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
                    body: JSON.stringify(corpoRequisicao)
                });

                if (response.ok) {
                    formNovoServico.reset();
                    carregarTabelaConfiguracao(); 
                    carregarServicosParaSelect(); 
                } else {
                    alert("Erro ao salvar serviço: " + await response.text());
                }
            } catch (error) { alert("Erro de conexão."); }
        });
    }

    async function excluirServicoDaAPI(id) {
        if (!confirm("Apagar este serviço do banco de dados?")) return;
        try {
            const response = await fetch(`http://localhost:8080/api/servicos/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                carregarTabelaConfiguracao(); 
                carregarServicosParaSelect(); 
            } else {
                alert("Erro ao excluir. O serviço já pode estar em uso.");
            }
        } catch (error) { alert("Erro de conexão."); }
    }

    

    renderizarCalendario();
    carregarAgendaDoDia();
});

// ==========================================
// --- FUNÇÕES GLOBAIS (FORA DO DOMContentLoaded) ---
// ==========================================

// Função assíncrona para buscar e atualizar o resumo financeiro na tela
async function atualizarFinanceiro() {
    const container = document.getElementById('container-financeiro');
    
    // Se o container já estiver visível, esconde. Se estiver escondido, mostra.
    if (container.style.display === 'flex') {
        container.style.display = 'none';
        return;
    }

    try {
        const token = localStorage.getItem('token'); 

        // ADICIONE ESTA LINHA PARA TESTARMOS:
        console.log("Meu token atual é:", token); 

        
        const response = await fetch('http://localhost:8080/api/financeiro/resumo', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) throw new Error('Erro ao carregar os dados financeiros');

        const dados = await response.json();

        // Formatação para Real Brasileiro (BRL)
        const formatarMoeda = (valor) => 
            valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

        // Atualiza os valores nos cards do HTML
        document.getElementById('faturamento-hoje').innerText = formatarMoeda(dados.faturamentoHoje);
        document.getElementById('faturamento-mes').innerText = formatarMoeda(dados.faturamentoMesAtual);

        // Mostra o container com os valores atualizados
        container.style.display = 'flex';
        
    } catch (error) {
        console.error('Erro no financeiro:', error);
        alert('Não foi possível carregar o resumo financeiro.');
    }
}

// ==========================================
// --- FUNÇÕES GLOBAIS (FORA DO DOMContentLoaded) ---
// ==========================================

// Função assíncrona para buscar e atualizar o resumo financeiro na tela
async function atualizarFinanceiro() {
    const container = document.getElementById('container-financeiro');
    
    // Se o container já estiver visível, esconde. Se estiver escondido, mostra.
    if (container.style.display === 'flex') {
        container.style.display = 'none';
        return;
    }

    try {
        // CORREÇÃO: Pegando o token com o nome exato salvo no momento do login
        const token = localStorage.getItem('@CDB:token'); 
        
        const response = await fetch('http://localhost:8080/api/financeiro/resumo', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) throw new Error('Erro ao carregar os dados financeiros');

        const dados = await response.json();

        // Formatação para Real Brasileiro (BRL)
        const formatarMoeda = (valor) => 
            valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

        // Atualiza os valores nos cards do HTML
        document.getElementById('faturamento-hoje').innerText = formatarMoeda(dados.faturamentoHoje);
        document.getElementById('faturamento-mes').innerText = formatarMoeda(dados.faturamentoMesAtual);

        // Mostra o container com os valores atualizados
        container.style.display = 'flex';
        
    } catch (error) {
        console.error('Erro no financeiro:', error);
        alert('Não foi possível carregar o resumo financeiro.');
    }
}