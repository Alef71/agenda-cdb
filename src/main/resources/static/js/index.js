document.addEventListener("DOMContentLoaded", () => {
    const API_URL = "http://localhost:8080/api/auth"; 

    // Elementos de View
    const viewLogin = document.getElementById('view-login');
    const viewCadastro = document.getElementById('view-cadastro');
    
    // Botões de navegação
    const btnIrCadastro = document.getElementById('btn-ir-cadastro');
    const btnVoltarLogin = document.getElementById('btn-voltar-login');
    
    // Formulários
    const formLogin = document.getElementById('form-login');
    const formCadastro = document.getElementById('form-cadastro');

    // --- NAVEGAÇÃO ENTRE TELAS ---
    function toggleViews() {
        viewLogin.classList.toggle('hidden');
        viewCadastro.classList.toggle('hidden');
    }

    if(btnIrCadastro) {
        btnIrCadastro.addEventListener('click', (e) => {
            e.preventDefault();
            toggleViews();
        });
    }

    if(btnVoltarLogin) {
        btnVoltarLogin.addEventListener('click', (e) => {
            e.preventDefault();
            toggleViews();
        });
    }

    // --- 1. LÓGICA DE LOGIN ---
    if(formLogin) {
        formLogin.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const cpf = document.getElementById('login-cpf').value;
            const senha = document.getElementById('login-senha').value;
            const btn = document.getElementById('btn-submit-login');

            try {
                btn.textContent = "Entrando...";
                btn.disabled = true;

                const response = await fetch(`${API_URL}/login`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ cpf, senha })
                });

                if (response.ok) {
                    const data = await response.json();
                    
                    // Salva o Token JWT e os dados de LoginResponseDTO
                    localStorage.setItem('@CDB:token', data.token);
                    localStorage.setItem('@CDB:user', JSON.stringify({
                        barbeiroId: data.barbeiroId,
                        nome: data.nome,
                        role: data.role,
                        fotoUrl: data.fotoUrl
                    }));

                    // Redirecionamento baseado na Role (AGORA SEM AS BARRAS)
                    if (data.role === 'ADMIN') {
                        window.location.href = "admin.html"; 
                    } else {
                        window.location.href = "agenda.html"; 
                    }

                } else {
                    alert("CPF ou senha incorretos! (Ou usuário pendente)");
                }
            } catch (error) {
                console.error("Erro:", error);
                alert("Erro ao conectar com o servidor.");
            } finally {
                btn.textContent = "Entrar";
                btn.disabled = false;
            }
        });
    }

    // --- 2. LÓGICA DE CADASTRO ---
    if(formCadastro) {
        formCadastro.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const payload = {
                nome: document.getElementById('cad-nome').value,
                cpf: document.getElementById('cad-cpf').value,
                whatsapp: document.getElementById('cad-whatsapp').value,
                senha: document.getElementById('cad-senha').value,
                fotoUrl: document.getElementById('cad-fotoUrl').value
            };

            const btn = document.getElementById('btn-submit-cadastro');

            try {
                btn.textContent = "Cadastrando...";
                btn.disabled = true;

                const response = await fetch(`${API_URL}/registrar`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload)
                });

                if (response.ok) {
                    alert("Cadastro realizado com sucesso! Faça login.");
                    toggleViews(); // Volta para a tela de login
                    formCadastro.reset(); // Limpa o formulário
                } else {
                    let erroMsg = "Verifique se o CPF já existe.";
                    try {
                        const erroJson = await response.json();
                        erroMsg = erroJson.message || erroMsg;
                    } catch {}
                    alert("Erro ao cadastrar: " + erroMsg);
                }
            } catch (error) {
                console.error("Erro:", error);
                alert("Erro de conexão com o servidor.");
            } finally {
                btn.textContent = "Cadastrar";
                btn.disabled = false;
            }
        });
    }
});