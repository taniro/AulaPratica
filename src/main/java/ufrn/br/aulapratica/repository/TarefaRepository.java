package ufrn.br.aulapratica.repository;

import ufrn.br.aulapratica.model.Tarefa;
import java.util.ArrayList;
import java.util.List;

public class TarefaRepository {
    private static final List<Tarefa> bancoDeDados = new ArrayList<>();

    public Tarefa salvar(Tarefa tarefa) {
        bancoDeDados.add(tarefa);
        return tarefa;
    }

    public List<Tarefa> buscarTodas() {
        // Retorna uma cópia para evitar modificações indevidas na lista original
        return new ArrayList<>(bancoDeDados);
    }
}