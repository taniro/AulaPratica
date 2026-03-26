package ufrn.br.aulapratica.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public  class Tarefa {
    private Long id;
    private String texto;
    private static final AtomicLong geradorId = new AtomicLong(1);


    public Tarefa(String texto) {
        this.id = geradorId.accumulateAndGet(1L, Long::sum);
        this.texto = texto;
    }

    public Long id() {
        return id;
    }

    public String texto() {
        return texto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Tarefa) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.texto, that.texto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, texto);
    }

    @Override
    public String toString() {
        return "Tarefa[" +
                "id=" + id + ", " +
                "texto=" + texto + ']';
    }

}
