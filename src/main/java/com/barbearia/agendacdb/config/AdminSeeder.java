package com.barbearia.agendacdb.config;

import java.util.Optional;

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

    public AdminSeeder(BarbeiroRepository barbeiroRepository, PasswordEncoder passwordEncoder) {
        this.barbeiroRepository = barbeiroRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String cpfAdmin = "07934307632";
        
        
        Optional<Barbeiro> adminExistente = barbeiroRepository.findByCpf(cpfAdmin);

        if (adminExistente.isEmpty()) {
            Barbeiro admin = new Barbeiro();
            admin.setNome("Alef Amaral");
            admin.setCpf(cpfAdmin);
            admin.setWhatsapp("33999026412");
           
            admin.setSenha(passwordEncoder.encode("Afra-1993")); 
            admin.setRole(RoleBarbeiro.ADMIN); 
            admin.setStatus(StatusAprovacao.APROVADO); 

            barbeiroRepository.save(admin);
            System.out.println("✅ Usuário ADMIN (Alef Amaral) criado com sucesso no banco de dados!");
        } else {
            System.out.println("👍 Usuário ADMIN já existe no banco. Pulando criação automática.");
        }
    }
}
