package br.ufjf.dcc025.model.repository;

import br.ufjf.dcc025.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository implements Repository<Consulta> {
    private static final String PATH = DIRECTORY + File.separator + "consultas.json";

    @Override
    public List<Consulta> findAll() {
        File arquivo = new File(PATH);
        if (!arquivo.exists()) return new ArrayList<>();

        String json = Arquivo.le(PATH);
        if (json.trim().isEmpty()) return new ArrayList<>();

        final GsonRuntimeTypeAdapterFactory<DocumentoMedico> typeFactory = GsonRuntimeTypeAdapterFactory
                .of(DocumentoMedico.class, "type").registerSubtype(AtestadoMedico.class, "Atestado")
                .registerSubtype(ReceitaMedica.class, "Receita")
                .registerSubtype(ExameMedico.class, "Exame");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(typeFactory)
                .setPrettyPrinting()
                .create();

        Type tipoLista = new TypeToken<List<Consulta>>() {}.getType();

        List<Consulta> consultas = gson.fromJson(json, tipoLista);
        if(consultas == null)
            consultas = new ArrayList<>();
        return consultas;
    }

    @Override
    public void save(List<Consulta> itens) {

        final GsonRuntimeTypeAdapterFactory<DocumentoMedico> typeFactory = GsonRuntimeTypeAdapterFactory
                .of(DocumentoMedico.class, "type").registerSubtype(AtestadoMedico.class, "Atestado")
                .registerSubtype(ReceitaMedica.class, "Receita")
                .registerSubtype(ExameMedico.class, "Exame");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(typeFactory)
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(itens);

        File diretorio = new File(DIRECTORY);
        if (!diretorio.exists()) diretorio.mkdirs();

        Arquivo.salva(PATH, json);
    }
}
