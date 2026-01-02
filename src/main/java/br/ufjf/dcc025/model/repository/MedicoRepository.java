package br.ufjf.dcc025.model.repository;

import br.ufjf.dcc025.model.Medico; // Importa o tipo CONCRETO
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MedicoRepository implements Repository<Medico> {

    private static final String PATH = DIRECTORY + File.separator + "medicos.json";

    @Override
    public List<Medico> findAll() {
        File arquivo = new File(PATH);
        if (!arquivo.exists()) return new ArrayList<>();

        String json = Arquivo.le(PATH);
        if (json.trim().isEmpty()) return new ArrayList<>();

        Gson gson = GsonConfiguration.createGson();
        Type tipoLista = new TypeToken<List<Medico>>() {}.getType();

        List<Medico> medicos = gson.fromJson(json, tipoLista);
        if(medicos == null)
            medicos = new ArrayList<>();
        return medicos;
    }

    @Override
    public void save(List<Medico> itens) {
        Gson gson = GsonConfiguration.createGson();
        String json = gson.toJson(itens);

        File diretorio = new File(DIRECTORY);
        if (!diretorio.exists()) diretorio.mkdirs();

        Arquivo.salva(PATH, json);
    }
}