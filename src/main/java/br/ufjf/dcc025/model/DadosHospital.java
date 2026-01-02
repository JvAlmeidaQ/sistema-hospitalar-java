package br.ufjf.dcc025.model;

import br.ufjf.dcc025.model.repository.ConsultaRepository;
import br.ufjf.dcc025.model.repository.MedicoRepository;
import br.ufjf.dcc025.model.repository.PacienteRepository;
import br.ufjf.dcc025.model.repository.SecretariaRepository;

import java.util.ArrayList;
import java.util.List;

public class DadosHospital {
    public static final List<Paciente> pacientes = new ArrayList<>();
    public static final List<Medico> medicos = new ArrayList<>();
    public static final List<Consulta> consultas = new ArrayList<>();
    public static final List<Secretaria> secretarias = new ArrayList<>();

    private DadosHospital() {}

    public static void carregarDados()
    {
        PacienteRepository pacienteRepository = new PacienteRepository();
        MedicoRepository medicoRepository = new MedicoRepository();
        SecretariaRepository secretariaRepository = new SecretariaRepository();
        ConsultaRepository consultaRepository = new ConsultaRepository();

        pacientes.clear();
        medicos.clear();
        secretarias.clear();
        consultas.clear();

        pacientes.addAll(pacienteRepository.findAll());
        medicos.addAll(medicoRepository.findAll());
        secretarias.addAll(secretariaRepository.findAll());
        consultas.addAll(consultaRepository.findAll());

        System.out.println("Carregando dados dos pacientes: " +  pacientes.size());
        System.out.println("Carregando dados dos medicos: " +  medicos.size());
        System.out.println("Carregando dados das secretarias: " +  secretarias.size());
        System.out.println("Carregando dados dos consultas: " +  consultas.size());
        System.out.println("Dados carregados com sucesso!");
    }

    public static void salvarDados()
    {
        PacienteRepository pacienteRepository = new PacienteRepository();
        MedicoRepository medicoRepository = new MedicoRepository();
        SecretariaRepository secretariaRepository = new SecretariaRepository();
        ConsultaRepository consultaRepository = new ConsultaRepository();

        pacienteRepository.save(pacientes);
        medicoRepository.save(medicos);
        secretariaRepository.save(secretarias);
        consultaRepository.save(consultas);

        System.out.println("Salvando dados dos pacientes: " +  pacientes.size());
        System.out.println("Salvando dados dos medicos: " +  medicos.size());
        System.out.println("Salvando dados das secretarias: " +  secretarias.size());
        System.out.println("Salvando dados dos consultas: " +  consultas.size());

    }
}
