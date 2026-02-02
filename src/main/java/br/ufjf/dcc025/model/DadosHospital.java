

package br.ufjf.dcc025.model;

import br.ufjf.dcc025.exceptions.CPFDuplicadoException;
import br.ufjf.dcc025.model.repository.ConsultaRepository;
import br.ufjf.dcc025.model.repository.MedicoRepository;
import br.ufjf.dcc025.model.repository.PacienteRepository;
import br.ufjf.dcc025.model.repository.SecretariaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DadosHospital {


    private static DadosHospital instance;


    private List<Paciente> pacientes;
    private List<Medico> medicos;
    private List<Consulta> consultas;
    private List<Secretaria> secretarias;


    private DadosHospital() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.consultas = new ArrayList<>();
        this.secretarias = new ArrayList<>();
    }


    public static synchronized DadosHospital getInstance() {
        if (instance == null) {
            instance = new DadosHospital();
        }
        return instance;
    }

    public List<Paciente> getPacientes() { return Collections.unmodifiableList(pacientes); }
    public List<Medico> getMedicos() { return Collections.unmodifiableList(medicos); }
    public List<Consulta> getConsultas() { return Collections.unmodifiableList(consultas); }
    public List<Secretaria> getSecretarias() { return Collections.unmodifiableList(secretarias); }

    public void addPaciente(Paciente paciente) {
        this.pacientes.add(paciente);
    }
    public void addMedico(Medico medico) {
        this.medicos.add(medico);
    }
    public void addConsulta(Consulta consulta) {
        this.consultas.add(consulta);
    }
    public void addSecretaria(Secretaria secretaria) {
        this.secretarias.add(secretaria);
    }

    public void carregarDados() {

        PacienteRepository pacienteRepository = new PacienteRepository();
        MedicoRepository medicoRepository = new MedicoRepository();
        SecretariaRepository secretariaRepository = new SecretariaRepository();
        ConsultaRepository consultaRepository = new ConsultaRepository();

        this.pacientes.clear();
        this.medicos.clear();
        this.secretarias.clear();
        this.consultas.clear();


        this.pacientes.addAll(pacienteRepository.findAll());
        this.medicos.addAll(medicoRepository.findAll());
        this.secretarias.addAll(secretariaRepository.findAll());
        this.consultas.addAll(consultaRepository.findAll());


        carregarConsultas();

        System.out.println("Carregando dados dos pacientes: " + pacientes.size());
        System.out.println("Carregando dados dos medicos: " + medicos.size());
        System.out.println("Carregando dados das secretarias: " + secretarias.size());
        System.out.println("Carregando dados das consultas: " + consultas.size());
        System.out.println("Dados carregados com sucesso!");
    }

    public void salvarDados() {
        PacienteRepository pacienteRepository = new PacienteRepository();
        MedicoRepository medicoRepository = new MedicoRepository();
        SecretariaRepository secretariaRepository = new SecretariaRepository();
        ConsultaRepository consultaRepository = new ConsultaRepository();

        pacienteRepository.save(this.pacientes);
        medicoRepository.save(this.medicos);
        secretariaRepository.save(this.secretarias);
        consultaRepository.save(this.consultas);

        System.out.println("Salvando dados");
    }


    private void carregarConsultas() {
        for (Consulta consulta : this.consultas) {

            if(consulta.getMedico() != null) {
                String cpfMedico = consulta.getMedico().getCpf();
                Medico medico = buscarMedicoPorCpf(cpfMedico);
                if (medico != null) {
                    medico.novaConsulta(consulta);
                    consulta.setMedico(medico);
                }
            }

            if(consulta.getPaciente() != null) {
                String cpfPaciente = consulta.getPaciente().getCpf();
                Paciente paciente = buscarPacientePorCpf(cpfPaciente);
                if (paciente != null) {
                    paciente.novaConsulta(consulta);
                    consulta.setPaciente(paciente);
                }
            }
        }
    }

    private Medico buscarMedicoPorCpf(String cpf) {
        for (Medico m : this.medicos) {
            if (m.getCpf().equals(cpf)) return m;
        }
        return null;
    }

    private Paciente buscarPacientePorCpf(String cpf) {
        for (Paciente p : this.pacientes) {
            if (p.getCpf().equals(cpf)) return p;
        }
        return null;
    }

    public boolean cpfJaExiste(String cpf) throws CPFDuplicadoException {

        String cpfInputLimpo = cpf.replaceAll("[^0-9]", "");

        for (Medico m : DadosHospital.getInstance().getMedicos()) {
            String cpfBancoLimpo = m.getCpf().replaceAll("[^0-9]", "");

            if (cpfBancoLimpo.equals(cpfInputLimpo))
               return true;
        }

        for (Secretaria s : DadosHospital.getInstance().getSecretarias()) {
            String cpfBancoLimpo = s.getCpf().replaceAll("[^0-9]", "");

            if (cpfBancoLimpo.equals(cpfInputLimpo))
                return true;
        }

        for(Paciente p : DadosHospital.getInstance().getPacientes()){
            String cpfBancoLimpo = p.getCpf().replaceAll("[^0-9]", "");

            if(cpfBancoLimpo.equals(cpfInputLimpo))
               return true;
        }

        return false;
    }
}