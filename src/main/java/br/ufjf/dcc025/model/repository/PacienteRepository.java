package br.ufjf.dcc025.model.repository;

import br.ufjf.dcc025.model.Medico;
import br.ufjf.dcc025.model.Paciente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository implements  Repository<Paciente> {

    private static final String PATH = DIRECTORY + File.separator + "pacientes.json";

    @Override
    public List<Paciente> findAll() {
        File arquivo = new File(PATH);
        if (!arquivo.exists()) return new ArrayList<>();

        String json = Arquivo.le(PATH);
        if (json.trim().isEmpty()) return new ArrayList<>();

        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<Paciente>>() {}.getType();

        List<Paciente> pacientes = gson.fromJson(json, tipoLista);
        if(pacientes == null)
            pacientes = new ArrayList<>();
        return pacientes;
    }

    @Override
    public void save(List<Paciente> itens) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(itens);

        File diretorio = new File(DIRECTORY);
        if (!diretorio.exists()) diretorio.mkdirs();

        Arquivo.salva(PATH, json);
    }
}
