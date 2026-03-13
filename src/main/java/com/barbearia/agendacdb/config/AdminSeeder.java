package com.barbearia.agendacdb.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.barbearia.agendacdb.enums.RoleBarbeiro;
import com.barbearia.agendacdb.enums.StatusAprovacao;
import com.barbearia.agendacdb.models.Barbeiro;
import com.barbearia.agendacdb.repositories.BarbeiroRepository;

@Configuration
public class AdminSeeder implements CommandLineRunner {

    private final BarbeiroRepository barbeiroRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.default.nome}")
    private String adminNome;

    @Value("${admin.default.cpf}")
    private String adminCpf;

    @Value("${admin.default.whatsapp}")
    private String adminWhatsapp;

    @Value("${admin.default.senha}")
    private String adminSenha;

    public AdminSeeder(BarbeiroRepository barbeiroRepository, PasswordEncoder passwordEncoder) {
        this.barbeiroRepository = barbeiroRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<Barbeiro> adminExistente = barbeiroRepository.findByCpf(adminCpf);

        if (adminExistente.isEmpty()) {
            Barbeiro admin = new Barbeiro();
        
            admin.setNome(adminNome);
            admin.setCpf(adminCpf);
            admin.setWhatsapp(adminWhatsapp);
            admin.setSenha(passwordEncoder.encode(adminSenha)); 
            admin.setRole(RoleBarbeiro.ADMIN); 
            admin.setStatus(StatusAprovacao.APROVADO); 

            barbeiroRepository.save(admin);
            System.out.println("✅ Usuário ADMIN (" + adminNome + ") criado com sucesso no banco de dados!");
        } else {
            System.out.println("👍 Usuário ADMIN já existe no banco. Pulando criação automática.");
        }
    }
}