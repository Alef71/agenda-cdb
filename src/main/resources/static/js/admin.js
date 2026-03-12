document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem('@CDB:token');
    const userDataStr = localStorage.getItem('@CDB:user');

    if (!token || !userDataStr) {
        window.location.href = "login.html";
        return;
    }

    const user = JSON.parse(userDataStr);

    if (user.role !== 'ADMIN') {
        alert("Acesso negado. Área restrita para administradores.");
        window.location.href = "login.html";
        return;
    }

    const nomeElement = document.getElementById('nome-admin');
    const fotoElement = document.getElementById('foto-admin');

    if (nomeElement) nomeElement.textContent = user.nome;
    
    if (fotoElement) {
        fotoElement.src = user.fotoUrl || "https://ui-avatars.com/api/?name=" + encodeURIComponent(user.nome) + "&background=D4AF37&color=232129";
    }

    const btnSair = document.getElementById('btn-sair');
    if (btnSair) {
        btnSair.addEventListener('click', () => {
            localStorage.clear();
            window.location.href = "login.html";
        });
    }

    // Inicializa o carregamento do painel
    carregarPainelAdmin();
});

// --- FUNÇÃO PRINCIPAL QUE CARREGA AS DUAS LISTAS ---
async function carregarPainelAdmin() {
    const token = localStorage.getItem('@CDB:token');
    const listaPendentes = document.getElementById('lista-pendentes');
    const listaAprovados = document.getElementById('lista-aprovados');
    const contadorPendentes = document.getElementById('contador-pendentes');

    if (!listaPendentes || !listaAprovados) return;

    try {
        const response = await fetch('http://localhost:8080/api/barbeiros', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`, 
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const barbeiros = await response.json();
            
            // Limpa as duas listas antes de preencher
            listaPendentes.innerHTML = '';
            listaAprovados.innerHTML = '';

            // 1. FILTRO DE PENDENTES
            const pendentes = barbeiros.filter(b => b.role === 'BARBEIRO' && b.status === 'PENDENTE');
            contadorPendentes.textContent = pendentes.length;

            if (pendentes.length === 0) {
                listaPendentes.innerHTML = '<p style="color: #999; text-align: center; padding: 20px;">Nenhum barbeiro aguardando aprovação.</p>';
            } else {
                pendentes.forEach(b => listaPendentes.appendChild(criarCardBarbeiro(b, true)));
            }

            // 2. FILTRO DE APROVADOS (ATIVOS)
            const aprovados = barbeiros.filter(b => b.role === 'BARBEIRO' && b.status === 'APROVADO');
            if (aprovados.length === 0) {
                listaAprovados.innerHTML = '<p style="color: #999; text-align: center; padding: 20px;">Nenhum barbeiro ativo na equipe.</p>';
            } else {
                aprovados.forEach(b => listaAprovados.appendChild(criarCardBarbeiro(b, false)));
            }

            // Renderiza os ícones do Lucide nos novos elementos
            lucide.createIcons(); 
        }
    } catch (error) {
        console.error("Erro ao carregar painel:", error);
    }
}

// --- FUNÇÃO AUXILIAR PARA CRIAR O CARD (EVITA REPETIÇÃO) ---
function criarCardBarbeiro(barbeiro, ehPendente) {
    const card = document.createElement('div');
    card.className = ehPendente ? 'admin-card' : 'admin-card card-ativo';
    
    // Define os botões baseados no status
    const botoes = ehPendente ? `
        <button class="btn-reject" title="Recusar" onclick="recusarBarbeiro('${barbeiro.id}')">
            <i data-lucide="x"></i>
        </button>
        <button class="btn-approve" title="Aprovar" onclick="aprovarBarbeiro('${barbeiro.id}')">
            <i data-lucide="check"></i>
        </button>
    ` : `
        <button class="btn-reject" title="Remover da Equipe" onclick="recusarBarbeiro('${barbeiro.id}')">
            <i data-lucide="trash-2"></i>
        </button>
    `;

    card.innerHTML = `
        <div class="user-info">
            <div class="avatar-placeholder" style="overflow: hidden; padding: 0; width: 50px; height: 50px; border-radius: 50%;">
                <img src="${barbeiro.fotoUrl || 'https://ui-avatars.com/api/?name=' + encodeURIComponent(barbeiro.nome) + '&background=D4AF37&color=232129'}" style="width: 100%; height: 100%; object-fit: cover;" alt="Foto">
            </div>
            <div>
                <strong>${barbeiro.nome}</strong>
                <span style="display: block; font-size: 0.85rem; color: #ccc;">
                    <i data-lucide="phone" style="width: 12px;"></i> ${barbeiro.whatsapp || 'Sem Telefone'}
                </span>
                <small style="color: #888;">CPF: ${barbeiro.cpf}</small>
            </div>
        </div>
        <div class="action-buttons">
            ${botoes}
        </div>
    `;
    return card;
}

// --- FUNÇÃO APROVAR ---
window.aprovarBarbeiro = async function(id) {
    if(!confirm("Deseja aprovar este barbeiro? Ele terá acesso ao sistema.")) return;

    const token = localStorage.getItem('@CDB:token');
    try {
        const response = await fetch(`http://localhost:8080/api/barbeiros/${id}/aprovar`, {
            method: 'PUT',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            alert("Barbeiro aprovado com sucesso!");
            carregarPainelAdmin(); 
        } else {
            alert("Erro ao aprovar barbeiro.");
        }
    } catch (error) {
        console.error("Erro:", error);
        alert("Erro de conexão.");
    }
}

// --- FUNÇÃO RECUSAR / EXCLUIR ---
window.recusarBarbeiro = async function(id) {
    const msg = "Tem certeza que deseja remover este cadastro? Esta ação não pode ser desfeita.";
    if(!confirm(msg)) return;

    const token = localStorage.getItem('@CDB:token');
    try {
        const response = await fetch(`http://localhost:8080/api/barbeiros/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok || response.status === 204) {
            alert("Cadastro removido com sucesso!");
            carregarPainelAdmin(); 
        } else {
            alert("Erro ao remover barbeiro.");
        }
    } catch (error) {
        console.error("Erro:", error);
        alert("Erro de conexão.");
    }
}